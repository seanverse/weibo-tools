package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.mp.bean.message.WeiboKefuMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMessMessage;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.util.Arrays;

@Slf4j
public class WeiboMessMessageGsonAdapter implements JsonSerializer<WeiboMessMessage> {

  /**
   * <pre>
   *   JSON格式见 <a href=“https://open.weibo.com/wiki/高级群发接口”>高级群发接口的JSON格式</a>
   * </pre>
   */
  @Override
  public JsonElement serialize(WeiboMessMessage message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject messageJson = new JsonObject();

    JsonObject filterJson = new JsonObject();
    JsonObject contentJson = new JsonObject();

    if (message.isFilterByGroup())
    {
       filterJson.addProperty("group_id", message.getGroupId());
    }
    else
    {
      if (message.getUidList() != null)
      {
          JsonArray uidJson = new JsonArray();
          for(String u : message.getUidList()){uidJson.add(u); }
          filterJson.add("touser", uidJson);
      }
    }
    switch (message.getType()) {
      case MSG_TEXT:
        JsonObject t = new JsonObject();
        t.addProperty("content", message.getText());
        contentJson.add("text", t);
        break;
      case MSG_ARTICLES:
        JsonArray jsonArray = new JsonArray();
        if (message.getArticles() != null)
          message.getArticles().forEach(f -> {
            jsonArray.add(f.toJsonObject());
          });
        contentJson.add("articles", jsonArray);
        break;
      default:
        throw new RuntimeException("非法消息类型，暂不支持");
    }
    messageJson.addProperty("msgtype", message.getType().getMsgTypeText());
    return messageJson;
  }

}
