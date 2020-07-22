package vip.seanxq.weibo.mp.bean.material;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * 图文素材.
 *
 * @author codepiano
 */
@Data
public class WeiboMpMaterialNews implements Serializable {
  private static final long serialVersionUID = -3283203652013494976L;

  private Date createTime;
  private Date updateTime;

  private List<WeiboMpNewsArticle> articles = new ArrayList<>();

  public List<WeiboMpNewsArticle> getArticles() {
    return this.articles;
  }

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
    return this.toJson();
  }

}
