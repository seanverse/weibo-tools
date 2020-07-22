package vip.seanxq.weibo.mp.bean.result;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * <pre>
 * 获取群发速度
 * https://developers.weixin.qq.com/doc/offiaccount/Message_Management/Batch_Sends_and_Originality_Checks.html#9
 * speed	realspeed
 * 0	80w/分钟
 * 1	60w/分钟
 * 2	45w/分钟
 * 3	30w/分钟
 * 4	10w/分钟
 * </pre>
 */
@Data
public class WeiboMpMassSpeedGetResult implements Serializable {

  private static final long serialVersionUID = -6478157575168068334L;

  private Integer speed;

  private Integer realspeed;

  public static WeiboMpMassSpeedGetResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpMassSpeedGetResult.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}
