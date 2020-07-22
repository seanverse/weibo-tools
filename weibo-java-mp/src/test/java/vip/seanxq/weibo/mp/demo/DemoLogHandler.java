package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeibMpMessageHandler;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;

import java.util.Map;

/**
 * Created by qianjia on 15/1/22.
 */
public class DemoLogHandler implements WeibMpMessageHandler {
  @Override
  public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage, Map<String, Object> context, WeiboMpService wxMpService,
                                     WeiboSessionManager sessionManager) {
    System.out.println(wxMessage.toString());
    return null;
  }
}
