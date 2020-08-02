package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;
import vip.seanxq.weibo.common.enums.MsgType;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class WeiboMessMessage implements Serializable {
  private MsgType type;
  /**
   * group / user
   */
  private boolean filterByGroup;
  private String groupId;
  private String[] uidList;

  //text 要回复的私信文本内容。文本大小必须小于300个汉字。
  private String text;
  //articles
  private List<MsgArticle> articles = new ArrayList<>();

  public WeiboMessMessage(MsgType type, boolean filterByGroup) {
    this.type = type;
    this.filterByGroup = filterByGroup;
  }

  /**
   * 获得文本消息
   */
  public static WeiboMessMessage TextByUser(String[] uidList,  String text) {
    WeiboMessMessage item =  new WeiboMessMessage(MsgType.MSG_TEXT, false);
    item.setUidList(uidList);
    item.setText(text);
    return item;
  }
  /**
   * 获得图文消息
   */
  public static WeiboMessMessage TextByGroup(String groupId, String text) {
    WeiboMessMessage item =  new WeiboMessMessage(MsgType.MSG_TEXT, true);
    item.setGroupId(groupId);
    item.setText(text);
    return item;
  }

  /**
   * 获得图文消息
   */
  public static WeiboMessMessage ArticleByUser(String[] uidList, MsgArticle[] Articles) {
    WeiboMessMessage item =  new WeiboMessMessage(MsgType.MSG_ARTICLES, false);
    item.setUidList(uidList);
    //item.setSender_id(senderId);
    item.setArticles(new ArrayList<MsgArticle>(Arrays.asList(Articles)));
    return item;
  }

  /**
   * 获得图文消息
   */
  public static WeiboMessMessage ArticleByGroup(String groupId, MsgArticle[] Articles) {
    WeiboMessMessage item =  new WeiboMessMessage(MsgType.MSG_ARTICLES, true);
    item.setGroupId(groupId);
    item.setArticles(new ArrayList<MsgArticle>(Arrays.asList(Articles)));
    return item;
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return toJson();
  }
}
