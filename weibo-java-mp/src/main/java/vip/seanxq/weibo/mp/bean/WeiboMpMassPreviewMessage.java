package vip.seanxq.weibo.mp.bean;

import lombok.Data;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * @author miller
 */
@Data
public class WeiboMpMassPreviewMessage implements Serializable {
  private static final long serialVersionUID = 9095211638358424020L;

  private String toWxUserName;
  private String toWxUserOpenid;
  /**
   * <pre>
   * 消息类型
   * 请使用
   * {@link WeiboConsts.MassMsgType#IMAGE}
   * {@link WeiboConsts.MassMsgType#MPNEWS}
   * {@link WeiboConsts.MassMsgType#TEXT}
   * {@link WeiboConsts.MassMsgType#MPVIDEO}
   * {@link WeiboConsts.MassMsgType#VOICE}
   * 如果msgtype和media_id不匹配的话，会返回系统繁忙的错误
   * </pre>
   */
  private String msgType;
  private String content;
  private String mediaId;

  public WeiboMpMassPreviewMessage() {
    super();
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
