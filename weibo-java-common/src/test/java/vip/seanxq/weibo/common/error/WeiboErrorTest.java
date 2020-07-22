package vip.seanxq.weibo.common.error;

import vip.seanxq.weibo.common.WeiboType;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

@Test
public class WeiboErrorTest {
  public void testFromJson() {
    String json = "{ \"errcode\": 40003, \"errmsg\": \"invalid openid\" }";
    WeiboError weiboError = WeiboError.fromJson(json, WeiboType.MP);
    assertEquals(40003, weiboError.getErrorCode());
    assertEquals(weiboError.getErrorMsgEn(), "invalid openid");

  }

  public void testFromBadJson1() {
    String json = "{ \"errcode\": 40003, \"errmsg\": \"invalid openid\", \"media_id\": \"12323423dsfafsf232f\" }";
    WeiboError weiboError = WeiboError.fromJson(json, WeiboType.MP);
    assertEquals(40003, weiboError.getErrorCode());
    assertEquals(weiboError.getErrorMsgEn(), "invalid openid");

  }

  public void testFromBadJson2() {
    String json = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200}";
    WeiboError weiboError = WeiboError.fromJson(json, WeiboType.MP);
    assertEquals(0, weiboError.getErrorCode());
    assertNull(weiboError.getErrorMsg());

  }

}
