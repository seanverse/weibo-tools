package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialUploadResult;

import java.lang.reflect.Type;

/**
 * @author codepiano
 */
public class WeiboMpMaterialUploadResultAdapter implements JsonDeserializer<WeiboMpMaterialUploadResult> {

  @Override
  public WeiboMpMaterialUploadResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboMpMaterialUploadResult uploadResult = new WeiboMpMaterialUploadResult();
    JsonObject uploadResultJsonObject = json.getAsJsonObject();

    if (uploadResultJsonObject.get("url") != null && !uploadResultJsonObject.get("url").isJsonNull()) {
      uploadResult.setUrl(GsonHelper.getAsString(uploadResultJsonObject.get("url")));
    }
    if (uploadResultJsonObject.get("media_id") != null && !uploadResultJsonObject.get("media_id").isJsonNull()) {
      uploadResult.setMediaId(GsonHelper.getAsString(uploadResultJsonObject.get("media_id")));
    }
    return uploadResult;
  }

}
