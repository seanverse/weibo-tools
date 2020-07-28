package vip.seanxq.weibo.mp.bean.mass;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * 群发时用到的视频素材
 *
 * @author chanjarster
 */
@Data
public class WeiboMpMassVideo implements Serializable {
  private static final long serialVersionUID = 9153925016061915637L;

  private String mediaId;
  private String title;
  private String description;

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
