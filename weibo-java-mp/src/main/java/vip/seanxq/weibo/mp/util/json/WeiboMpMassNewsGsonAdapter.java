package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.mp.bean.WeiboMpMassNews;
import vip.seanxq.weibo.mp.bean.material.WeiboMpNewsArticle;

import java.lang.reflect.Type;

public class WeiboMpMassNewsGsonAdapter implements JsonSerializer<WeiboMpMassNews>, JsonDeserializer<WeiboMpMassNews> {

  @Override
  public JsonElement serialize(WeiboMpMassNews message, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject newsJson = new JsonObject();

    JsonArray articleJsonArray = new JsonArray();
    for (WeiboMpNewsArticle article : message.getArticles()) {
      JsonObject articleJson = WbMpGsonBuilder.create().toJsonTree(article, WeiboMpNewsArticle.class).getAsJsonObject();
      articleJsonArray.add(articleJson);
    }
    newsJson.add("articles", articleJsonArray);

    return newsJson;
  }

  @Override
  public WeiboMpMassNews deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    WeiboMpMassNews weiboMpMassNews = new WeiboMpMassNews();
    JsonObject json = jsonElement.getAsJsonObject();
    if (json.get("media_id") != null && !json.get("media_id").isJsonNull()) {
      JsonArray articles = json.getAsJsonArray("articles");
      for (JsonElement article1 : articles) {
        JsonObject articleInfo = article1.getAsJsonObject();
        WeiboMpNewsArticle article = WbMpGsonBuilder.create().fromJson(articleInfo, WeiboMpNewsArticle.class);
        weiboMpMassNews.addArticle(article);
      }
    }
    return weiboMpMassNews;
  }
}
