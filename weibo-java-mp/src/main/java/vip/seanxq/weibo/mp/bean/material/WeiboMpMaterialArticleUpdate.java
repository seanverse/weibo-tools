package vip.seanxq.weibo.mp.bean.material;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

@Data
public class WeiboMpMaterialArticleUpdate implements Serializable {
  private static final long serialVersionUID = -7611963949517780270L;

  private String mediaId;
  private int index;
  private WeiboMpNewsArticle articles;

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
