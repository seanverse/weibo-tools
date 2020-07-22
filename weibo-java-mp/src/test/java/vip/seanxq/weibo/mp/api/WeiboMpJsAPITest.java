package vip.seanxq.weibo.mp.api;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.util.crypto.SHA1;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * 测试jsapi ticket接口
 *
 * @author chanjarster
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WeiboMpJsAPITest {

  @Inject
  protected WeiboMpService wxService;

  public void test() {
    long timestamp = 1419835025L;
    String url = "http://omstest.vmall.com:23568/thirdparty/wechat/vcode/gotoshare?quantity=1&batchName=MATE7";
    String noncestr = "82693e11-b9bc-448e-892f-f5289f46cd0f";
    String jsapiTicket = "bxLdikRXVbTPdHSM05e5u4RbEYQn7pNQMPrfzl8lJNb1foLDa3HIwI3BRMkQmSO_5F64VFa75uURcq6Uz7QHgA";
    String result = SHA1.genWithAmple(
      "jsapi_ticket=" + jsapiTicket,
      "noncestr=" + noncestr,
      "timestamp=" + timestamp,
      "url=" + url
    );

    Assert.assertEquals(result, "c6f04b64d6351d197b71bd23fb7dd2d44c0db486");
  }

}
