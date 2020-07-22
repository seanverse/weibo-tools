package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.bean.WeiboJsapiSignature;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.service.WeiboService;
import vip.seanxq.weibo.common.util.http.MediaUploadRequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.apache.ApacheHttpClientBuilder;
import vip.seanxq.weibo.mp.bean.WeiboMpSemanticQuery;
import vip.seanxq.weibo.mp.bean.result.WeiboMpCurrentAutoReplyInfo;
import vip.seanxq.weibo.mp.bean.result.WeiboMpOAuth2AccessToken;
import vip.seanxq.weibo.mp.bean.result.WeiboMpSemanticQueryResult;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUser;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.common.enums.TicketType;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.util.Map;

/**
 * 微博公众号API的Service.
 *
 * @author chanjarster
 */
public interface WeiboMpService extends WeiboService {
  /**
   * <pre>
   * 验证消息的确来自微博服务器.
   * 详情请见: http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421135319&token=&lang=zh_CN
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
   * 获得ticket,不强制刷新ticket.
   *
   * @param type ticket 类型
   * @return ticket
   * @throws WeiboErrorException .
   * @see #getTicket(TicketType, boolean)
   */
  String getTicket(TicketType type) throws WeiboErrorException;

  /**
   * <pre>
   * 获得ticket.
   * 获得时会检查 Token是否过期，如果过期了，那么就刷新一下，否则就什么都不干
   * </pre>
   *
   * @param forceRefresh 强制刷新
   * @param type         ticket类型
   * @return ticket
   * @throws WeiboErrorException .
   */
  String getTicket(TicketType type, boolean forceRefresh) throws WeiboErrorException;

  /**
   * 获得jsapi_ticket,不强制刷新jsapi_ticket.
   *
   * @return jsapi ticket
   * @throws WeiboErrorException .
   * @see #getJsapiTicket(boolean)
   */
  String getJsapiTicket() throws WeiboErrorException;

  /**
   * <pre>
   * 获得jsapi_ticket.
   * 获得时会检查jsapiToken是否过期，如果过期了，那么就刷新一下，否则就什么都不干
   *
   * 详情请见：http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115&token=&lang=zh_CN
   * </pre>
   *
   * @param forceRefresh 强制刷新
   * @return jsapi ticket
   * @throws WeiboErrorException .
   */
  String getJsapiTicket(boolean forceRefresh) throws WeiboErrorException;

  /**
   * <pre>
   * 创建调用jsapi时所需要的签名.
   *
   * 详情请见：http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141115&token=&lang=zh_CN
   * </pre>
   *
   * @param url 地址
   * @return 生成的签名对象
   * @throws WeiboErrorException .
   */
  WeiboJsapiSignature createJsapiSignature(String url) throws WeiboErrorException;

  /**
   * <pre>
   * 长链接转短链接接口.
   * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=长链接转短链接接口
   * </pre>
   *
   * @param longUrl 长url
   * @return 生成的短地址
   * @throws WeiboErrorException .
   */
  String shortUrl(String longUrl) throws WeiboErrorException;

  /**
   * <pre>
   * 语义查询接口.
   * 详情请见：http://mp.weixin.qq.com/wiki/index.php?title=语义理解
   * </pre>
   *
   * @param semanticQuery 查询条件
   * @return 查询结果
   * @throws WeiboErrorException .
   */
  WeiboMpSemanticQueryResult semanticQuery(WeiboMpSemanticQuery semanticQuery) throws WeiboErrorException;

  /**
   * <pre>
   * 构造第三方使用网站应用授权登录的url.
   * 详情请见: <a href="https://open.weixin.qq.com/cgi-bin/showdocument?action=dir_list&t=resource/res_list&verify=1&id=open1419316505&token=&lang=zh_CN">网站应用微博登录开发指南</a>
   * URL格式为：https://open.weixin.qq.com/connect/qrconnect?appid=APPID&redirect_uri=REDIRECT_URI&response_type=code&scope=SCOPE&state=STATE#wechat_redirect
   * </pre>
   *
   * @param redirectURI 用户授权完成后的重定向链接，无需urlencode, 方法内会进行encode
   * @param scope       应用授权作用域，拥有多个作用域用逗号（,）分隔，网页应用目前仅填写snsapi_login即可
   * @param state       非必填，用于保持请求和回调的状态，授权请求后原样带回给第三方。该参数可用于防止csrf攻击（跨站请求伪造攻击），建议第三方带上该参数，可设置为简单的随机数加session进行校验
   * @return url
   */
  String buildQrConnectUrl(String redirectURI, String scope, String state);

  /**
   * <pre>
   * 构造oauth2授权的url连接.
   * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=网页授权获取用户基本信息
   * </pre>
   *
   * @param redirectURI 用户授权完成后的重定向链接，无需urlencode, 方法内会进行encode
   * @param scope       scope
   * @param state       state
   * @return url
   */
  String oauth2buildAuthorizationUrl(String redirectURI, String scope, String state);

  /**
   * <pre>
   * 用code换取oauth2的access token.
   * 详情请见: http://mp.weixin.qq.com/wiki/index.php?title=网页授权获取用户基本信息
   * </pre>
   *
   * @param code code
   * @return token对象
   * @throws WeiboErrorException .
   */
  WeiboMpOAuth2AccessToken oauth2getAccessToken(String code) throws WeiboErrorException;

  /**
   * <pre>
   * 刷新oauth2的access token.
   * </pre>
   *
   * @param refreshToken 刷新token
   * @return 新的token对象
   * @throws WeiboErrorException .
   */
  WeiboMpOAuth2AccessToken oauth2refreshAccessToken(String refreshToken) throws WeiboErrorException;

  /**
   * <pre>
   * 用oauth2获取用户信息, 当前面引导授权时的scope是snsapi_userinfo的时候才可以.
   * </pre>
   *
   * @param oAuth2AccessToken token对象
   * @param lang              zh_CN, zh_TW, en
   * @return 用户对象
   * @throws WeiboErrorException .
   */
  WeiboMpUser oauth2getUserInfo(WeiboMpOAuth2AccessToken oAuth2AccessToken, String lang) throws WeiboErrorException;

  /**
   * <pre>
   * 验证oauth2的access token是否有效.
   * </pre>
   *
   * @param oAuth2AccessToken token对象
   * @return 是否有效
   */
  boolean oauth2validateAccessToken(WeiboMpOAuth2AccessToken oAuth2AccessToken);

  /**
   * <pre>
   * 获取微博服务器IP地址
   * http://mp.weixin.qq.com/wiki/0/2ad4b6bfd29f30f71d39616c2a0fcedc.html
   * </pre>
   *
   * @return 微博服务器ip地址数组
   * @throws WeiboErrorException .
   */
  String[] getCallbackIP() throws WeiboErrorException;

  /**
   * <pre>
   *  网络检测
   *  https://mp.weixin.qq.com/wiki?t=resource/res_main&id=21541575776DtsuT
   *  为了帮助开发者排查回调连接失败的问题，提供这个网络检测的API。它可以对开发者URL做域名解析，然后对所有IP进行一次ping操作，得到丢包率和耗时。
   * </pre>
   *
   * @param action   执行的检测动作
   * @param operator 指定平台从某个运营商进行检测
   * @return 检测结果
   * @throws WeiboErrorException .
   */
  WeiboNetCheckResult netCheck(String action, String operator) throws WeiboErrorException;

  /**
   * <pre>
   * 获取公众号的自动回复规则.
   * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Getting_Rules_for_Auto_Replies.html
   * 开发者可以通过该接口，获取公众号当前使用的自动回复规则，包括关注后自动回复、消息自动回复（60分钟内触发一次）、关键词自动回复。
   * 请注意：
   * 1、第三方平台开发者可以通过本接口，在旗下公众号将业务授权给你后，立即通过本接口检测公众号的自动回复配置，并通过接口再次给公众号设置好自动回复规则，以提升公众号运营者的业务体验。
   * 2、本接口仅能获取公众号在公众平台官网的自动回复功能中设置的自动回复规则，若公众号自行开发实现自动回复，或通过第三方平台开发者来实现，则无法获取。
   * 3、认证/未认证的服务号/订阅号，以及接口测试号，均拥有该接口权限。
   * 4、从第三方平台的公众号登录授权机制上来说，该接口从属于消息与菜单权限集。
   * 5、本接口中返回的图片/语音/视频为临时素材（临时素材每次获取都不同，3天内有效，通过素材管理-获取临时素材接口来获取这些素材），本接口返回的图文消息为永久素材素材（通过素材管理-获取永久素材接口来获取这些素材）。
   * 接口调用请求说明
   * http请求方式: GET（请使用https协议）
   * https://api.weixin.qq.com/cgi-bin/get_current_autoreply_info?access_token=ACCESS_TOKEN
   * </pre>
   *
   * @return 公众号的自动回复规则
   * @throws WeiboErrorException .
   */
  WeiboMpCurrentAutoReplyInfo getCurrentAutoReplyInfo() throws WeiboErrorException;

  /**
   * <pre>
   *  公众号调用或第三方平台帮公众号调用对公众号的所有api调用（包括第三方帮其调用）次数进行清零：
   *  HTTP调用：https://api.weixin.qq.com/cgi-bin/clear_quota?access_token=ACCESS_TOKEN
   *  接口文档地址：https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1433744592
   *
   * </pre>
   *
   * @param appid 公众号的APPID
   */
  void clearQuota(String appid) throws WeiboErrorException;

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
   *
   * @param postData 请求参数json值
   * @param url      请求接口地址
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String post(WeiboMpApiUrl url, String postData) throws WeiboErrorException;

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
   * 返回素材相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboMpMaterialService
   */
  WeiboMpMaterialService getMaterialService();

  /**
   * 返回菜单相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeibMpMenuService
   */
  WeibMpMenuService getMenuService();

  /**
   * 返回用户相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboMpUserService
   */
  WeiboMpUserService getUserService();

  /**
   * 返回用户标签相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboMpUserTagService
   */
  WeiboMpUserTagService getUserTagService();

  /**
   * 返回二维码相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeibMpQrcodeService
   */
  WeibMpQrcodeService getQrcodeService();

  /**
   * 返回数据分析统计相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboDataCubeService
   */
  WeiboDataCubeService getDataCubeService();

  /**
   * 返回用户黑名单管理相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboMpUserBlacklistService
   */
  WeiboMpUserBlacklistService getBlackListService();

  /**
   * 返回一次性订阅消息相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboMpSubscribeMsgService
   */
  WeiboMpSubscribeMsgService getSubscribeMsgService();

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
   * 返回群发消息相关接口方法的实现类对象，以方便调用其各个接口.
   *
   * @return WeibMpMassMessageService
   */
  WeibMpMassMessageService getMassMessageService();

  /**
   * 返回图像处理接口的实现类对象，以方便调用其各个接口.
   *
   * @return WeiboImgProcService
   */
  WeiboImgProcService getImgProcService();

  /**
   * .
   *
   * @param materialService .
   */
  void setMaterialService(WeiboMpMaterialService materialService);

  /**
   * .
   *
   * @param menuService .
   */
  void setMenuService(WeibMpMenuService menuService);

  /**
   * .
   *
   * @param userService .
   */
  void setUserService(WeiboMpUserService userService);

  /**
   * .
   *
   * @param tagService .
   */
  void setTagService(WeiboMpUserTagService tagService);

  /**
   * .
   *
   * @param qrCodeService .
   */
  void setQrCodeService(WeibMpQrcodeService qrCodeService);

  /**
   * .
   *
   * @param dataCubeService .
   */
  void setDataCubeService(WeiboDataCubeService dataCubeService);

  /**
   * .
   *
   * @param blackListService .
   */
  void setBlackListService(WeiboMpUserBlacklistService blackListService);

  /**
   * .
   *
   * @param massMessageService .
   */
  void setMassMessageService(WeibMpMassMessageService massMessageService);

  /**
   * .
   *
   * @param imgProcService .
   */
  void setImgProcService(WeiboImgProcService imgProcService);

}
