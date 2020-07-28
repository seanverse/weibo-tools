package vip.seanxq.weibo.mp.util.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import vip.seanxq.weibo.common.util.json.GsonHelper;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUser;

public class WeiboFansUserGsonAdapter implements JsonDeserializer<WeiboFansUser> {

  @Override
  public WeiboFansUser deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    JsonObject o = json.getAsJsonObject();
    WeiboFansUser user = new WeiboFansUser();

    Integer subscribe = GsonHelper.getInteger(o, "subscribe");
    if (subscribe != null) {
      user.setSubscribe(!new Integer(0).equals(subscribe));
    }
    user.setUid(GsonHelper.getString(o, "uid"));
    user.setCity(GsonHelper.getString(o, "city"));
    user.setCountry(GsonHelper.getString(o, "country"));
    user.setHeadImgUrl(GsonHelper.getString(o, "headimgurl"));
    user.setHeadimgurlLarge(GsonHelper.getString(o, "headimgurl_large"));
    user.setHeadimgurlHd(GsonHelper.getString(o, "headimgurl_hd"));
    user.setLanguage(GsonHelper.getString(o, "language"));
    user.setNickname(GsonHelper.getString(o, "nickname"));
    user.setProvince(GsonHelper.getString(o, "province"));
    user.setSubscribeTime(GsonHelper.getLong(o, "subscribe_time"));
    user.setRemark(GsonHelper.getString(o, "remark"));

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
    //	该用户是否关注access_token中的uid，1：是，0：否
    user.setFollow(GsonHelper.getInteger(o, "follow"));
    return user;
  }

}
