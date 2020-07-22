package vip.seanxq.weibo.mp.bean.imgproc;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author Theo Nie
 */
@Data
public class WeiboImgProcAiCropResult implements Serializable {
  private static final long serialVersionUID = -6470673963772979463L;

  @SerializedName("img_size")
  private ImgSize imgSize;
  @SerializedName("results")
  private List<Results> results;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  public static WeiboImgProcAiCropResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboImgProcAiCropResult.class);
  }

  @Data
  public static class ImgSize {
    @SerializedName("w")
    private int w;
    @SerializedName("h")
    private int h;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }
  }

  @Data
  public static class Results {
    @SerializedName("crop_left")
    private int cropLeft;
    @SerializedName("crop_top")
    private int cropTop;
    @SerializedName("crop_right")
    private int cropRight;
    @SerializedName("crop_bottom")
    private int cropBottom;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }
  }
}
