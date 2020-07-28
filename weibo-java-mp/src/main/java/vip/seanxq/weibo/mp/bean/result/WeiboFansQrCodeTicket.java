package vip.seanxq.weibo.mp.bean.result;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * 换取二维码的Ticket
 */
@Data
public class WeiboFansQrCodeTicket implements Serializable {
  private static final long serialVersionUID = 5777119669111011584L;

  protected String ticket;
  /**
   * 如果为-1，说明是永久
   */
  protected int expireSeconds = -1; //临时的为1800秒

  public static WeiboFansQrCodeTicket fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboFansQrCodeTicket.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
