package vip.seanxq.weibo.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum MsgType {

  MSG_TEXT("text"),
  MSG_ARTICLES("articles"),
  MSG_POSITION("position"),

  //接收消息中增加了图片、语音消息类型
  MSG_IMAGE("image"),
  MSG_VOICE("voice"),

  //事件消息，结合data元素的subtype才能确定具体的操作事件
  MSG_EVENT("event"),

  //被@的事件消息
  MSG_MENTION("mention");

  private  final String msgTypeText;


}
