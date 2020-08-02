package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.bean.message.WeiboReceiveMessage;

import java.util.Map;

/**
 * 处理微博推送消息的处理器接口.
 *
 * @author Daniel Qian
 */
public interface WeiboFansMessageHandler {

  /**
   * 处理微博推送消息.
   *
   * @param receiveMessage WeiboReceiveMessage      微博推送消息
   * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
   * @param wxMpService    服务类
   * @param sessionManager session管理器
   * @return xml格式的消息，如果在异步规则里处理的话，可以返回null
   * @throws WeiboErrorException 异常
   */
  WeiboReceiveMessage handle(WeiboReceiveMessage receiveMessage,
                             Map<String, Object> context,
                             WeiboMpService wxMpService,
                             WeiboSessionManager sessionManager) throws WeiboErrorException;

}
