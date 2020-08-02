package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.enums.MsgType;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.message.*;

import java.lang.reflect.Type;


@Slf4j
public class WeiboReceiveMessageGsonAdapter implements JsonDeserializer<WeiboReceiveMessage> {

  /**
   * <pre>
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
   * <pre>
   */
  @Override
  public WeiboReceiveMessage deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboReceiveMessage message = new WeiboReceiveMessage();
    JsonObject jsonObject = json.getAsJsonObject();
    message.setType(MsgType.valueOf(jsonObject.get("type").getAsString()));
    message.setSenderId(jsonObject.get("sender_id").getAsString());
    message.setReceiverId(jsonObject.get("receiver_id").getAsString());
    message.setCreatedAt(jsonObject.get("created_at").getAsString());
    message.setText(jsonObject.get("text").getAsString());

    //data分不同的情况
    JsonObject data = jsonObject.get("data").getAsJsonObject();
    switch (message.getType())
    {
      case MSG_TEXT:
        //data内无内容
        break;
      case MSG_IMAGE:
        MsgImage img = new MsgImage();
        img.setVfid(data.get("vfid").getAsString());
        img.setTovfid(data.get("tovfid").getAsString());
        message.setImage(img);
        break;
      case MSG_VOICE:
        MsgVoice voice = new MsgVoice();
        voice.setVfid(data.get("vfid").getAsString());
        voice.setTovfid(data.get("tovfid").getAsString());
        message.setVoice(voice);
        break;
      case MSG_POSITION:
        MsgPosition pos = new MsgPosition();
        pos.setLongitude(data.get("longitude").getAsString());
        pos.setLatitude(data.get("latitude").getAsString());
        break;
      case MSG_EVENT:
      case MSG_MENTION: {
        MsgEventData eventData = message.getEventData();
        EventSubType subType = EventSubType.valueOf(data.get("subtype").getAsString());
        if (eventData == null) { eventData = new MsgEventData(subType);message.setEventData(eventData);}
        eventData.setSubType(subType);
        switch (subType)
        {
          case EVENT_FOLLOW:
          case EVENT_UNFOLLOW:
          case EVENT_SUBSCRIBE:
          case EVENT_UNSUBSCRIBE:
            eventData.setDataKey("");
            break;
          //scan和scan_follow为二维码扫描事件,扫码并关注事件
          case  EVENT_SCAN:
          case EVENT_SCANFOLLOW: {
            eventData.setDataKey(data.get("key").getAsString());
            eventData.setDataTicket(data.get("ticket").getAsString());
            break;
          }
            //	status：@的微博，comment：@的评论
          case EVENT_MENTION_STATUS:
            //	status：@的微博，comment：@的评论
          case EVENT_MENTION_COMMENT:
            //		click, 自定义菜单点击事件
            case EVENT_MENU_CLICK:
            //	自定义菜单跳转链接事件消息
          case EVENT_MENU_VIEW:
          {
            eventData.setDataKey(data.get("key").getAsString());
            break;
          }
          default:
            break;
        } //end sub switch
        break;
      }
      default:
        break;
    }
    return message;
  }


}
