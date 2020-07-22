package vip.seanxq.weibo.mp.bean.tag;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 * 获取标签下粉丝列表的结果对象
 * Created by Binary Wang on 2016-09-19.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
public class WeiboTagListUser implements Serializable {
  private static final long serialVersionUID = -4551768374200676112L;

  /**
   * "count":2,这次获取的粉丝数量.
   */
  @SerializedName("count")
  private Integer count;
  /**
   * "data" 粉丝列表.
   */
  @SerializedName("data")
  private WxTagListUserData data;
  /**
   * "next_openid" 拉取列表最后一个用户的openid.
   */
  @SerializedName("next_openid")
  private String nextOpenid;

  public static WeiboTagListUser fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboTagListUser.class);
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return this.toJson();
  }

  @Data
  public static class WxTagListUserData implements Serializable {
    private static final long serialVersionUID = -8584537400336245701L;

    /**
     * openid 列表.
     */
    @SerializedName("openid")
    private List<String> openidList;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }
  }
}
