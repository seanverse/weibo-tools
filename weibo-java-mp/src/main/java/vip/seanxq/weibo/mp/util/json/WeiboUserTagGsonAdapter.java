package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.tag.TagRuleType;
import vip.seanxq.weibo.mp.bean.tag.WeiboUserTag;

import java.lang.reflect.Type;

public class WeiboUserTagGsonAdapter implements JsonDeserializer<WeiboUserTag> {
  @Override
  public WeiboUserTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboUserTag groupTag = new WeiboUserTag();
    groupTag.setId(GsonHelper.getLong(o, "id"));
    groupTag.setName(GsonHelper.getString(o, "name"));
    if (o.get("count") != null && !o.get("count").isJsonNull()) {
      groupTag.setCount(GsonHelper.getLong(o, "count"));
    }

    if (o.get("rule_type") != null && !o.get("rule_type").isJsonNull()) {
      groupTag.setRuleType(TagRuleType.valueOf(GsonHelper.getString(o, "rule_type")));
    }
    return  groupTag;
  }

}


