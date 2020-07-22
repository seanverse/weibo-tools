package vip.seanxq.weibo.mp.bean.tag;

import java.io.Serializable;
import java.util.List;

import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 *  用户标签对象
 *  Created by Binary Wang on 2016/9/2.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
public class WeiboUserTag implements Serializable {
  private static final long serialVersionUID = -7722428695667031252L;

  /**
   * 标签id，由微博分配.
   */
  private Long id;

  /**
   * 标签名，UTF8编码.
   */
  private String name;

  /**
   * 此标签下粉丝数.
   */
  private Integer count;

  public static WeiboUserTag fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(
      new JsonParser().parse(json).getAsJsonObject().get("tag"),
      WeiboUserTag.class);
  }

  public static List<WeiboUserTag> listFromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(
      new JsonParser().parse(json).getAsJsonObject().get("tags"),
      new TypeToken<List<WeiboUserTag>>() {
      }.getType());
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return this.toJson();
  }
}
