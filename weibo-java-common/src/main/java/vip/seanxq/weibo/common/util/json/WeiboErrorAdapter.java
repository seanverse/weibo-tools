package vip.seanxq.weibo.common.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.error.WeiboError;

import java.lang.reflect.Type;

/**
 * WeiboError gson 反序列化Adapter。
 */
public class WeiboErrorAdapter implements JsonDeserializer<WeiboError> {

  @Override
  public WeiboError deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
    throws JsonParseException {
    WeiboError.WeiboErrorBuilder errorBuilder = WeiboError.builder();
    JsonObject wxErrorJsonObject = json.getAsJsonObject();
    //errcode 转 errorcode, errmsg 转 errmsg
    if (wxErrorJsonObject.get("errcode") != null && !wxErrorJsonObject.get("errcode").isJsonNull()) {
      errorBuilder.errorCode(GsonHelper.getAsPrimitiveInt(wxErrorJsonObject.get("errcode")));
    }
    if (wxErrorJsonObject.get("errmsg") != null && !wxErrorJsonObject.get("errmsg").isJsonNull()) {
      errorBuilder.errorMsg(GsonHelper.getAsString(wxErrorJsonObject.get("errmsg")));
    }

    errorBuilder.json(json.toString());

    return errorBuilder.build();
  }

}
