package vip.seanxq.weibo.mp.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.mp.bean.mass.WeiboMpMassPreviewMessage;

import java.lang.reflect.Type;

/**
 * @author miller
 */
public class WeiboMpMassPreviewMessageGsonAdapter implements JsonSerializer<WeiboMpMassPreviewMessage> {
  @Override
  public JsonElement serialize(WeiboMpMassPreviewMessage weiboMpMassPreviewMessage, Type type, JsonSerializationContext jsonSerializationContext) {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("towxname", weiboMpMassPreviewMessage.getToWxUserName());
    jsonObject.addProperty("touser", weiboMpMassPreviewMessage.getToWxUserOpenid());
    if (WeiboConsts.MassMsgType.MPNEWS.equals(weiboMpMassPreviewMessage.getMsgType())) {
      JsonObject news = new JsonObject();
      news.addProperty("media_id", weiboMpMassPreviewMessage.getMediaId());
      jsonObject.add(WeiboConsts.MassMsgType.MPNEWS, news);
    }
    if (WeiboConsts.MassMsgType.TEXT.equals(weiboMpMassPreviewMessage.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("content", weiboMpMassPreviewMessage.getContent());
      jsonObject.add(WeiboConsts.MassMsgType.TEXT, sub);
    }
    if (WeiboConsts.MassMsgType.VOICE.equals(weiboMpMassPreviewMessage.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", weiboMpMassPreviewMessage.getMediaId());
      jsonObject.add(WeiboConsts.MassMsgType.VOICE, sub);
    }
    if (WeiboConsts.MassMsgType.IMAGE.equals(weiboMpMassPreviewMessage.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", weiboMpMassPreviewMessage.getMediaId());
      jsonObject.add(WeiboConsts.MassMsgType.IMAGE, sub);
    }
    if (WeiboConsts.MassMsgType.MPVIDEO.equals(weiboMpMassPreviewMessage.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", weiboMpMassPreviewMessage.getMediaId());
      jsonObject.add(WeiboConsts.MassMsgType.MPVIDEO, sub);
    }
    jsonObject.addProperty("msgtype", weiboMpMassPreviewMessage.getMsgType());
    return jsonObject;
  }
}
