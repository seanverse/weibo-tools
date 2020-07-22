package vip.seanxq.weibo.mp.bean.material;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * @author codepiano
 */
@Data
public class WeiboMpMaterialFileBatchGetResult implements Serializable {
  private static final long serialVersionUID = -560388368297267884L;

  private int totalCount;
  private int itemCount;
  private List<WxMaterialFileBatchGetNewsItem> items;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Data
  public static class WxMaterialFileBatchGetNewsItem {
    private String mediaId;
    private Date updateTime;
    private String name;
    private String url;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }
  }
}
