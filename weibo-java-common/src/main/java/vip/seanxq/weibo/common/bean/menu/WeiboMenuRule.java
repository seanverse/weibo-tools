package vip.seanxq.weibo.common.bean.menu;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

/**
 * menu rule.
 *
 * @author Daniel Qian
 */
@Data
public class WeiboMenuRule implements Serializable {
  private static final long serialVersionUID = -4587181819499286670L;

  /**
   * 反序列化时这里反人类的使用和序列化时不一样的名字.
   */
  @SerializedName(value = "tag_id", alternate = "group_id")
  private String tagId;
  private String sex;
  private String country;
  private String province;
  private String city;
  private String clientPlatformType;
  private String language;

  @Override
  public String toString() {
    return WeiboGsonBuilder.create().toJson(this);
  }
}
