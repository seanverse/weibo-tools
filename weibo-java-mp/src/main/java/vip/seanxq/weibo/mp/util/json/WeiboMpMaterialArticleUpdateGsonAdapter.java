package vip.seanxq.weibo.mp.util.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialArticleUpdate;
import vip.seanxq.weibo.mp.bean.material.WeiboMpNewsArticle;

import java.lang.reflect.Type;

public class WeiboMpMaterialArticleUpdateGsonAdapter implements JsonSerializer<WeiboMpMaterialArticleUpdate> {

  @Override
  public JsonElement serialize(WeiboMpMaterialArticleUpdate weiboMpMaterialArticleUpdate, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject articleUpdateJson = new JsonObject();
    articleUpdateJson.addProperty("media_id", weiboMpMaterialArticleUpdate.getMediaId());
    articleUpdateJson.addProperty("index", weiboMpMaterialArticleUpdate.getIndex());
    articleUpdateJson.add("articles", WbMpGsonBuilder.create().toJsonTree(weiboMpMaterialArticleUpdate.getArticles(), WeiboMpNewsArticle.class));
    return articleUpdateJson;
  }

}
