package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialFileBatchGetResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class WeiboMpMaterialFileBatchGetGsonAdapter implements JsonDeserializer<WeiboMpMaterialFileBatchGetResult> {

  @Override
  public WeiboMpMaterialFileBatchGetResult deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    WeiboMpMaterialFileBatchGetResult weiboMpMaterialFileBatchGetResult = new WeiboMpMaterialFileBatchGetResult();
    JsonObject json = jsonElement.getAsJsonObject();
    if (json.get("total_count") != null && !json.get("total_count").isJsonNull()) {
      weiboMpMaterialFileBatchGetResult.setTotalCount(GsonHelper.getAsInteger(json.get("total_count")));
    }
    if (json.get("item_count") != null && !json.get("item_count").isJsonNull()) {
      weiboMpMaterialFileBatchGetResult.setItemCount(GsonHelper.getAsInteger(json.get("item_count")));
    }
    if (json.get("item") != null && !json.get("item").isJsonNull()) {
      JsonArray item = json.getAsJsonArray("item");
      List<WeiboMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem> items = new ArrayList<>();
      for (JsonElement anItem : item) {
        JsonObject articleInfo = anItem.getAsJsonObject();
        items.add(WbMpGsonBuilder.create().fromJson(articleInfo, WeiboMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem.class));
      }
      weiboMpMaterialFileBatchGetResult.setItems(items);
    }
    return weiboMpMaterialFileBatchGetResult;
  }
}
