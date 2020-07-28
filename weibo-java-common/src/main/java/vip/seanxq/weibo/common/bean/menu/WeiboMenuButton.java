package vip.seanxq.weibo.common.bean.menu;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

/**
 * menu button.
 *
 * @author Daniel Qian
 */
@Data
public class WeiboMenuButton implements Serializable {
  private static final long serialVersionUID = -1070939403109776555L;

  /**
   * <pre>
   * 菜单的响应动作类型.
   * view表示网页类型，
   * click表示点击类型，
   * </pre>
   */
  private String type;

  /**
   * 菜单标题，不超过16个字节，子菜单不超过40个字节.
   */
  private String name;

  /**
   * <pre>
   * 菜单KEY值，用于消息接口推送，不超过128字节.
   * click等点击类型必须
   * </pre>
   */
  private String key;

  /**
   * <pre>
   * 网页链接.
   * 用户点击菜单可打开链接，不超过256字节。view类型必须
   * </pre>
   */
  private String url;

  @SerializedName("sub_button")
  private List<WeiboMenuButton> subButtons = new ArrayList<>();

  @Override
  public String toString() {
    return WeiboGsonBuilder.create().toJson(this);
  }

}
