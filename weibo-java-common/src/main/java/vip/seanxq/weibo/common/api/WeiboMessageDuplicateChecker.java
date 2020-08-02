package vip.seanxq.weibo.common.api;

/**
 * <pre>
 * 消息重复检查器.
 * 微博服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次
 * </pre>
 *
 * @author Daniel Qian
 */
public interface WeiboMessageDuplicateChecker {

  /**
   * 判断消息是否重复.
   * <h2>消息的排重方式</h2>
   * <p>普通消息：若有msgId则以msgId来去重，若没有则以FromUserName + CreateTime.</p>
   * <p>事件消息：关于重试的消息排重，推荐使用FromUserName + CreateTime 排重。weibo表示事件消息没有msgId.
   *     在群发消息后返回的群发执行状态事件消息中有群发消息msgId可以用于去重. </p>
   * @param messageId messageId需要根据上面讲的方式构造
   * @return 如果是重复消息，返回true，否则返回false
   */
  boolean isDuplicate(String messageId);

}
