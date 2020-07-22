package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.api.WeibMpMessageHandler;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
public class DemoOAuth2Handler implements WeibMpMessageHandler {
  @Override
  public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage,
                                     Map<String, Object> context, WeiboMpService wxMpService,
                                     WeiboSessionManager sessionManager) {
    String href = "<a href=\"" + wxMpService.oauth2buildAuthorizationUrl(
      wxMpService.getWxMpConfigStorage().getOauth2redirectUri(),
      WeiboConsts.OAuth2Scope.SNSAPI_USERINFO, null) + "\">测试oauth2</a>";
    return WeiboMpXmlOutMessage.TEXT().content(href)
      .fromUser(wxMessage.getToUser()).toUser(wxMessage.getFromUser())
      .build();
  }
}
