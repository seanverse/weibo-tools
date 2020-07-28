package vip.seanxq.weibo.mp.bean.tag;

import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.List;

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
  private long id=0;

  /**
   * 标签名，UTF8编码.
   */
  private String name="";

  /**
   * 此标签下粉丝数.
   */
  private long count = 0;

  @SerializedName("rule_type")
  private TagRuleType ruleType = TagRuleType.BY_CUSTOM_GROUP;

  public static WeiboUserTag fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(new JsonParser().parse(json).getAsJsonObject().get("group"), WeiboUserTag.class);
  }

  /**
   * <pre>
   *   获取自定义规则分组，返回的数据结构
   *  {
   *     "groups": [
   *         {
   *             "id": 0,
   *             "name": "未分组",
   *             "count": 72596,
   *             "rule_type": 1			//0:关键词分组；
   *         },
   *         {
   *             "id": 1,
   *             "name": "黑名单",
   *             "count": 36,
   *             "rule_type": 2			//2:自定义分组
   *         }
   *     ]
   * }   *
   * </pre>
   *
   * @param json
   * @return List<WeiboUserTag>
   */
  public static List<WeiboUserTag> listFromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(new JsonParser().parse(json).getAsJsonObject().get("groups"),
      new TypeToken<List<WeiboUserTag>>() { }.getType());
  }

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Override
  public String toString() {
    return this.toJson();
  }
}
