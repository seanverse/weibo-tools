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
    CONNECT_OAUTH2_AUTHORIZE_URL(OPEN_DEFAULT_HOST_URL, "/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s&connect_redirect=1#wechat_redirect");

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
  enum SendMessage implements WeiboMpApiUrl {
    /**
     * http post: https://m.api.weibo.com/2/messages/reply.json
     */
    MSG_REPLY_KEFU_MESSAGE(FANS_DEFAULT_HOST_URL, "/messages/reply.json"),
    /**
     * 组群发消息.
     * https://m.api.weibo.com/2/messages/sendall.json?access_token=ACCESS_TOKEN
     */
    MSG_SEND_MESS_MESSAGE(FANS_DEFAULT_HOST_URL, "/messages/sendall.json");

    private String prefix;
    private String path;

    @Override
    public String getUrl(WeiboConfigStorage config) {
      return prefix + path;
    }
  }



}
