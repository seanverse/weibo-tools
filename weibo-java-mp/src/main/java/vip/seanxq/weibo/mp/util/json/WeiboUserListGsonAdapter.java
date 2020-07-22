package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUserList;

import java.lang.reflect.Type;

public class WeiboUserListGsonAdapter implements JsonDeserializer<WeiboMpUserList> {

  @Override
  public WeiboMpUserList deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboMpUserList weiboMpUserList = new WeiboMpUserList();
    weiboMpUserList.setTotal(GsonHelper.getLong(o, "total"));
    weiboMpUserList.setCount(GsonHelper.getInteger(o, "count"));
    weiboMpUserList.setNextOpenid(GsonHelper.getString(o, "next_openid"));
    if (o.get("data") != null && !o.get("data").isJsonNull() && !o.get("data").getAsJsonObject().get("openid").isJsonNull()) {
      JsonArray data = o.get("data").getAsJsonObject().get("openid").getAsJsonArray();
      for (int i = 0; i < data.size(); i++) {
        weiboMpUserList.getOpenids().add(GsonHelper.getAsString(data.get(i)));
      }
    }
    return weiboMpUserList;
  }

}
