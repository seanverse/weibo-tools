package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.api.WeiboFansMessageHandler;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutTextMessage;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
public class DemoTextHandler implements WeiboFansMessageHandler {
  @Override
  public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage, Map<String, Object> context,
                                     WeiboMpService wxMpService, WeiboSessionManager sessionManager) {
    WeiboMpXmlOutTextMessage m
      = WeiboMpXmlOutMessage.TEXT().content("测试加密消息").fromUser(wxMessage.getToUser())
      .toUser(wxMessage.getFromUser()).build();
    return m;
  }

}
