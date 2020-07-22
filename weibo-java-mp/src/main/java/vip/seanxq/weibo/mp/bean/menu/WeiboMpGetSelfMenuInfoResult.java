package vip.seanxq.weibo.mp.bean.menu;

import java.io.Serializable;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 * Created by Binary Wang on 2016-11-25.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
public class WeiboMpGetSelfMenuInfoResult implements Serializable {
  private static final long serialVersionUID = -5612495636936835166L;

  @SerializedName("selfmenu_info")
  private WeiboMpSelfMenuInfo selfMenuInfo;

  @SerializedName("is_menu_open")
  private Integer isMenuOpen;

  public static WeiboMpGetSelfMenuInfoResult fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboMpGetSelfMenuInfoResult.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}
