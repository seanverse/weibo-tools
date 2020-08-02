package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.mp.bean.message.WeiboReceiveMessage;

/**
 * 消息匹配器，用在消息路由的时候
 */
public interface WeiboFansMessageMatcher {

  /**
   * 消息是否匹配某种模式
   *
   * @param message
   */
  boolean match(WeiboReceiveMessage message);

}
