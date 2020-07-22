package vip.seanxq.weibo.mp.bean.material;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * @author miller
 */
@Data
public class WeiboMediaImgUploadResult implements Serializable {
  private static final long serialVersionUID = 1996392453428768829L;
  private String url;

  public static WeiboMediaImgUploadResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMediaImgUploadResult.class);
  }

}
