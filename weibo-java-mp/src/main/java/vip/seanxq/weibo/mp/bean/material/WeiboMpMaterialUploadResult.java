package vip.seanxq.weibo.mp.bean.material;

import java.io.Serializable;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

@Data
public class WeiboMpMaterialUploadResult implements Serializable {
  private static final long serialVersionUID = -128818731449449537L;
  private String mediaId;
  private String url;
  private Integer errCode;
  private String errMsg;

  public static WeiboMpMaterialUploadResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpMaterialUploadResult.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}

