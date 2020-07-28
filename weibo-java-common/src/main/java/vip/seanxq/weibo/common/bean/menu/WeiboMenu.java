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
 * <pre>
 * 微博自定义菜单
 * 参数说明：
 * 1，button 必填 一级菜单数组，个数应为1~3个
 * 2，sub_button 选填 二级菜单数组，个数应为1~5个
 * 3，type 选填 （与二级菜单sub_button同级时可不填）菜单的响应动作类型，目前有click、view两种类型
 * 4，name 必填 菜单标题，不超过16个字节，子菜单不超过40个字节
 * 5，key 选填 type为click时必填，菜单KEY值，用于消息接口推送，不超过128字节
 * 6，url 选填 type为view时必填，网页链接，用户点击菜单可打开链接，不超过256字节
 * </pre>
 */
@Data
public class WeiboMenu implements Serializable {
  private static final long serialVersionUID = -7083914585539687746L;

  private List<WeiboMenuButton> buttons = new ArrayList<>();

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
