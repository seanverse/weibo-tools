package vip.seanxq.weibo.mp.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import vip.seanxq.weibo.mp.bean.group.WeiboUserGroup;
import vip.seanxq.weibo.mp.bean.message.WeiboKefuMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMessMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboPassiveMessage;
import vip.seanxq.weibo.mp.bean.result.WeiboFansQrCodeTicket;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUser;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUserList;
import vip.seanxq.weibo.mp.bean.result.WeiboMpOAuth2AccessToken;

/**
 * @author someone
 */
public class WbMpGsonBuilder {

  private static final GsonBuilder INSTANCE = new GsonBuilder();

  //也可以使用@JsonAdapter注解标注在实体类上

  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(WeiboMpOAuth2AccessToken.class, new WeiboMpOAuth2AccessTokenAdapter());
    INSTANCE.registerTypeAdapter(WeiboFansUser.class, new WeiboFansUserGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboUserGroup.class, new WeiboUserTagGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboFansUserList.class, new WeiboUserListGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboFansQrCodeTicket.class, new WeiboQrCodeTicketAdapter());
    INSTANCE.registerTypeAdapter(WeiboKefuMessage.class, new WeiboKefuMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboPassiveMessage.class, new WeiboPassiveMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMessMessage.class, new WeiboMessMessageGsonAdapter());
  }

  public static Gson create() {
    return INSTANCE.create();
  }

}
