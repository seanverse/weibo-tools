package vip.seanxq.weibo.mp.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.mp.bean.mass.WeiboMpMassTagMessage;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

/**
 * 群发消息json转换适配器.
 *
 * @author chanjarster
 */
public class WeiboMpMassTagMessageGsonAdapter implements JsonSerializer<WeiboMpMassTagMessage> {

  @Override
  public JsonElement serialize(WeiboMpMassTagMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();

    JsonObject filter = new JsonObject();
    if (message.isSendAll() || null == message.getTagId()) {
      filter.addProperty("is_to_all", true);
    } else {
      filter.addProperty("is_to_all", false);
      filter.addProperty("tag_id", message.getTagId());
    }
    messageJson.add("filter", filter);

    if (WeiboConsts.MassMsgType.MPNEWS.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WeiboConsts.MassMsgType.MPNEWS, sub);
    }
    if (WeiboConsts.MassMsgType.TEXT.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("content", message.getContent());
      messageJson.add(WeiboConsts.MassMsgType.TEXT, sub);
    }
    if (WeiboConsts.MassMsgType.VOICE.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WeiboConsts.MassMsgType.VOICE, sub);
    }
    if (WeiboConsts.MassMsgType.IMAGE.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WeiboConsts.MassMsgType.IMAGE, sub);
    }
    if (WeiboConsts.MassMsgType.MPVIDEO.equals(message.getMsgType())) {
      JsonObject sub = new JsonObject();
      sub.addProperty("media_id", message.getMediaId());
      messageJson.add(WeiboConsts.MassMsgType.MPVIDEO, sub);
    }
    messageJson.addProperty("msgtype", message.getMsgType());
    messageJson.addProperty("send_ignore_reprint", message.isSendIgnoreReprint() ? 0 : 1);

    if (StringUtils.isNotEmpty(message.getClientMsgId())) {
      messageJson.addProperty("clientmsgid", message.getClientMsgId());
    }

    return messageJson;
  }

}
