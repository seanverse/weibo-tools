package vip.seanxq.weibo.mp.enums;

import lombok.AllArgsConstructor;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;

/**
 * weibo api url
 */
public interface WeiboMpApiUrl {
  public static final String API_DEFAULT_HOST_URL = "https://api.weibo.com/2";
  public static final String FANS_DEFAULT_HOST_URL = "https://m.api.weibo.com/2";
  public static final String OPEN_DEFAULT_HOST_URL = "https://open.weibo.com";

  /**
   * 得到api完整地址.
   *
   * @param config 微博公众号配置
   * @return api地址
   */
  String getUrl(WeiboConfigStorage config);

  @AllArgsConstructor
  enum Other implements WeiboMpApiUrl {
    SET_DATA_FORMAT_URL(API_DEFAULT_HOST_URL, "/eps/push/set_format.json"),

    /**
     * 获取access_token.
     */
    GET_ACCESS_TOKEN_URL(API_DEFAULT_HOST_URL, "/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s"),
    /**
     * 获得各种类型的ticket.
     */
    GET_TICKET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/ticket/getticket?type="),
    /**
     * 长链接转短链接接口.
     */
    SHORTURL_API_URL(API_DEFAULT_HOST_URL, "/cgi-bin/shorturl"),
    /**
     * 语义查询接口.
     */
    SEMANTIC_SEMPROXY_SEARCH_URL(API_DEFAULT_HOST_URL, "/semantic/semproxy/search"),
    /**
     * 用code换取oauth2的access token.
     */
    OAUTH2_ACCESS_TOKEN_URL(API_DEFAULT_HOST_URL, "/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code"),
    /**
     * 刷新oauth2的access token.
     */
    OAUTH2_REFRESH_TOKEN_URL(API_DEFAULT_HOST_URL, "/sns/oauth2/refresh_token?appid=%s&grant_type=refresh_token&refresh_token=%s"),
    /**
     * 用oauth2获取用户信息.
     */
    OAUTH2_USERINFO_URL(API_DEFAULT_HOST_URL, "/sns/userinfo?access_token=%s&openid=%s&lang=%s"),
    /**
     * 验证oauth2的access token是否有效.
     */
    OAUTH2_VALIDATE_TOKEN_URL(API_DEFAULT_HOST_URL, "/sns/auth?access_token=%s&openid=%s"),
    /**
     * 获取微博服务器IP地址.
     */
    GET_CALLBACK_IP_URL(API_DEFAULT_HOST_URL, "/cgi-bin/getcallbackip"),
    /**
     * 网络检测.
     */
    NETCHECK_URL(API_DEFAULT_HOST_URL, "/cgi-bin/callback/check"),
    /**
     * 第三方使用网站应用授权登录的url.
     */
    QRCONNECT_URL(OPEN_DEFAULT_HOST_URL, "/connect/qrconnect?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect"),
    /**
     * oauth2授权的url连接.
     */
    CONNECT_OAUTH2_AUTHORIZE_URL(OPEN_DEFAULT_HOST_URL, "/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&connect_redirect=1#wechat_redirect"),
    /**
     * 获取公众号的自动回复规则.
     */
    GET_CURRENT_AUTOREPLY_INFO_URL(API_DEFAULT_HOST_URL, "/cgi-bin/get_current_autoreply_info"),
    /**
     * 公众号调用或第三方平台帮公众号调用对公众号的所有api调用（包括第三方帮其调用）次数进行清零.
     */
    CLEAR_QUOTA_URL(API_DEFAULT_HOST_URL, "/cgi-bin/clear_quota");

    private String prefix;
    private String path;


    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }

  @AllArgsConstructor
  enum Menu implements WeiboMpApiUrl {
    /**
     * get.
     */
    MENU_GET(FANS_DEFAULT_HOST_URL, "/messages/menu/show.json"),
    /**
     * delete.
     */
    MENU_DELETE(FANS_DEFAULT_HOST_URL, "/messages/menu/delete.json"),
    /**
     * create.
     */
    MENU_CREATE(FANS_DEFAULT_HOST_URL, "/messages/menu/create.json");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }


  @AllArgsConstructor
  enum Qrcode implements WeiboMpApiUrl {
    /**
     * create.
     */
    QRCODE_CREATE(API_DEFAULT_HOST_URL, "/eps/qrcode/create.json"),
    /**
     * showqrcode.
     */
    SHOW_QRCODE(FANS_DEFAULT_HOST_URL, "/eps/qrcode/show");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return  prefix + path;
    }
  }

  @AllArgsConstructor
  enum SubscribeMsg implements WeiboMpApiUrl {
    /**
     * subscribemsg.
     */
    SUBSCRIBE_MESSAGE_AUTHORIZE_URL(FANS_DEFAULT_HOST_URL, "/mp/subscribemsg?action=get_confirm&appid=%s&scene=%d&template_id=%s&redirect_url=%s&reserved=%s#wechat_redirect"),
    /**
     * subscribe.
     */
    SEND_MESSAGE_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/template/subscribe");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }

  @AllArgsConstructor
  enum UserBlacklist implements WeiboMpApiUrl {
    /**
     * getblacklist.
     */
    GETBLACKLIST(API_DEFAULT_HOST_URL, "/cgi-bin/tags/members/getblacklist"),
    /**
     * batchblacklist.
     */
    BATCHBLACKLIST(API_DEFAULT_HOST_URL, "/cgi-bin/tags/members/batchblacklist"),
    /**
     * batchunblacklist.
     */
    BATCHUNBLACKLIST(API_DEFAULT_HOST_URL, "/cgi-bin/tags/members/batchunblacklist");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }

  @AllArgsConstructor
  enum FansUser implements WeiboMpApiUrl {
    /**
     * create.
     */
    TAGS_CREATE(FANS_DEFAULT_HOST_URL, "/messages/custom_rule/create.json"),
    /**
     * 获取自定义规则分组列表
     */
    TAGS_GET(FANS_DEFAULT_HOST_URL, "/messages/custom_rule/get.json"),
    /**
     * update.
     */
    TAGS_UPDATE(FANS_DEFAULT_HOST_URL, "/messages/custom_rule/update.json"),
    /**
     * delete.
     */
    TAGS_DELETE(FANS_DEFAULT_HOST_URL, "/messages/custom_rule/delete.json"),

    /**
     * 查询用户所在的分组
     */
    TAGS_USERTAGLIST(FANS_DEFAULT_HOST_URL, "/messages/custom_rule/getid.json"),

    /**
     * 更新用户所在的分组
     */
    TAGS_USERTAGUPDATE(FANS_DEFAULT_HOST_URL, "/messages/custom_rule/member/update.json"),

    /**
     * 通过uid取得用户基本信息 get
     * https://api.weibo.com/2/eps/user/info.json
     */
    USER_GETUSERINFO(API_DEFAULT_HOST_URL, "/eps/user/info.json"),

    /**
     * 通过uid取得用户基本信息
     * https://m.api.weibo.com/2/messages/subscribers/get.json
     */
    USER_GETSUBSCRIBERS(FANS_DEFAULT_HOST_URL, "/messages/subscribers/get.json");


    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }

  @AllArgsConstructor
  enum MassMessage implements WeiboMpApiUrl {
    /**
     * 上传群发用的图文消息.
     */
    MEDIA_UPLOAD_NEWS_URL(API_DEFAULT_HOST_URL, "/cgi-bin/media/uploadnews"),
    /**
     * 上传群发用的视频.
     */
    MEDIA_UPLOAD_VIDEO_URL(API_DEFAULT_HOST_URL, "/cgi-bin/media/uploadvideo"),
    /**
     * 分组群发消息.
     */
    MESSAGE_MASS_SENDALL_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/sendall"),
    /**
     * 按openId列表群发消息.
     */
    MESSAGE_MASS_SEND_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/send"),
    /**
     * 群发消息预览接口.
     */
    MESSAGE_MASS_PREVIEW_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/preview"),
    /**
     * 删除群发接口.
     */
    MESSAGE_MASS_DELETE_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/delete"),


    /**
     * 获取群发速度.
     */
    MESSAGE_MASS_SPEED_GET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/speed/get"),


    /**
     * 设置群发速度.
     */
    MESSAGE_MASS_SPEED_SET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/speed/set"),


    /**
     * 查询群发消息发送状态【订阅号与服务号认证后均可用】
     */
    MESSAGE_MASS_GET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/message/mass/get");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }

  @AllArgsConstructor
  enum Material implements WeiboMpApiUrl {
    /**
     * get.
     */
    MEDIA_GET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/media/get"),
    /**
     * jssdk media get.
     */
    JSSDK_MEDIA_GET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/media/get/jssdk"),
    /**
     * upload.
     */
    MEDIA_UPLOAD_URL(API_DEFAULT_HOST_URL, "/cgi-bin/media/upload?type=%s"),
    /**
     * uploadimg.
     */
    IMG_UPLOAD_URL(API_DEFAULT_HOST_URL, "/cgi-bin/media/uploadimg"),
    /**
     * add_material.
     */
    MATERIAL_ADD_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/add_material?type=%s"),
    /**
     * add_news.
     */
    NEWS_ADD_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/add_news"),
    /**
     * get_material.
     */
    MATERIAL_GET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/get_material"),
    /**
     * update_news.
     */
    NEWS_UPDATE_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/update_news"),
    /**
     * del_material.
     */
    MATERIAL_DEL_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/del_material"),
    /**
     * get_materialcount.
     */
    MATERIAL_GET_COUNT_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/get_materialcount"),
    /**
     * batchget_material.
     */
    MATERIAL_BATCHGET_URL(API_DEFAULT_HOST_URL, "/cgi-bin/material/batchget_material");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }


  @AllArgsConstructor
  enum ImgProc implements WeiboMpApiUrl {
    /**
     * 二维码/条码识别
     */
    QRCODE(API_DEFAULT_HOST_URL, "/cv/img/qrcode?img_url=%s"),

    /**
     * 二维码/条码识别(文件)
     */
    FILE_QRCODE(API_DEFAULT_HOST_URL, "/cv/img/qrcode"),

    /**
     * 图片高清化
     */
    SUPER_RESOLUTION(API_DEFAULT_HOST_URL, "/cv/img/superresolution?img_url=%s"),

    /**
     * 图片高清化(文件)
     */
    FILE_SUPER_RESOLUTION(API_DEFAULT_HOST_URL, "/cv/img/superresolution"),

    /**
     * 图片智能裁剪
     */
    AI_CROP(API_DEFAULT_HOST_URL, "/cv/img/aicrop?img_url=%s&ratios=%s"),

    /**
     * 图片智能裁剪(文件)
     */
    FILE_AI_CROP(API_DEFAULT_HOST_URL, "/cv/img/aicrop?ratios=%s");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }


}
