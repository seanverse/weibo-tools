package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialCountResult;

import java.lang.reflect.Type;

/**
 * @author codepiano
 */
public class WeiboMpMaterialCountResultAdapter implements JsonDeserializer<WeiboMpMaterialCountResult> {

  @Override
  public WeiboMpMaterialCountResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboMpMaterialCountResult countResult = new WeiboMpMaterialCountResult();
    JsonObject materialCountResultJsonObject = json.getAsJsonObject();

    if (materialCountResultJsonObject.get("voice_count") != null && !materialCountResultJsonObject.get("voice_count").isJsonNull()) {
      countResult.setVoiceCount(GsonHelper.getAsInteger(materialCountResultJsonObject.get("voice_count")));
    }
    if (materialCountResultJsonObject.get("video_count") != null && !materialCountResultJsonObject.get("video_count").isJsonNull()) {
      countResult.setVideoCount(GsonHelper.getAsInteger(materialCountResultJsonObject.get("video_count")));
    }
    if (materialCountResultJsonObject.get("image_count") != null && !materialCountResultJsonObject.get("image_count").isJsonNull()) {
      countResult.setImageCount(GsonHelper.getAsInteger(materialCountResultJsonObject.get("image_count")));
    }
    if (materialCountResultJsonObject.get("news_count") != null && !materialCountResultJsonObject.get("news_count").isJsonNull()) {
      countResult.setNewsCount(GsonHelper.getAsInteger(materialCountResultJsonObject.get("news_count")));
    }
    return countResult;
  }

}
