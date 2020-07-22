package vip.seanxq.weibo.mp.bean.material;

import java.io.Serializable;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * @author codepiano
 */
@Data
public class WeiboMpMaterialCountResult implements Serializable {
  private static final long serialVersionUID = -5568772662085874138L;

  private int voiceCount;
  private int videoCount;
  private int imageCount;
  private int newsCount;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}

