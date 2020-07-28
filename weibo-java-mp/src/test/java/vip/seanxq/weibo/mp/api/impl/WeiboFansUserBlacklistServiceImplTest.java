package vip.seanxq.weibo.mp.api.impl;

import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import org.testng.*;
import org.testng.annotations.*;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * @author miller
 */
@Test(groups = "userAPI")
@Guice(modules = ApiTestModule.class)
public class WeiboFansUserBlacklistServiceImplTest {
  @Inject
  protected WeiboMpService wxService;

  @Test
  public void testGetBlacklist() throws Exception {
    TestConfigStorage configStorage = (TestConfigStorage) this.wxService
      .getWxMpConfigStorage();
    WeiboMpUserBlacklistGetResult weiboMpUserBlacklistGetResult = this.wxService.getBlackListService().getBlacklist(configStorage.getOpenid());
    Assert.assertNotNull(weiboMpUserBlacklistGetResult);
    Assert.assertFalse(weiboMpUserBlacklistGetResult.getCount() == -1);
    Assert.assertFalse(weiboMpUserBlacklistGetResult.getTotal() == -1);
    Assert.assertFalse(weiboMpUserBlacklistGetResult.getOpenidList().size() == -1);
    System.out.println(weiboMpUserBlacklistGetResult);
  }

  @Test
  public void testPushToBlacklist() throws Exception {
    TestConfigStorage configStorage = (TestConfigStorage) this.wxService
      .getWxMpConfigStorage();
    List<String> openidList = new ArrayList<>();
    openidList.add(configStorage.getOpenid());
    this.wxService.getBlackListService().pushToBlacklist(openidList);
  }

  @Test
  public void testPullFromBlacklist() throws Exception {
    TestConfigStorage configStorage = (TestConfigStorage) this.wxService
      .getWxMpConfigStorage();
    List<String> openidList = new ArrayList<>();
    openidList.add(configStorage.getOpenid());
    this.wxService.getBlackListService().pullFromBlacklist(openidList);
  }

}
