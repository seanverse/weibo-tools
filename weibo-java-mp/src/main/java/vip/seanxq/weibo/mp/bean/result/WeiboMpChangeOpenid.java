package vip.seanxq.weibo.mp.bean.result;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

/**
 * 主体变更迁移用户 openid 返回.
 *
 * @author 007gzs
 */
@Data
public class WeiboMpChangeOpenid implements Serializable {
  private static final long serialVersionUID = -8132023284876534743L;
  private String oriOpenid;
  private String newOpenid;
  private String errMsg;
  public static WeiboMpChangeOpenid fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpChangeOpenid.class);
  }
  public static List<WeiboMpChangeOpenid> fromJsonList(String json) {
    Type collectionType = new TypeToken<List<WeiboMpChangeOpenid>>() {
    }.getType();
    Gson gson = WbMpGsonBuilder.create();
    JsonObject jsonObject = gson.fromJson(json, JsonObject.class);
    return gson.fromJson(jsonObject.get("result_list"), collectionType);
  }
}
