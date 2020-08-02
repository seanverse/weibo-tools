package vip.seanxq.weibo.mp.api.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.bean.WeiboAccessToken;
import vip.seanxq.weibo.common.bean.WeiboJsapiSignature;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;
import vip.seanxq.weibo.common.enums.TicketType;
import vip.seanxq.weibo.common.enums.WbMessageFormat;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.session.StandardSessionManager;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.common.util.DataUtils;
import vip.seanxq.weibo.common.util.RandomUtils;
import vip.seanxq.weibo.common.util.crypto.SHA1;
import vip.seanxq.weibo.common.util.http.*;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

import vip.seanxq.weibo.mp.api.*;
import vip.seanxq.weibo.mp.bean.result.WeiboMpCurrentAutoReplyInfo;
import vip.seanxq.weibo.mp.bean.result.WeiboMpOAuth2AccessToken;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUser;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;
import vip.seanxq.weibo.mp.util.WeiboMpConfigStorageHolder;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * 基础实现类.
 *
 * @author someone
 */
@Slf4j
public abstract class BaseWeiboMpServiceImpl<H, P> implements WeiboMpService, RequestHttp<H, P> {
  private static final JsonParser JSON_PARSER = new JsonParser();

  protected WeiboSessionManager sessionManager = new StandardSessionManager();
  private WeibCustomMenuService menuService = new WeibCustomMenuServiceImpl(this);
  private WeiboFansUserService userService = new WeiboFansUserServiceImpl(this);
  private WeiboFansGroupService userGroupService = new WeiboFansGroupServiceImpl(this);
  private WeiboFansQrcodeService qrCodeService = new WeiboFansQrcodeServiceImpl(this);
  private WeiboFansMessageService messageService = new WeiboFansMessageServiceImpl(this);

  private Map<String, WeiboConfigStorage> configStorageMap;

  private int retrySleepMillis = 1000;
  private int maxRetryTimes = 5;

  /**
   * Service的实现只以json格式方式实现，不支持xml
   * https://open.weibo.com/wiki/Eps/push/set_format
   * @param format xml/json
   * @throws WeiboErrorException error
   */
  @Override
  @Deprecated
  public boolean setDataFormat(WbMessageFormat format) throws WeiboErrorException {
      //WbMessageFormat format = this.getWxMpConfigStorage().getMessageFormat();
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("access_token", this.getAccessToken());
    jsonObject.addProperty("format", format.getData_format());
    log.debug("设定message format" + format.getData_format());
    String responseContent = this.post(WeiboMpApiUrl.Other.SET_DATA_FORMAT_URL, jsonObject.toString());
    return responseContent != null && !responseContent.contains("error_code"); //不含error_code表示设定成功
  }

  @Override
  public boolean checkSignature(String timestamp, String nonce, String signature) {
    try {
      return SHA1.gen(this.getWxMpConfigStorage().getToken(), timestamp, nonce)
        .equals(signature);
    } catch (Exception e) {
      log.error("Checking signature failed, and the reason is :" + e.getMessage());
      return false;
    }
  }

  @Override
  public String getAccessToken() throws WeiboErrorException {
    return getAccessToken(false);
  }

  @Override
  public String get(String url, String queryParam) throws WeiboErrorException {
    return execute(SimpleGetRequestExecutor.create(this), url, queryParam);
  }

  @Override
  public String get(WeiboMpApiUrl url, String queryParam) throws WeiboErrorException {
    return this.get(url.getUrl(this.getWxMpConfigStorage()), queryParam);
  }

  @Override
  public String post(String url, String postData) throws WeiboErrorException {
    return execute(SimplePostRequestExecutor.create(this), url, postData);
  }

  @Override
  public String post(WeiboMpApiUrl url, String postData) throws WeiboErrorException {
    return this.post(url.getUrl(this.getWxMpConfigStorage()), postData);
  }

  @Override
  public String post(String url, Object obj) throws WeiboErrorException {
    return this.execute(SimplePostRequestExecutor.create(this), url, WeiboGsonBuilder.create().toJson(obj));
  }

  @Override
  public <T, E> T execute(RequestExecutor<T, E> executor, WeiboMpApiUrl url, E data) throws WeiboErrorException {
    return this.execute(executor, url.getUrl(this.getWxMpConfigStorage()), data);
  }

  /**
   * 向微博端发送请求，在这里执行的策略是当发生access_token过期时才去刷新，然后重新执行请求，而不是全局定时请求.
   */
  @Override
  public <T, E> T execute(RequestExecutor<T, E> executor, String uri, E data) throws WeiboErrorException {
    int retryTimes = 0;
    do {
      try {
        return this.executeInternal(executor, uri, data);
      } catch (WeiboErrorException e) {
        if (retryTimes + 1 > this.maxRetryTimes) {
          log.warn("重试达到最大次数【{}】", maxRetryTimes);
          //最后一次重试失败后，直接抛出异常，不再等待
          throw new RuntimeException("微博服务端异常，超出重试次数");
        }

        WeiboError error = e.getError();
        // -1 系统繁忙, 1000ms后重试
        if (error.getErrorCode() == -1) {
          int sleepMillis = this.retrySleepMillis * (1 << retryTimes);
          try {
            log.warn("微博系统繁忙，{} ms 后重试(第{}次)", sleepMillis, retryTimes + 1);
            Thread.sleep(sleepMillis);
          } catch (InterruptedException e1) {
            throw new RuntimeException(e1);
          }
        } else {
          throw e;
        }
      }
    } while (retryTimes++ < this.maxRetryTimes);

    log.warn("重试达到最大次数【{}】", this.maxRetryTimes);
    throw new RuntimeException("微博服务端异常，超出重试次数");
  }

  protected <T, E> T executeInternal(RequestExecutor<T, E> executor, String uri, E data) throws WeiboErrorException {
    E dataForLog = DataUtils.handleDataWithSecret(data);

    if (uri.contains("access_token=")) {
      throw new IllegalArgumentException("uri参数中不允许有access_token: " + uri);
    }

    String accessToken = getAccessToken(false);

    //weixin是在每个uri的queryparam带上?access_token = accessToken
    //weibo的Post则是放入access_token放入request body中
    //例如：curl "https://m.api.weibo.com/2/messages/custom_rule/update.json" -d 'access_token=ACCESS_TOKEN&id=ID&name=NAME'
    //get则是放在queryparam ，例如：curl "https://m.api.weibo.com/2/messages/custom_rule/getid.json?access_token=ACCESS_TOKEN&follower_id=FOLLOWER_ID"

    String uriWithAccessToken = uri + (uri.contains("?") ? "&" : "?") + "access_token=" + accessToken;
    //todo: mark_seanx:实际执行之处 URLEncoder.encode(str, "UTF-8");
    try {
      T result = executor.execute(uriWithAccessToken, data, WeiboType.MP);
      log.debug("\n【请求地址】: {}\n【请求参数】：{}\n【响应数据】：{}", uriWithAccessToken, dataForLog, result);
      return result;
    } catch (WeiboErrorException e) {
      WeiboError error = e.getError();
      /*
       * 发生以下情况时尝试刷新access_token
       * 40001 获取 access_token 时 AppSecret 错误，或者 access_token 无效。请开发者认真比对 AppSecret 的正确性，或查看是否正在为恰当的公众号调用接口
       * 42001 access_token 超时，请检查 access_token 的有效期，请参考基础支持 - 获取 access_token 中，对 access_token 的详细机制说明
       * 40014 不合法的 access_token ，请开发者认真比对 access_token 的有效性（如是否过期），或查看是否正在为恰当的公众号调用接口
       */
      if (error.getErrorCode() == 42001 || error.getErrorCode() == 40001 || error.getErrorCode() == 40014) {
        // 强制设置wxMpConfigStorage它的access token过期了，这样在下一次请求里就会刷新access token
        Lock lock = this.getWxMpConfigStorage().getAccessTokenLock();
        lock.lock();
        try {
          if (StringUtils.equals(this.getWxMpConfigStorage().getAccessToken(), accessToken)) {
            this.getWxMpConfigStorage().expireAccessToken();
          }
        } catch (Exception ex) {
          this.getWxMpConfigStorage().expireAccessToken();
        } finally {
          lock.unlock();
        }
        // 尝试刷新access_token
        if (this.getWxMpConfigStorage().autoRefreshToken()) {
          return this.execute(executor, uri, data);
        }
      }

      if (error.getErrorCode() != 0) {
        log.error("\n【请求地址】: {}\n【请求参数】：{}\n【错误信息】：{}", uriWithAccessToken, dataForLog, error);
        throw new WeiboErrorException(error, e);
      }
      return null;
    } catch (IOException e) {
      log.error("\n【请求地址】: {}\n【请求参数】：{}\n【异常信息】：{}", uriWithAccessToken, dataForLog, e.getMessage());
      throw new WeiboErrorException(WeiboError.builder().errorMsg(e.getMessage()).build(), e);
    }
  }

  @Override
  public WeiboConfigStorage getWxMpConfigStorage() {
    if (this.configStorageMap.size() == 1) {
      // 只有一个公众号，直接返回其配置即可
      return this.configStorageMap.values().iterator().next();
    }

    return this.configStorageMap.get(WeiboMpConfigStorageHolder.get());
  }

  protected String extractAccessToken(String resultContent) throws WeiboErrorException {
    WeiboConfigStorage config = this.getWxMpConfigStorage();
    WeiboError error = WeiboError.fromJson(resultContent, WeiboType.MP);
    if (error.getErrorCode() != 0) {
      throw new WeiboErrorException(error);
    }
    WeiboAccessToken accessToken = WeiboAccessToken.fromJson(resultContent);
    config.updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
    return config.getAccessToken();
  }

  @Override
  public void setWxMpConfigStorage(WeiboConfigStorage wxConfigProvider) {
    final String defaultMpId = WeiboMpConfigStorageHolder.get();
    this.setMultiConfigStorages(ImmutableMap.of(defaultMpId, wxConfigProvider), defaultMpId);
  }

  @Override
  public void setMultiConfigStorages(Map<String, WeiboConfigStorage> configStorages) {
    this.setMultiConfigStorages(configStorages, configStorages.keySet().iterator().next());
  }

  @Override
  public void setMultiConfigStorages(Map<String, WeiboConfigStorage> configStorages, String defaultMpId) {
    this.configStorageMap = Maps.newHashMap(configStorages);
    WeiboMpConfigStorageHolder.set(defaultMpId);
    this.initHttp();
  }

  @Override
  public void addConfigStorage(String mpId, WeiboConfigStorage configStorages) {
    synchronized (this) {
      this.configStorageMap.put(mpId, configStorages);
    }
  }

  @Override
  public void removeConfigStorage(String mpId) {
    synchronized (this) {
      this.configStorageMap.remove(mpId);
    }
  }

  @Override
  public WeiboMpService switchoverTo(String weiboId) {
    if (this.configStorageMap.containsKey(weiboId)) {
      WeiboMpConfigStorageHolder.set(weiboId);
      return this;
    }

    throw new RuntimeException(String.format("无法找到对应【%s】的微博企业号配置信息，请核实！", weiboId));
  }

  @Override
  public boolean switchover(String weiboId) {
    if (this.configStorageMap.containsKey(weiboId)) {
      WeiboMpConfigStorageHolder.set(weiboId);
      return true;
    }

    log.error("无法找到对应【{}】的微博企业号配置信息，请核实！", weiboId);
    return false;
  }

  @Override
  public void setRetrySleepMillis(int retrySleepMillis) {
    this.retrySleepMillis = retrySleepMillis;
  }

  @Override
  public void setMaxRetryTimes(int maxRetryTimes) {
    this.maxRetryTimes = maxRetryTimes;
  }

  @Override
  public WeibCustomMenuService getMenuService() {
    return this.menuService;
  }

  @Override
  public WeiboFansUserService getUserService() {
    return this.userService;
  }

  @Override
  public WeiboFansGroupService getUserTagService() {
    return this.userGroupService;
  }

  @Override
  public WeiboFansQrcodeService getQrcodeService() {
    return this.qrCodeService;
  }

  /**
   * 发送消息相关的Service
   */
  @Override
  public WeiboFansMessageService getMessageService() {
    return this.messageService;
  }

  @Override
  public RequestHttp getRequestHttp() {
    return this;
  }

  @Override
  public void setMenuService(WeibCustomMenuService menuService) {
    this.menuService = menuService;
  }

  @Override
  public void setUserService(WeiboFansUserService userService) {
    this.userService = userService;
  }

  @Override
  public void setUserGroupService(WeiboFansGroupService userGroupService) {
    this.userGroupService = userGroupService;
  }

  @Override
  public void setQrCodeService(WeiboFansQrcodeService qrCodeService) {
    this.qrCodeService = qrCodeService;
  }

  /**
   * .
   *
   * @param messageService
   */
  @Override
  public void setMessageService(WeiboFansMessageService messageService) { this.messageService = messageService;}

}
