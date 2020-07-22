package vip.seanxq.weibo.mp.bean.imgproc;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * 图片高清化返回结果
 * @author Theo Nie
 */
@Data
public class WeiboImgProcSuperResolutionResult implements Serializable {

  private static final long serialVersionUID = 8007440280170407021L;
  @SerializedName("media_id")
  private String mediaId;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  public static WeiboImgProcSuperResolutionResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboImgProcSuperResolutionResult.class);
  }
}
