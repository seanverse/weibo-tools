package vip.seanxq.weibo.mp.bean.mass;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import vip.seanxq.weibo.mp.bean.material.WeiboMpNewsArticle;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * 群发时用到的图文消息素材.
 *
 * @author chanjarster
 */
@Data
public class WeiboMpMassNews implements Serializable {
  private static final long serialVersionUID = 565937155013581016L;

  private List<WeiboMpNewsArticle> articles = new ArrayList<>();

  public void addArticle(WeiboMpNewsArticle article) {
    this.articles.add(article);
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  public boolean isEmpty() {
    return this.articles == null || this.articles.isEmpty();
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}