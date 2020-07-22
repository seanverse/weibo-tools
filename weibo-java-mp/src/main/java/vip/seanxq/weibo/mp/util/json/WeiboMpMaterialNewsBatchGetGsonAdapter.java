package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialNewsBatchGetResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WeiboMpMaterialNewsBatchGetGsonAdapter implements JsonDeserializer<WeiboMpMaterialNewsBatchGetResult> {

  @Override
  public WeiboMpMaterialNewsBatchGetResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    WeiboMpMaterialNewsBatchGetResult weiboMpMaterialNewsBatchGetResult = new WeiboMpMaterialNewsBatchGetResult();
    JsonObject json = jsonElement.getAsJsonObject();
    if (json.get("total_count") != null && !json.get("total_count").isJsonNull()) {
      weiboMpMaterialNewsBatchGetResult.setTotalCount(GsonHelper.getAsInteger(json.get("total_count")));
    }
    if (json.get("item_count") != null && !json.get("item_count").isJsonNull()) {
      weiboMpMaterialNewsBatchGetResult.setItemCount(GsonHelper.getAsInteger(json.get("item_count")));
    }
    if (json.get("item") != null && !json.get("item").isJsonNull()) {
      JsonArray item = json.getAsJsonArray("item");
      List<WeiboMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem> items = new ArrayList<>();
      for (JsonElement anItem : item) {
        JsonObject articleInfo = anItem.getAsJsonObject();
        items.add(WbMpGsonBuilder.create().fromJson(articleInfo, WeiboMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem.class));
      }
      weiboMpMaterialNewsBatchGetResult.setItems(items);
    }
    return weiboMpMaterialNewsBatchGetResult;
  }
}
