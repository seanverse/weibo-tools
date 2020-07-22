package vip.seanxq.weibo.mp.api.impl;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import vip.seanxq.weibo.mp.bean.WeiboUserQuery;
import vip.seanxq.weibo.mp.bean.result.WeiboMpChangeOpenid;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUser;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUserList;
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
public class WeiboMpUserServiceImplTest {

  @Inject
  private WeiboMpService wxService;

  private TestConfigStorage configProvider;

  @BeforeTest
  public void setup() {
    this.configProvider = (TestConfigStorage) this.wxService
      .getWxMpConfigStorage();
  }

  public void testUserUpdateRemark() throws WeiboErrorException {
    this.wxService.getUserService()
      .userUpdateRemark(this.configProvider.getOpenid(), "测试备注名");
  }

  public void testUserInfo() throws WeiboErrorException {
    WeiboMpUser user = this.wxService.getUserService()
      .userInfo(this.configProvider.getOpenid(), null);
    Assert.assertNotNull(user);
    System.out.println(user);
  }

  public void testUserInfoList() throws WeiboErrorException {
    List<String> openids = new ArrayList<>();
    openids.add(this.configProvider.getOpenid());
    List<WeiboMpUser> userList = this.wxService.getUserService()
      .userInfoList(openids);
    Assert.assertEquals(userList.size(), 1);
    System.out.println(userList);
  }

  public void testUserInfoListByWxMpUserQuery() throws WeiboErrorException {
    WeiboUserQuery query = new WeiboUserQuery();
    query.add(this.configProvider.getOpenid(), "zh_CN");
    List<WeiboMpUser> userList = this.wxService.getUserService()
      .userInfoList(query);
    Assert.assertEquals(userList.size(), 1);
    System.out.println(userList);
  }

  public void testUserList() throws WeiboErrorException {
    WeiboMpUserList weiboMpUserList = this.wxService.getUserService().userList(null);
    Assert.assertNotNull(weiboMpUserList);
    Assert.assertNotEquals(-1, weiboMpUserList.getCount());
    Assert.assertNotEquals(-1, weiboMpUserList.getTotal());
    Assert.assertNotEquals(-1, weiboMpUserList.getOpenids().size());
    System.out.println(weiboMpUserList);
  }

  public void testChangeOpenid() throws WeiboErrorException {
    List<String> openids = new ArrayList<>();
    openids.add(this.configProvider.getOpenid());
    List<WeiboMpChangeOpenid> weiboMpChangeOpenidList = this.wxService.getUserService().changeOpenid("原公众号appid", openids);
    Assert.assertNotNull(weiboMpChangeOpenidList);
    Assert.assertEquals(1, weiboMpChangeOpenidList.size());
    WeiboMpChangeOpenid weiboMpChangeOpenid = weiboMpChangeOpenidList.get(0);
    Assert.assertNotNull(weiboMpChangeOpenid);
    Assert.assertEquals(this.configProvider.getOpenid(), weiboMpChangeOpenid.getOriOpenid());
    System.out.println(weiboMpChangeOpenid);
  }

  public static class MockTest {
    private WeiboMpService wxService = mock(WeiboMpService.class);

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
      when(wxService.post(USER_CHANGE_OPENID_URL, WbMpGsonBuilder.create().toJson(map))).thenReturn(returnJson);
      List<WeiboMpChangeOpenid> weiboMpChangeOpenidList = this.wxService.getUserService().changeOpenid(fromAppid, openids);
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
