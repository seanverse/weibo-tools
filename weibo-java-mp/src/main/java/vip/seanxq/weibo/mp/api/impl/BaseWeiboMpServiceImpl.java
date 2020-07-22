package vip.seanxq.weibo.mp.api.impl;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.bean.WeiboAccessToken;
import vip.seanxq.weibo.common.bean.WeiboJsapiSignature;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;
import vip.seanxq.weibo.common.enums.TicketType;
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
import vip.seanxq.weibo.mp.bean.WeiboMpSemanticQuery;
import vip.seanxq.weibo.mp.bean.result.WeiboMpCurrentAutoReplyInfo;
import vip.seanxq.weibo.mp.bean.result.WeiboMpOAuth2AccessToken;
import vip.seanxq.weibo.mp.bean.result.WeiboMpSemanticQueryResult;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUser;
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
  private WeiboMpMaterialService materialService = new WeiboMpMaterialServiceImpl(this);
  private WeibMpMenuService menuService = new WeibMpMenuServiceImpl(this);
  private WeiboMpUserService userService = new WeiboMpUserServiceImpl(this);
  private WeiboMpUserTagService tagService = new WeiboMpUserTagServiceImpl(this);
  private WeibMpQrcodeService qrCodeService = new WeiboMpQrcodeServiceImpl(this);
  private WeiboDataCubeService dataCubeService = new WeiboDataCubeServiceImpl(this);
  private WeiboMpUserBlacklistService blackListService = new WeiboMpUserBlacklistServiceImpl(this);
  private final WeiboMpSubscribeMsgService subscribeMsgService = new WeiboMpSubscribeMsgServiceImpl(this);
  private WeibMpMassMessageService massMessageService = new WeibMpMassMessageServiceImpl(this);
  private WeiboImgProcService imgProcService = new WeiboImgProcServiceImpl(this);

  private Map<String, WeiboConfigStorage> configStorageMap;

  private int retrySleepMillis = 1000;
  private int maxRetryTimes = 5;

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
  public String getTicket(TicketType type) throws WeiboErrorException {
    return this.getTicket(type, false);
  }

  @Override
  public String getTicket(TicketType type, boolean forceRefresh) throws WeiboErrorException {
    Lock lock = this.getWxMpConfigStorage().getTicketLock(type);
    try {
      lock.lock();
      if (forceRefresh) {
        this.getWxMpConfigStorage().expireTicket(type);
      }

      if (this.getWxMpConfigStorage().isTicketExpired(type)) {
        String responseContent = execute(SimpleGetRequestExecutor.create(this),
          WeiboMpApiUrl.Other.GET_TICKET_URL.getUrl(this.getWxMpConfigStorage()) + type.getCode(), null);
        JsonObject tmpJsonObject = JSON_PARSER.parse(responseContent).getAsJsonObject();
        String jsapiTicket = tmpJsonObject.get("ticket").getAsString();
        int expiresInSeconds = tmpJsonObject.get("expires_in").getAsInt();
        this.getWxMpConfigStorage().updateTicket(type, jsapiTicket, expiresInSeconds);
      }
    } finally {
      lock.unlock();
    }

    return this.getWxMpConfigStorage().getTicket(type);
  }

  @Override
  public String getJsapiTicket() throws WeiboErrorException {
    return this.getJsapiTicket(false);
  }

  @Override
  public String getJsapiTicket(boolean forceRefresh) throws WeiboErrorException {
    return this.getTicket(TicketType.JSAPI, forceRefresh);
  }

  @Override
  public WeiboJsapiSignature createJsapiSignature(String url) throws WeiboErrorException {
    long timestamp = System.currentTimeMillis() / 1000;
    String randomStr = RandomUtils.getRandomStr();
    String jsapiTicket = getJsapiTicket(false);
    String signature = SHA1.genWithAmple("jsapi_ticket=" + jsapiTicket,
      "noncestr=" + randomStr, "timestamp=" + timestamp, "url=" + url);
    WeiboJsapiSignature jsapiSignature = new WeiboJsapiSignature();
    jsapiSignature.setAppId(this.getWxMpConfigStorage().getAppId());
    jsapiSignature.setTimestamp(timestamp);
    jsapiSignature.setNonceStr(randomStr);
    jsapiSignature.setUrl(url);
    jsapiSignature.setSignature(signature);
    return jsapiSignature;
  }

  @Override
  public String getAccessToken() throws WeiboErrorException {
    return getAccessToken(false);
  }

  @Override
  public String shortUrl(String longUrl) throws WeiboErrorException {
    if (longUrl.contains("&access_token=")) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1)
        .errorMsg("要转换的网址中存在非法字符｛&access_token=｝，会导致微博接口报错，属于微博bug，请调整地址，否则不建议使用此方法！")
        .build());
    }

    JsonObject o = new JsonObject();
    o.addProperty("action", "long2short");
    o.addProperty("long_url", longUrl);
    String responseContent = this.post(WeiboMpApiUrl.Other.SHORTURL_API_URL, o.toString());
    JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
    return tmpJsonElement.getAsJsonObject().get("short_url").getAsString();
  }

  @Override
  public WeiboMpSemanticQueryResult semanticQuery(WeiboMpSemanticQuery semanticQuery) throws WeiboErrorException {
    String responseContent = this.post(WeiboMpApiUrl.Other.SEMANTIC_SEMPROXY_SEARCH_URL, semanticQuery.toJson());
    return WeiboMpSemanticQueryResult.fromJson(responseContent);
  }

  @Override
  public String oauth2buildAuthorizationUrl(String redirectURI, String scope, String state) {
    return String.format(WeiboMpApiUrl.Other.CONNECT_OAUTH2_AUTHORIZE_URL.getUrl(this.getWxMpConfigStorage()),
      this.getWxMpConfigStorage().getAppId(), URIUtil.encodeURIComponent(redirectURI), scope, StringUtils.trimToEmpty(state));
  }

  @Override
  public String buildQrConnectUrl(String redirectURI, String scope, String state) {
    return String.format(WeiboMpApiUrl.Other.QRCONNECT_URL.getUrl(this.getWxMpConfigStorage()), this.getWxMpConfigStorage().getAppId(),
      URIUtil.encodeURIComponent(redirectURI), scope, StringUtils.trimToEmpty(state));
  }

  private WeiboMpOAuth2AccessToken getOAuth2AccessToken(String url) throws WeiboErrorException {
    try {
      RequestExecutor<String, String> executor = SimpleGetRequestExecutor.create(this);
      String responseText = executor.execute(url, null, WeiboType.MP);
      return WeiboMpOAuth2AccessToken.fromJson(responseText);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public WeiboMpOAuth2AccessToken oauth2getAccessToken(String code) throws WeiboErrorException {
    String url = String.format(WeiboMpApiUrl.Other.OAUTH2_ACCESS_TOKEN_URL.getUrl(this.getWxMpConfigStorage()), this.getWxMpConfigStorage().getAppId(),
      this.getWxMpConfigStorage().getSecret(), code);
    return this.getOAuth2AccessToken(url);
  }

  @Override
  public WeiboMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WeiboErrorException {
    String url = String.format(WeiboMpApiUrl.Other.OAUTH2_REFRESH_TOKEN_URL.getUrl(this.getWxMpConfigStorage()), this.getWxMpConfigStorage().getAppId(), refreshToken);
    return this.getOAuth2AccessToken(url);
  }

  @Override
  public WeiboMpUser oauth2getUserInfo(WeiboMpOAuth2AccessToken token, String lang) throws WeiboErrorException {
    if (lang == null) {
      lang = "zh_CN";
    }

    String url = String.format(WeiboMpApiUrl.Other.OAUTH2_USERINFO_URL.getUrl(this.getWxMpConfigStorage()), token.getAccessToken(), token.getOpenId(), lang);

    try {
      RequestExecutor<String, String> executor = SimpleGetRequestExecutor.create(this);
      String responseText = executor.execute(url, null, WeiboType.MP);
      return WeiboMpUser.fromJson(responseText);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public boolean oauth2validateAccessToken(WeiboMpOAuth2AccessToken token) {
    String url = String.format(WeiboMpApiUrl.Other.OAUTH2_VALIDATE_TOKEN_URL.getUrl(this.getWxMpConfigStorage()), token.getAccessToken(), token.getOpenId());

    try {
      SimpleGetRequestExecutor.create(this).execute(url, null, WeiboType.MP);
    } catch (IOException e) {
      throw new RuntimeException(e);
    } catch (WeiboErrorException e) {
      return false;
    }
    return true;
  }

  @Override
  public String[] getCallbackIP() throws WeiboErrorException {
    String responseContent = this.get(WeiboMpApiUrl.Other.GET_CALLBACK_IP_URL, null);
    JsonElement tmpJsonElement = JSON_PARSER.parse(responseContent);
    JsonArray ipList = tmpJsonElement.getAsJsonObject().get("ip_list").getAsJsonArray();
    String[] ipArray = new String[ipList.size()];
    for (int i = 0; i < ipList.size(); i++) {
      ipArray[i] = ipList.get(i).getAsString();
    }
    return ipArray;
  }

  @Override
  public WeiboNetCheckResult netCheck(String action, String operator) throws WeiboErrorException {
    JsonObject o = new JsonObject();
    o.addProperty("action", action);
    o.addProperty("check_operator", operator);
    String responseContent = this.post(WeiboMpApiUrl.Other.NETCHECK_URL, o.toString());
    return WeiboNetCheckResult.fromJson(responseContent);
  }

  @Override
  public WeiboMpCurrentAutoReplyInfo getCurrentAutoReplyInfo() throws WeiboErrorException {
    return WeiboMpCurrentAutoReplyInfo.fromJson(this.get(WeiboMpApiUrl.Other.GET_CURRENT_AUTOREPLY_INFO_URL, null));
  }

  @Override
  public void clearQuota(String appid) throws WeiboErrorException {
    JsonObject o = new JsonObject();
    o.addProperty("appid", appid);
    this.post(WeiboMpApiUrl.Other.CLEAR_QUOTA_URL, o.toString());
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
    String uriWithAccessToken = uri + (uri.contains("?") ? "&" : "?") + "access_token=" + accessToken;

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
  public WeiboMpService switchoverTo(String mpId) {
    if (this.configStorageMap.containsKey(mpId)) {
      WeiboMpConfigStorageHolder.set(mpId);
      return this;
    }

    throw new RuntimeException(String.format("无法找到对应【%s】的公众号配置信息，请核实！", mpId));
  }

  @Override
  public boolean switchover(String mpId) {
    if (this.configStorageMap.containsKey(mpId)) {
      WeiboMpConfigStorageHolder.set(mpId);
      return true;
    }

    log.error("无法找到对应【{}】的公众号配置信息，请核实！", mpId);
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
  public WeiboMpMaterialService getMaterialService() {
    return this.materialService;
  }

  @Override
  public WeibMpMenuService getMenuService() {
    return this.menuService;
  }

  @Override
  public WeiboMpUserService getUserService() {
    return this.userService;
  }

  @Override
  public WeiboMpUserTagService getUserTagService() {
    return this.tagService;
  }

  @Override
  public WeibMpQrcodeService getQrcodeService() {
    return this.qrCodeService;
  }

  @Override
  public WeiboDataCubeService getDataCubeService() {
    return this.dataCubeService;
  }

  @Override
  public WeiboMpUserBlacklistService getBlackListService() {
    return this.blackListService;
  }

  @Override
  public WeiboMpSubscribeMsgService getSubscribeMsgService() {
    return this.subscribeMsgService;
  }

  @Override
  public RequestHttp getRequestHttp() {
    return this;
  }

  @Override
  public WeibMpMassMessageService getMassMessageService() {
    return this.massMessageService;
  }

  @Override
  public void setMaterialService(WeiboMpMaterialService materialService) {
    this.materialService = materialService;
  }

  @Override
  public void setMenuService(WeibMpMenuService menuService) {
    this.menuService = menuService;
  }

  @Override
  public void setUserService(WeiboMpUserService userService) {
    this.userService = userService;
  }

  @Override
  public void setTagService(WeiboMpUserTagService tagService) {
    this.tagService = tagService;
  }

  @Override
  public void setQrCodeService(WeibMpQrcodeService qrCodeService) {
    this.qrCodeService = qrCodeService;
  }

  @Override
  public void setDataCubeService(WeiboDataCubeService dataCubeService) {
    this.dataCubeService = dataCubeService;
  }

  @Override
  public void setBlackListService(WeiboMpUserBlacklistService blackListService) {
    this.blackListService = blackListService;
  }

  @Override
  public void setMassMessageService(WeibMpMassMessageService massMessageService) {
    this.massMessageService = massMessageService;
  }

  @Override
  public WeiboImgProcService getImgProcService() {
    return this.imgProcService;
  }

  @Override
  public void setImgProcService(WeiboImgProcService imgProcService) {
    this.imgProcService = imgProcService;
  }
}
