package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUserBlacklistGetResult;

import java.lang.reflect.Type;

/**
 * @author miller
 */
public class WeiboUserBlacklistGetResultGsonAdapter implements JsonDeserializer<WeiboMpUserBlacklistGetResult> {
  @Override
  public WeiboMpUserBlacklistGetResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboMpUserBlacklistGetResult weiboMpUserBlacklistGetResult = new WeiboMpUserBlacklistGetResult();
    weiboMpUserBlacklistGetResult.setTotal(GsonHelper.getInteger(o, "total"));
    weiboMpUserBlacklistGetResult.setCount(GsonHelper.getInteger(o, "count"));
    weiboMpUserBlacklistGetResult.setNextOpenid(GsonHelper.getString(o, "next_openid"));
    if (o.get("data") != null && !o.get("data").isJsonNull() && !o.get("data").getAsJsonObject().get("openid").isJsonNull()) {
      JsonArray data = o.get("data").getAsJsonObject().get("openid").getAsJsonArray();
      for (int i = 0; i < data.size(); i++) {
        weiboMpUserBlacklistGetResult.getOpenidList().add(GsonHelper.getAsString(data.get(i)));
      }
    }
    return weiboMpUserBlacklistGetResult;
  }
}
