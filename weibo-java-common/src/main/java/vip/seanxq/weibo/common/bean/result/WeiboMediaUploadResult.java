package vip.seanxq.weibo.common.bean.result;

import java.io.Serializable;

import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

/**
 *
 * @author Daniel Qian
 */
@Data
public class WeiboMediaUploadResult implements Serializable {
  private static final long serialVersionUID = 330834334738622341L;

  private String url;
  private String type;
  private String mediaId;
  private String thumbMediaId;
  private long createdAt;

  public static WeiboMediaUploadResult fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboMediaUploadResult.class);
  }

  @Override
  public String toString() {
    return WeiboGsonBuilder.create().toJson(this);
  }

}
