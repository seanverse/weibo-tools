package vip.seanxq.weibo.mp.bean.material;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

@Data
public class WeiboMpMaterialVideoInfoResult implements Serializable {
  private static final long serialVersionUID = 1269131745333792202L;

  private String title;
  private String description;
  private String downUrl;

  public static WeiboMpMaterialVideoInfoResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpMaterialVideoInfoResult.class);
  }

}
