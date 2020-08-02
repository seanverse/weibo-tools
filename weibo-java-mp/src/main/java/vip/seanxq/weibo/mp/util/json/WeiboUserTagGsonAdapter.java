package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.group.GroupRuleType;
import vip.seanxq.weibo.mp.bean.group.WeiboUserGroup;

import java.lang.reflect.Type;

public class WeiboUserTagGsonAdapter implements JsonDeserializer<WeiboUserGroup> {
  @Override
  public WeiboUserGroup deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboUserGroup groupTag = new WeiboUserGroup();
    groupTag.setId(GsonHelper.getLong(o, "id"));
    groupTag.setName(GsonHelper.getString(o, "name"));
    if (o.get("count") != null && !o.get("count").isJsonNull()) {
      groupTag.setCount(GsonHelper.getLong(o, "count"));
    }

    if (o.get("rule_type") != null && !o.get("rule_type").isJsonNull()) {
      groupTag.setRuleType(GroupRuleType.valueOf(GsonHelper.getString(o, "rule_type")));
    }
    return  groupTag;
  }

}


