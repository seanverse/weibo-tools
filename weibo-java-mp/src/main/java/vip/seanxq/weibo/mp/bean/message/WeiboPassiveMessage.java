package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;
import vip.seanxq.weibo.common.enums.MsgType;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 发送被动响应的消息结构
 */
@Data
public class WeiboPassiveMessage implements Serializable {
  private static final long serialVersionUID = -477699086954365246L;
  /**
   * JSONObject jo = new JSONObject();
   * jo.put("result",true);
   * jo.put("sender_id", senderId);
   * jo.put("receiver_id", receiverId);
   * jo.put("type", type);
   * try {
   * jo.put("data", URLEncoder.encode(data, "utf-8")); //data字段的内容需要进行utf8的urlencode
   * } catch (Exception e) {
   * // TODO Auto-generated catch block
   * e.printStackTrace();
   * }
   * return jo.toString();
   */
  private String result;
  private String sender_id;
  private String receiver_id;
  private MsgType type;
  //text
  private String text;
  //articles
  private List<MsgArticle> articles = new ArrayList<>();
  //position
  private MsgPosition pos;

  public WeiboPassiveMessage(MsgType type) {
    this.type = type;
  }

  /**
   * 获得文本消息
   */
  public static WeiboPassiveMessage TEXT() {
    WeiboPassiveMessage item = new WeiboPassiveMessage(MsgType.MSG_TEXT);
    item.setResult("true");
    return item;
  }

  /**
   * 获得文本消息
   */
  public static WeiboPassiveMessage TEXT(String senderId, String receiverId, String content) {
    WeiboPassiveMessage item = TEXT();
    item.setReceiver_id(receiverId);
    item.setSender_id(senderId);
    item.setText(content);
    return item;
  }

  /**
   * 获得文本消息
   */
  public static WeiboPassiveMessage Article() {
    WeiboPassiveMessage item = new WeiboPassiveMessage(MsgType.MSG_ARTICLES);
    item.setResult("true");
    return item;
  }

  /**
   * 获得文本消息
   */
  public static WeiboPassiveMessage Article(String senderId, String receiverId, MsgArticle[] Articles) {
    WeiboPassiveMessage item = Article();
    item.setReceiver_id(receiverId);
    item.setSender_id(senderId);
    item.setArticles(new ArrayList<MsgArticle>(Arrays.asList(Articles)));
    return item;
  }

  /**
   * 获得文本消息
   */
  public static WeiboPassiveMessage Position() {
    WeiboPassiveMessage item = new WeiboPassiveMessage(MsgType.MSG_POSITION);
    item.setResult("true");
    return item;
  }

  /**
   * 获得文本消息
   */
  public static WeiboPassiveMessage Position(String senderId, String receiverId, MsgPosition pos) {
    WeiboPassiveMessage item = Position();
    item.setReceiver_id(receiverId);
    item.setSender_id(senderId);
    item.setPos(pos);
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
