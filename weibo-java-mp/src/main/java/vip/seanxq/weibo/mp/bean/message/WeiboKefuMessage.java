package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;
import vip.seanxq.weibo.common.enums.MsgType;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 发送客服消息的消息结构
 */
@Data
public class WeiboKefuMessage implements Serializable {
  private static final long serialVersionUID = -47242386954365246L;

  private MsgType type;
  private String receiver_id;
  /**
   * 	取值为0或1，不填则默认为1。取值为1时，通过本接口发送的消息会进入发送方的私信箱；
   * 	取值为0时，通过本接口发送的消息不会进入发送方的私信箱。
   */
  private int save_sender_box;
  //text 要回复的私信文本内容。文本大小必须小于300个汉字。
  private String text;
  //articles
  private List<MsgArticle> articles = new ArrayList<>();
  //position
  private MsgPosition pos;

  public WeiboKefuMessage(MsgType type) {
    this.type = type;
  }

  /**
   * 获得图文消息
   */
  public static WeiboKefuMessage TEXT(String receiverId, String content) {
    WeiboKefuMessage item = new WeiboKefuMessage(MsgType.MSG_TEXT);
    item.setReceiver_id(receiverId);
    item.setText(content);
    return item;
  }

  /**
   * 获得文本消息
   */
  public static WeiboKefuMessage Article() {
    WeiboKefuMessage item = new WeiboKefuMessage(MsgType.MSG_ARTICLES);
    //item.setResult("true");
    return item;
  }

  /**
   * 获得图文消息
   */
  public static WeiboKefuMessage Article(String receiverId, MsgArticle[] Articles) {
    WeiboKefuMessage item = Article();
    item.setReceiver_id(receiverId);
    item.setArticles(new ArrayList<MsgArticle>(Arrays.asList(Articles)));
    return item;
  }

  /**
   * 获得Position消息
   */
  public static WeiboKefuMessage Position(String receiverId, MsgPosition pos) {
    WeiboKefuMessage item = new WeiboKefuMessage(MsgType.MSG_POSITION);
    item.setReceiver_id(receiverId);
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





