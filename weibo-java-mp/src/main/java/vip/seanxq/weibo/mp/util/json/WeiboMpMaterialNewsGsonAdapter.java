package vip.seanxq.weibo.mp.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialNews;
import vip.seanxq.weibo.mp.bean.material.WeiboMpNewsArticle;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WeiboMpMaterialNewsGsonAdapter implements JsonSerializer<WeiboMpMaterialNews>, JsonDeserializer<WeiboMpMaterialNews> {

  @Override
  public JsonElement serialize(WeiboMpMaterialNews weiboMpMaterialNews, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject newsJson = new JsonObject();

    JsonArray articleJsonArray = new JsonArray();
    for (WeiboMpNewsArticle article : weiboMpMaterialNews.getArticles()) {
      JsonObject articleJson = WbMpGsonBuilder.create().toJsonTree(article, WeiboMpNewsArticle.class).getAsJsonObject();
      articleJsonArray.add(articleJson);
    }
    newsJson.add("articles", articleJsonArray);

    if (weiboMpMaterialNews.getCreateTime() != null) {
      newsJson.addProperty("create_time",
        SimpleDateFormat.getDateTimeInstance().format(weiboMpMaterialNews.getCreateTime()));
    }

    if (weiboMpMaterialNews.getUpdateTime() != null) {
      newsJson.addProperty("update_time",
        SimpleDateFormat.getDateTimeInstance().format(weiboMpMaterialNews.getUpdateTime()));
    }

    return newsJson;
  }

  @Override
  public WeiboMpMaterialNews deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
    WeiboMpMaterialNews weiboMpMaterialNews = new WeiboMpMaterialNews();
    JsonObject json = jsonElement.getAsJsonObject();
    if (json.get("news_item") != null && !json.get("news_item").isJsonNull()) {
      JsonArray articles = json.getAsJsonArray("news_item");
      for (JsonElement article1 : articles) {
        JsonObject articleInfo = article1.getAsJsonObject();
        WeiboMpNewsArticle article = WbMpGsonBuilder.create().fromJson(articleInfo, WeiboMpNewsArticle.class);
        weiboMpMaterialNews.addArticle(article);
      }
    }

    if (json.get("create_time") != null && !json.get("create_time").isJsonNull()) {
      Date createTime = new Date(GsonHelper.getAsLong(json.get("create_time"))* 1000);
      weiboMpMaterialNews.setCreateTime(createTime);
    }

    if (json.get("update_time") != null && !json.get("update_time").isJsonNull()) {
      Date updateTime = new Date(GsonHelper.getAsLong(json.get("update_time"))* 1000);
      weiboMpMaterialNews.setUpdateTime(updateTime);
    }

    return weiboMpMaterialNews;
  }
}
