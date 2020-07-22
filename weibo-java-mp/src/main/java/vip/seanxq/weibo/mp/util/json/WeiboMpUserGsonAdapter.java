package vip.seanxq.weibo.mp.util.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUser;

public class WeiboMpUserGsonAdapter implements JsonDeserializer<WeiboMpUser> {

  @Override
  public WeiboMpUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboMpUser user = new WeiboMpUser();
    Integer subscribe = GsonHelper.getInteger(o, "subscribe");
    if (subscribe != null) {
      user.setSubscribe(!new Integer(0).equals(subscribe));
    }
    user.setCity(GsonHelper.getString(o, "city"));
    user.setCountry(GsonHelper.getString(o, "country"));
    user.setHeadImgUrl(GsonHelper.getString(o, "headimgurl"));
    user.setLanguage(GsonHelper.getString(o, "language"));
    user.setNickname(GsonHelper.getString(o, "nickname"));
    user.setOpenId(GsonHelper.getString(o, "openid"));
    user.setProvince(GsonHelper.getString(o, "province"));
    user.setSubscribeTime(GsonHelper.getLong(o, "subscribe_time"));
    user.setUnionId(GsonHelper.getString(o, "unionid"));
    user.setRemark(GsonHelper.getString(o, "remark"));
    user.setGroupId(GsonHelper.getInteger(o, "groupid"));
    user.setTagIds(GsonHelper.getLongArray(o, "tagid_list"));
    user.setPrivileges(GsonHelper.getStringArray(o, "privilege"));
    user.setSubscribeScene(GsonHelper.getString(o, "subscribe_scene"));
    user.setQrScene(GsonHelper.getString(o, "qr_scene"));
    user.setQrSceneStr(GsonHelper.getString(o, "qr_scene_str"));

    Integer sex = GsonHelper.getInteger(o, "sex");
    if (sex != null) {
      user.setSex(sex);
      switch (sex) {
        case 1:
          user.setSexDesc("男");
          break;
        case 2:
          user.setSexDesc("女");
          break;
        default:
          user.setSexDesc("未知");
      }

    }
    return user;
  }

}
