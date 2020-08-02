package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;

/**
 * 接收事件推送消息的数据项，通过subtype来识别类型
 */
@Data
public class MsgEventData {
  private EventSubType subType;
  /**
   * subbtype: EVENT_FOLLOW/UNFOLLOW 无datakey
   * subtype: EVENT_SUBSCRIBE/UNSUBSCRIBE 无datakey
   * subtype: EVENT_SCAN/SCANFOLLOW data:key 事件KEY值，格式为action_name_scene_id，也就是说以action_name为前缀，后面为二维码的scene_id；
   * subtype: EVENT_MENTION_STATUS/COMMENT data:key 当subtype为status时为微博ID，为comment时为评论ID
   * subtype: EVENT_MENU_CLICK data:key 被点击的自定义菜单的key值
   * subtype: EVENT_MENU_VIEW  data:key 该按钮设置的url
   */
  private String dataKey="";

  /**
   * 	EVENT_SCAN/SCANFOLLOW时才有用，二维码的ticket，可用来换取二维码图片。
   */
  private String dataTicket="";

  public MsgEventData(EventSubType subType)
  {
    this.setSubType(subType);
  }

}
