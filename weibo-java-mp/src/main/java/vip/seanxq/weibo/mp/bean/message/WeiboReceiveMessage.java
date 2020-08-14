package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;
import vip.seanxq.weibo.common.enums.MsgType;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * <pre>
 * 接收的消息结构:普通消息和事件消息融合的结构定义
 * 普通消息的示例：
 * <a href="https://open.weibo.com/wiki/接收普通消息">接收普通消息</a>
 * {
 *     "type": "voice",
 *     "receiver_id": 1902538057,
 *     "sender_id": 2489518277,
 *     "created_at": "Mon Jul 16 18:09:20 +0800 2012",
 *     "text": "发了一个语音消息",
 *     "data": {
 *         "vfid": 821804459,    // 发送者用此ID查看语音
 *         "tovfid": 821804469  // 接收者用此ID查看语音
 *     }
 * }
 * 事件消息的示例：
 * <a href="https://open.weibo.com/wiki/接收事件推送">接收事件推送</a>
 * {
 *     "type": "event",
 *     "receiver_id": 1902538057,
 *     "sender_id": 2489518277,
 *     "created_at": "Mon Jul 16 18:09:20 +0800 2012",
 *     "text": "扫描二维码",
 *     "data": {
 *         "subtype": "scan_follow",
 *         "key": "action_name_scene_id",
 *         "ticket": "TICKET",
 *     }
 * }
 */
@Data
public class WeiboReceiveMessage implements Serializable {

  private MsgType type;
  private String receiverId;
  private String senderId;
  private String createdAt;
  private String text;

  //image
  MsgImage image;
  //voice
  private MsgVoice voice;
  //position
  private MsgPosition position;

  //接收事件消息的subtype和datakey等
  private MsgEventData eventData = new MsgEventData(EventSubType.NULL);

  public WeiboReceiveMessage(){}

  public WeiboReceiveMessage(MsgType type) {
    this.type = type;
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return toJson();
  }
}
