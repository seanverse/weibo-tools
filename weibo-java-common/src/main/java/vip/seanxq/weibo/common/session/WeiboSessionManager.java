package vip.seanxq.weibo.common.session;

/**
 * @author Daniel Qian
 */
public interface WeiboSessionManager {

  /**
   * 获取某个sessionId对应的session,如果sessionId没有对应的session，则新建一个并返回。
   */
  WeiboSession getSession(String sessionId);

  /**
   * 获取某个sessionId对应的session,如果sessionId没有对应的session，若create为true则新建一个，否则返回null。
   */
  WeiboSession getSession(String sessionId, boolean create);


}
