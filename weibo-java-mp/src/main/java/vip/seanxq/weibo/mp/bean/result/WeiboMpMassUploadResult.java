package vip.seanxq.weibo.mp.bean.result;

import java.io.Serializable;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 * 上传群发用的素材的结果.
 * 视频和图文消息需要在群发前上传素材
 * </pre>
 *
 * @author chanjarster
 */
@Data
public class WeiboMpMassUploadResult implements Serializable {
  private static final long serialVersionUID = 6568157943644994029L;

  private String type;
  private String mediaId;
  private long createdAt;

  public static WeiboMpMassUploadResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpMassUploadResult.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}
