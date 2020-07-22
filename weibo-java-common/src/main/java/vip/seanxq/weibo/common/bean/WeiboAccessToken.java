package vip.seanxq.weibo.common.bean;

import java.io.Serializable;

import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

/**
 * access token.
 *
 * @author Daniel Qian
 */
@Data
public class WeiboAccessToken implements Serializable {
  private static final long serialVersionUID = 8709719312922168909L;

  private String accessToken;

  private int expiresIn = -1;

  public static WeiboAccessToken fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboAccessToken.class);
  }

}
