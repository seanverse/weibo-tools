package vip.seanxq.weibo.mp.util.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboMpChangeOpenid;

import java.lang.reflect.Type;

public class WbMpChangeOpenidGsonAdapter implements JsonDeserializer<WeiboMpChangeOpenid> {

  @Override
  public WeiboMpChangeOpenid deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboMpChangeOpenid changeOpenid = new WeiboMpChangeOpenid();
    changeOpenid.setOriOpenid(GsonHelper.getString(o, "ori_openid"));
    changeOpenid.setNewOpenid(GsonHelper.getString(o, "new_openid"));
    changeOpenid.setErrMsg(GsonHelper.getString(o, "err_msg"));
    return changeOpenid;
  }

}
