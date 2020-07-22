package vip.seanxq.weibo.mp.bean.material;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

@Data
public class WeiboMpMaterialNewsBatchGetResult implements Serializable {
  private static final long serialVersionUID = -1617952797921001666L;

  private int totalCount;
  private int itemCount;
  private List<WxMaterialNewsBatchGetNewsItem> items;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Data
  public static class WxMaterialNewsBatchGetNewsItem {
    private String mediaId;
    private Date updateTime;
    private WeiboMpMaterialNews content;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }
  }
}
