package vip.seanxq.weibo.mp.bean.result;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * 微博粉丝用户信息.
 *
 * @author chanjarster
 */
@Data
public class WeiboFansUser implements Serializable {
  private static final long serialVersionUID = 5788154322646488738L;
  /**
   * 用户是否订阅该账号，值为0时，代表此用户没有订阅该账号；
   */
  private Boolean subscribe;
  private String uid; //用户id
  private String nickname;
  /**
   * 性别描述信息：男、女、未知等.
   */
  private String sexDesc;
  /**
   * 	性别，1：男、2：女、0：未知
   */
  private Integer sex;
  /**
   * 用户当前的语言版本，zh_CN 简体，zh_TW 繁体，en英语
   */
  private String language;
  private String city;
  private String province;
  private String country;
  /**
   * 	用户头像地址（大图），50×50像素
   */
  @SerializedName("headimgurl")
  private String headImgUrl;
  /**
   * 	用户头像地址（中图），180×180像素
   */
  @SerializedName("headimgurl_large")
  private String headimgurlLarge;

  /**
   * 	用户头像地址（高清），高清头像原图
   */
  @SerializedName("headimgurl_hd")
  private String headimgurlHd;
  @SerializedName("subscribe_time")
  private Long subscribeTime;
  /**
   * <pre>
   * 	该用户是否关注access_token中的uid，1：是，0：否
   * 	如果请求参数uid不是access_token中uid的粉丝，则返回：
   *    {
   *     "follow":    "0" //不同于微信subscribe=0就无法拉取其余信息，微博只有当follow=0时，也就是用户没有关注该账号时，才无法拉取其余信息；
   *    }
   * 	</pre>
   */
  private Integer follow;

  private String remark;

  public static WeiboFansUser fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboFansUser.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}
