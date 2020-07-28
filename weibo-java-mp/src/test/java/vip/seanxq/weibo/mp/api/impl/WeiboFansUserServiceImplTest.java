package vip.seanxq.weibo.mp.api.impl;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUserList;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;
import org.testng.Assert;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static vip.seanxq.weibo.mp.enums.WeiboMpApiUrl.User.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * 测试用户相关的接口
 *
 * @author chanjarster
 * @author Binary Wang
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WeiboFansUserServiceImplTest {

  @Inject
  private WeiboMpService wxService;

  private TestConfigStorage configProvider;

  @BeforeTest
  public void setup() {
    this.configProvider = (TestConfigStorage) this.wxService
      .getWxMpConfigStorage();
  }



  public void testUserList() throws WeiboErrorException {
    WeiboFansUserList weiboFansUserList = this.wxService.getUserService().userList(null);
    Assert.assertNotNull(weiboFansUserList);
    Assert.assertNotEquals(-1, weiboFansUserList.getCount());
    Assert.assertNotEquals(-1, weiboFansUserList.getTotal());
    Assert.assertNotEquals(-1, weiboFansUserList.getUids().size());
    System.out.println(weiboFansUserList);
  }



  public static class MockTest {
    private WeiboMpService weiboMpService = mock(WeiboMpService.class);

    @Test
    public void testMockChangeOpenid() throws WeiboErrorException {
      List<String> openids = new ArrayList<>();
      openids.add("oEmYbwN-n24jxvk4Sox81qedINkQ");
      openids.add("oEmYbwH9uVd4RKJk7ZZg6SzL6tTo");
      String fromAppid = "old_appid";
      Map<String, Object> map = new HashMap<>();
      map.put("from_appid", fromAppid);
      map.put("openid_list", openids);

      String returnJson = "{\"errcode\": 0,\"errmsg\": \"ok\",\"result_list\": [{\"ori_openid\": \"oEmYbwN-n24jxvk4Sox81qedINkQ\",\"new_openid\": \"o2FwqwI9xCsVadFah_HtpPfaR-X4\",\"err_msg\": \"ok\"},{\"ori_openid\": \"oEmYbwH9uVd4RKJk7ZZg6SzL6tTo\",\"err_msg\": \"ori_openid error\"}]}";
      when(weiboMpService.post(USER_CHANGE_OPENID_URL, WbMpGsonBuilder.create().toJson(map))).thenReturn(returnJson);
      List<WeiboMpChangeOpenid> weiboMpChangeOpenidList = this.weiboMpService.getUserService().changeOpenid(fromAppid, openids);
      Assert.assertNotNull(weiboMpChangeOpenidList);
      Assert.assertEquals(2, weiboMpChangeOpenidList.size());
      WeiboMpChangeOpenid weiboMpChangeOpenid = weiboMpChangeOpenidList.get(0);
      Assert.assertNotNull(weiboMpChangeOpenid);
      Assert.assertEquals("oEmYbwN-n24jxvk4Sox81qedINkQ", weiboMpChangeOpenid.getOriOpenid());
      Assert.assertEquals("o2FwqwI9xCsVadFah_HtpPfaR-X4", weiboMpChangeOpenid.getNewOpenid());
      Assert.assertEquals("ok", weiboMpChangeOpenid.getErrMsg());
      weiboMpChangeOpenid = weiboMpChangeOpenidList.get(1);
      Assert.assertNotNull(weiboMpChangeOpenid);
      Assert.assertEquals("oEmYbwH9uVd4RKJk7ZZg6SzL6tTo", weiboMpChangeOpenid.getOriOpenid());
      Assert.assertNull(weiboMpChangeOpenid.getNewOpenid());
      Assert.assertEquals("ori_openid error", weiboMpChangeOpenid.getErrMsg());
      System.out.println(weiboMpChangeOpenid);
    }

  }

}
