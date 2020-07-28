package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUserList;

import java.lang.reflect.Type;

public class WeiboUserListGsonAdapter implements JsonDeserializer<WeiboFansUserList> {

  @Override
  public WeiboFansUserList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboFansUserList weiboFansUserList = new WeiboFansUserList();
    weiboFansUserList.setTotal(GsonHelper.getLong(o, "total"));
    weiboFansUserList.setCount(GsonHelper.getInteger(o, "count"));
    weiboFansUserList.setNextUid(GsonHelper.getString(o, "next_uid"));
    if (o.get("data") != null && !o.get("data").isJsonNull() && !o.get("data").getAsJsonObject().get("uids").isJsonNull()) {
      JsonArray data = o.get("data").getAsJsonObject().get("uids").getAsJsonArray();
      for (int i = 0; i < data.size(); i++) {
        weiboFansUserList.getUids().add(GsonHelper.getAsString(data.get(i)));
      }
    }
    return weiboFansUserList;
  }

}
