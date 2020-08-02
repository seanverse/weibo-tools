package vip.seanxq.weibo.mp.bean.message;

import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.io.Serializable;

@Data
public class MsgArticle implements Serializable {
  private static final long serialVersionUID = 5145137235440507379L;

  /**
   * 图文的显示名称标题
   */
  private String display_name;
  /**
   * 图文的文字描述，大于等于2个图文时，仅显示第一个图文的描述
   */
  private String summary;
  /**
   * 	图文的URL地址，点击后跳转（注：该url必须为完整的url，例如， http://weibo.com/xxx ，如果省略掉”http:// “，则无法发送图文消息）
   * 	http://e.weibo.com/mediaprofile/article/detail?uid=1722052204&aid=983319"
   */
  private String url;
  /**
   * 图文的缩略显示图片，需为JPG、PNG格式，单图及多图第一张推荐使用280*155，多图非第一张推荐使用64*64
   * "http://storage.mcp.weibo.cn/0JlIv.jpg",
   */
  @SerializedName("image")
  private String imageurl;

  public JsonObject toJsonObject()
  {
    JsonObject j = new JsonObject();
    j.addProperty("display_name", this.display_name);
    j.addProperty("summary", this.summary);
    j.addProperty("image", this.imageurl);
    j.addProperty("url", this.url);
    return j;
  }
}
