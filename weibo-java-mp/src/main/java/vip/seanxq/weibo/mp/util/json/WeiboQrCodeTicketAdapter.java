package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboFansQrCodeTicket;

import java.lang.reflect.Type;

/**
 * {@link WeiboFansQrCodeTicket}
 */
public class WeiboQrCodeTicketAdapter implements JsonDeserializer<WeiboFansQrCodeTicket> {

  @Override
  public WeiboFansQrCodeTicket deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboFansQrCodeTicket ticket = new WeiboFansQrCodeTicket();
    JsonObject ticketJsonObject = json.getAsJsonObject();

    if (ticketJsonObject.get("ticket") != null && !ticketJsonObject.get("ticket").isJsonNull()) {
      ticket.setTicket(GsonHelper.getAsString(ticketJsonObject.get("ticket")));
    }
    if (ticketJsonObject.get("expire_seconds") != null && !ticketJsonObject.get("expire_seconds").isJsonNull()) {
      ticket.setExpireSeconds(GsonHelper.getAsPrimitiveInt(ticketJsonObject.get("expire_seconds")));
    }
    /*if (ticketJsonObject.get("url") != null && !ticketJsonObject.get("url").isJsonNull()) {
      ticket.setUrl(GsonHelper.getAsString(ticketJsonObject.get("url")));
    }*/
    return ticket;
  }

}
