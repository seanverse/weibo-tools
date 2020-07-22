package vip.seanxq.weibo.common.bean;

import org.testng.*;
import org.testng.annotations.*;

@Test
public class WeiboAccessTokenTest {

  public void testFromJson() {

    String json = "{\"access_token\":\"ACCESS_TOKEN\",\"expires_in\":7200}";
    WeiboAccessToken wxError = WeiboAccessToken.fromJson(json);
    Assert.assertEquals(wxError.getAccessToken(), "ACCESS_TOKEN");
    Assert.assertTrue(wxError.getExpiresIn() == 7200);

  }

}
