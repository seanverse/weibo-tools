package vip.seanxq.weibo.common.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.bean.WeiboAccessToken;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian
 */
public class WeiboAccessTokenAdapter implements JsonDeserializer<WeiboAccessToken> {

  @Override
  public WeiboAccessToken deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboAccessToken accessToken = new WeiboAccessToken();
    JsonObject accessTokenJsonObject = json.getAsJsonObject();

    if (accessTokenJsonObject.get("access_token") != null && !accessTokenJsonObject.get("access_token").isJsonNull()) {
      accessToken.setAccessToken(GsonHelper.getAsString(accessTokenJsonObject.get("access_token")));
    }
    if (accessTokenJsonObject.get("expires_in") != null && !accessTokenJsonObject.get("expires_in").isJsonNull()) {
      accessToken.setExpiresIn(GsonHelper.getAsPrimitiveInt(accessTokenJsonObject.get("expires_in")));
    }
    return accessToken;
  }

}
