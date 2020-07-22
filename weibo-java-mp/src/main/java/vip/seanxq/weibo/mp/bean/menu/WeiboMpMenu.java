package vip.seanxq.weibo.mp.bean.menu;

import java.io.Serializable;
import java.util.List;

import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.common.bean.menu.WeiboMenuButton;
import vip.seanxq.weibo.common.bean.menu.WeiboMenuRule;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 *   公众号专用的菜单类，可能包含个性化菜单
 * Created by Binary Wang on 2017-1-17.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
public class WeiboMpMenu implements Serializable {
  private static final long serialVersionUID = -5794350513426702252L;

  @SerializedName("menu")
  private WxMpConditionalMenu menu;

  @SerializedName("conditionalmenu")
  private List<WxMpConditionalMenu> conditionalMenu;

  public static WeiboMpMenu fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboMpMenu.class);
  }

  @Override
  public String toString() {
    return this.toJson();
  }

  public String toJson() {
    return WeiboGsonBuilder.create().toJson(this);
  }

  @Data
  public static class WxMpConditionalMenu implements Serializable {
    private static final long serialVersionUID = -2279946921755382289L;

    @SerializedName("button")
    private List<WeiboMenuButton> buttons;
    @SerializedName("matchrule")
    private WeiboMenuRule rule;
    @SerializedName("menuid")
    private String menuId;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }

  }

}
