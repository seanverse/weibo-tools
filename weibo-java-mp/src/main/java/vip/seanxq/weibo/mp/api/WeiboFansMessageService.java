package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.bean.message.MessMessageReuslt;
import vip.seanxq.weibo.mp.bean.message.WeiboKefuMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMessMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboPassiveMessage;

/**
 * 应用发送消息
 */
public interface WeiboFansMessageService {
  /**
   * <pre>
   * 对于每一个POST请求，开发者在响应包（Get）中返回特定JSON结构或者XML结构，对该消息进行响应（现支持回复文本、图文消息）。
   * 微博服务器在五秒内收不到响应会断掉连接，并且重新发起请求，总共重试三次；
   * 关于重试的消息排重，推荐使用FromUserName + CreateTime 排重；   *
   * 假如开发者无法保证在五秒内处理并回复，可以直接回复空串，微博服务器不会对此作任何处理，并且不会发起重试。这种情况下，可以使用客服消息接口进行异步回复。
   * 详情请见: <a href="https://open.weibo.com/wiki/发送被动响应消息">发送被动响应消息</a>
   * http请求方式: post， 消息内容以Json方式
   * </pre>
   */
   Boolean ReplyPassiveMessage(WeiboPassiveMessage message) throws WeiboErrorException;

  /**
   * 当用户主动发消息给认证账号后，微博将会把消息数据推送给开发者，开发者可以调用客服消息接口一定次数。
   * 用户发送消息，关注/订阅事件，点击自定义菜单，扫描二维码事件后，目前修改为开发者在48小时内不限制发送次数；
   * 用户@事件，开发者在48小时内可以回复一次；
   * 说明：指定的认证用户需被授予接收“被@消息”权限，此接口才返回“被@消息”，申请可邮件 jingyi7@staff.weibo.com 。
   * 用户取消关注后，开发者无法回复消息；
   * weibo wiki: <a href="https://open.weibo.com/wiki/发送客服消息">发送客服消息</a>
   * http post: https://m.api.weibo.com/2/messages/reply.json
   *
   */
   Boolean SendKefuMessage(WeiboKefuMessage message) throws WeiboErrorException;

  /**
   * 根据分组进行群发
   * 接口调用请求说明： URL：https://m.api.weibo.com/2/messages/sendall.json?access_token=ACCESS_TOKEN
   * http 方式： Post , json
   */
  MessMessageReuslt SendMessMessageByGroup(WeiboMessMessage message) throws WeiboErrorException;

  /**
   * <pre>
   * 根据UID列表群发
   * 接口调用请求说明： URL：https://m.api.weibo.com/2/messages/sendall.json?access_token=ACCESS_TOKEN HTTP 请求方式：POST
   * POST数据说明：
   * 文本
   * {
   *    "touser":[
   *     "uid1",
   *     "uid2"
   *    ],
   *    "text":{
   *       "content":"CONTENT"  //文本内容
   *    },
   *     "msgtype":"text"
   * }
   * </pre>
   */
  MessMessageReuslt SendMessMesageByUID(WeiboMessMessage message) throws WeiboErrorException;

}
