package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.mp.bean.mass.WeiboMassOpenIdsMessage;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;

/**
 * @author someone
 */
public class WeiboMpMassOpenIdsMessageGsonAdapter implements JsonSerializer<WeiboMassOpenIdsMessage> {

  @Override
  public JsonElement serialize(WeiboMassOpenIdsMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();

    JsonArray toUsers = new JsonArray();
    for (String openId : message.getToUsers()) {
      toUsers.add(new JsonPrimitive(openId));
    }
    messageJson.add("touser", toUsers);

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

    if(StringUtils.isNotEmpty(message.getClientMsgId())){
      messageJson.addProperty("clientmsgid", message.getClientMsgId());
    }

    return messageJson;
  }

}
