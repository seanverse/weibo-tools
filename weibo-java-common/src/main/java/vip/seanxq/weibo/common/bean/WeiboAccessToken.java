package vip.seanxq.weibo.common.bean;

import java.io.Serializable;

import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

/**
 * access token.
 *
 * OAuth2.0对于未审核应用有效期为24小时，对于已审核应用有效期最低为30天，不同的应用级别有效期不同.
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
