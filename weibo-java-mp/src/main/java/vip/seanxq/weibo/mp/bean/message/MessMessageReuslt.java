package vip.seanxq.weibo.mp.bean.message;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * 分组群发消息的发送结果返回
 */
@Data
public class MessMessageReuslt implements Serializable {
  private Boolean result;
  @SerializedName("msg_id")
  private String msgId;

  public static MessMessageReuslt fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, MessMessageReuslt.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
