package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.material.WeiboMediaImgUploadResult;

import java.lang.reflect.Type;

/**
 * @author miller
 */
public class WbMediaImgUploadResultGsonAdapter implements JsonDeserializer<WeiboMediaImgUploadResult> {
  @Override
  public WeiboMediaImgUploadResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboMediaImgUploadResult weiboMediaImgUploadResult = new WeiboMediaImgUploadResult();
    JsonObject jsonObject = json.getAsJsonObject();
    if (null != jsonObject.get("url") && !jsonObject.get("url").isJsonNull()) {
      weiboMediaImgUploadResult.setUrl(GsonHelper.getAsString(jsonObject.get("url")));
    }
    return weiboMediaImgUploadResult;
  }
}
