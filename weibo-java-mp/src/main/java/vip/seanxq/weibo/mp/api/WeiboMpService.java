package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.bean.WeiboJsapiSignature;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;
import vip.seanxq.weibo.common.enums.WbMessageFormat;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.service.WeiboService;
import vip.seanxq.weibo.common.util.http.MediaUploadRequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.apache.ApacheHttpClientBuilder;
import vip.seanxq.weibo.mp.bean.result.WeiboMpCurrentAutoReplyInfo;
import vip.seanxq.weibo.mp.bean.result.WeiboMpOAuth2AccessToken;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUser;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.common.enums.TicketType;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.util.Map;

/**
 * 微博公众号API的Service.
 */
public interface WeiboMpService extends WeiboService {

  /**
   * 微博消息推送服务完全兼容“微信XML格式”，以方便基于微信公众平台做了开发的第三方能够更为顺畅得迁移；
   * 第三方可以通过http://open.weibo.com/wiki/Eps/push/set_format 接口来选择自己需要的格式是XML还是JSON
   * @param format xml/json
   * @return 设定执行成功状态
   */
  boolean setDataFormat(WbMessageFormat format) throws WeiboErrorException;

  /**
   * <pre>
   * 验证消息的确来自微博服务器.
   * </pre>
   *
   * @param nonce     随机串
   * @param signature 签名
   * @param timestamp 时间戳
   * @return 是否验证通过
   */
  boolean checkSignature(String timestamp, String nonce, String signature);

  /**
   * 获取access_token, 不强制刷新access_token.
   *
   * @return token
   * @throws WeiboErrorException .
   * @see #getAccessToken(boolean)
   */
  String getAccessToken() throws WeiboErrorException;

  /**
   * <pre>
   * 获取access_token，本方法线程安全.
   * 且在多线程同时刷新时只刷新一次，避免超出2000次/日的调用次数上限
   *
   * 另：本service的所有方法都会在access_token过期时调用此方法
   *
   * 程序员在非必要情况下尽量不要主动调用此方法
   *
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183&token=&lang=zh_CN
   * </pre>
   *
   * @param forceRefresh 是否强制刷新
   * @return token
   * @throws WeiboErrorException .
   */
  String getAccessToken(boolean forceRefresh) throws WeiboErrorException;

  /**
   * <pre>
   * Service没有实现某个API的时候，可以用这个，
   * 比{@link #get}和{@link #post}方法更灵活，可以自己构造RequestExecutor用来处理不同的参数和不同的返回类型。
   * 可以参考，{@link MediaUploadRequestExecutor}的实现方法
   * </pre>
   *
   * @param data     参数数据
   * @param executor 执行器
   * @param url      接口地址
   * @return 结果
   * @throws WeiboErrorException 异常
   */
  <T, E> T execute(RequestExecutor<T, E> executor, String url, E data) throws WeiboErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有微博API中的GET请求.
   *
   * @param queryParam 参数
   * @param url        请求接口地址
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String get(WeiboMpApiUrl url, String queryParam) throws WeiboErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有微博API中的POST请求.
   * 大部分的API是接收application/x-www-form-urlencoded的数据
   * @param postData 请求参数值
   * @param url      请求接口地址
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String post(WeiboMpApiUrl url, String postData) throws WeiboErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有微博API中的POST请求.
   * 大部分的API是接收application/x-www-form-urlencoded的数据
   * @param postData 请求参数值
   * @param url      请求接口地址
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String post(String url, String postData) throws WeiboErrorException;

  /**
   * <pre>
   * Service没有实现某个API的时候，可以用这个，
   * 比{@link #get}和{@link #post}方法更灵活，可以自己构造RequestExecutor用来处理不同的参数和不同的返回类型。
   * 可以参考，{@link MediaUploadRequestExecutor}的实现方法
   * </pre>
   *
   * @param data     参数数据
   * @param executor 执行器
   * @param url      接口地址
   * @return 结果
   * @throws WeiboErrorException 异常
   */
  <T, E> T execute(RequestExecutor<T, E> executor, WeiboMpApiUrl url, E data) throws WeiboErrorException;

  /**
   * 设置当微博系统响应系统繁忙时，要等待多少 retrySleepMillis(ms) * 2^(重试次数 - 1) 再发起重试.
   *
   * @param retrySleepMillis 默认：1000ms
   */
  void setRetrySleepMillis(int retrySleepMillis);

  /**
   * <pre>
   * 设置当微博系统响应系统繁忙时，最大重试次数.
   * 默认：5次
   * </pre>
   *
   * @param maxRetryTimes 最大重试次数
   */
  void setMaxRetryTimes(int maxRetryTimes);

  /**
   * 获取WxMpConfigStorage 对象.
   *
   * @return WeiboConfigStorage
   */
  WeiboConfigStorage getWxMpConfigStorage();

  /**
   * 设置 {@link WeiboConfigStorage} 的实现. 兼容老版本
   *
   * @param wxConfigProvider .
   */
  void setWxMpConfigStorage(WeiboConfigStorage wxConfigProvider);

  /**
   * Map里 加入新的 {@link WeiboConfigStorage}，适用于动态添加新的微博公众号配置.
   *
   * @param mpId          公众号id
   * @param configStorage 新的微博配置
   */
  void addConfigStorage(String mpId, WeiboConfigStorage configStorage);

  /**
   * 从 Map中 移除 {@link String mpId} 所对应的 {@link WeiboConfigStorage}，适用于动态移除微博公众号配置.
   *
   * @param mpId 对应公众号的标识
   */
  void removeConfigStorage(String mpId);

  /**
   * 注入多个 {@link WeiboConfigStorage} 的实现. 并为每个 {@link WeiboConfigStorage} 赋予不同的 {@link String mpId} 值
   * 随机采用一个{@link String mpId}进行Http初始化操作
   *
   * @param configStorages WeiboConfigStorage map
   */
  void setMultiConfigStorages(Map<String, WeiboConfigStorage> configStorages);

  /**
   * 注入多个 {@link WeiboConfigStorage} 的实现. 并为每个 {@link WeiboConfigStorage} 赋予不同的 {@link String label} 值
   *
   * @param configStorages WeiboConfigStorage map
   * @param defaultMpId    设置一个{@link WeiboConfigStorage} 所对应的{@link String mpId}进行Http初始化
   */
  void setMultiConfigStorages(Map<String, WeiboConfigStorage> configStorages, String defaultMpId);

  /**
   * 进行相应的公众号切换.
   *
   * @param mpId 公众号标识
   * @return 切换是否成功
   */
  boolean switchover(String mpId);

  /**
   * 进行相应的公众号切换.
   *
   * @param mpId 公众号标识
   * @return 切换成功，则返回当前对象，方便链式调用，否则抛出异常
   */
  WeiboMpService switchoverTo(String mpId);


  /**
   * 返回菜单相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeibCustomMenuService
   */
  WeibCustomMenuService getMenuService();

  /**
   * 返回用户相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboFansUserService
   */
  WeiboFansUserService getUserService();

  /**
   * 返回用户标签相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboFansGroupService
   */
  WeiboFansGroupService getUserTagService();

  /**
   * 返回二维码相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboFansQrcodeService
   */
  WeiboFansQrcodeService getQrcodeService();

  /**
   * 发送消息相关的Service
   */
  WeiboFansMessageService getMessageService();

  /**
   * 初始化httpclient的builer实例，即设置 {@link ApacheHttpClientBuilder}
   */
  void initHttp();

  /**
   * 获取RequestHttp对象.
   *
   * @return RequestHttp对象
   */
  RequestHttp getRequestHttp();




  /**
   * .
   *
   * @param menuService .
   */
  void setMenuService(WeibCustomMenuService menuService);

  /**
   * .
   *
   * @param userService .
   */
  void setUserService(WeiboFansUserService userService);

  /**
   * .
   *
   * @param userGroupService .
   */
  void setUserGroupService(WeiboFansGroupService userGroupService);

  /**
   * .
   *
   * @param qrCodeService .
   */
  void setQrCodeService(WeiboFansQrcodeService qrCodeService);

  /**
   * .
   * @param messageService
   */
  void setMessageService(WeiboFansMessageService messageService);

}
