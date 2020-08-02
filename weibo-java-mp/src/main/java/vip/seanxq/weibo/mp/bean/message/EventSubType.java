package vip.seanxq.weibo.mp.bean.message;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

  @Getter
  @RequiredArgsConstructor
  public enum EventSubType {
    NULL("null"),
    //关注
    EVENT_FOLLOW("follow"),
    //取消关注
    EVENT_UNFOLLOW("unfollow"),
    //订阅
    EVENT_SUBSCRIBE("subscribe"),
    //取消订阅
    EVENT_UNSUBSCRIBE("unsubscribe"),
    //scan和scan_follow为二维码扫描事件。
    EVENT_SCAN("scan"),
    //扫码并关注事件
    EVENT_SCANFOLLOW("scan_follow"),
    //	status：@的微博，comment：@的评论
    EVENT_MENTION_STATUS("status"),
    //	status：@的微博，comment：@的评论
    EVENT_MENTION_COMMENT("comment"),

    //		click, 自定义菜单点击事件
    EVENT_MENU_CLICK("click"),
    //	自定义菜单跳转链接事件消息
    EVENT_MENU_VIEW("view");

    private final String msgTypeText;
  }
