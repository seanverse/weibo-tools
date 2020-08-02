package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.bean.message.WeiboReceiveMessage;

import java.util.Map;

/**
 * 微博消息拦截器，可以用来做验证
 *
 * @author Daniel Qian
 */
public interface WeiboFansMessageInterceptor {

  /**
   * 拦截微博消息
   *
   * @param receiveMessage
   * @param context        上下文，如果handler或interceptor之间有信息要传递，可以用这个
   * @param wxMpService
   * @param sessionManager
   * @return true代表OK，false代表不OK
   */
  boolean intercept(WeiboReceiveMessage receiveMessage,
                    Map<String, Object> context,
                    WeiboMpService wxMpService,
                    WeiboSessionManager sessionManager) throws WeiboErrorException;

}
