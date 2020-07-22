package vip.seanxq.weibo.common.bean.menu;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

/**
 * 菜单
 */
@Data
public class WeiboMenu implements Serializable {
  private static final long serialVersionUID = -7083914585539687746L;

  private List<WeiboMenuButton> buttons = new ArrayList<>();

  private WeiboMenuRule matchRule;


  public static WeiboMenu fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboMenu.class);
  }

  public static WeiboMenu fromJson(InputStream is) {
    return WeiboGsonBuilder.create()
      .fromJson(new InputStreamReader(is, StandardCharsets.UTF_8), WeiboMenu.class);
  }

  public String toJson() {
    return WeiboGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return this.toJson();
  }

}
