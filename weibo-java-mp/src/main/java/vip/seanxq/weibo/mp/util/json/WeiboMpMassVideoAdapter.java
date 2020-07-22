package vip.seanxq.weibo.mp.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import vip.seanxq.weibo.mp.bean.WeiboMpMassVideo;

import java.lang.reflect.Type;

/**
 * @author Daniel Qian
 */
public class WeiboMpMassVideoAdapter implements JsonSerializer<WeiboMpMassVideo> {

  @Override
  public JsonElement serialize(WeiboMpMassVideo message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();
    messageJson.addProperty("media_id", message.getMediaId());
    messageJson.addProperty("description", message.getDescription());
    messageJson.addProperty("title", message.getTitle());
    return messageJson;
  }

}
