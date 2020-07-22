package vip.seanxq.weibo.mp.api.impl;

import org.apache.commons.lang3.StringUtils;
import org.testng.*;
import org.testng.annotations.*;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import vip.seanxq.weibo.mp.bean.result.WeiboMpCurrentAutoReplyInfo;
import vip.seanxq.weibo.common.enums.TicketType;

import static org.testng.Assert.*;

@Test
@Guice(modules = ApiTestModule.class)
public class WeiboMpServiceImplTest {
  @Inject
  private WeiboMpService wxService;

  @Test
  public void testGetCurrentAutoReplyInfo() throws WeiboErrorException {
    WeiboMpCurrentAutoReplyInfo autoReplyInfo = this.wxService.getCurrentAutoReplyInfo();

    assertNotNull(autoReplyInfo);
    System.out.println(autoReplyInfo);
  }

  @Test
  public void testClearQuota() throws WeiboErrorException {
    this.wxService.clearQuota(wxService.getWxMpConfigStorage().getAppId());
  }

  @Test
  public void testBuildQrConnectUrl() {
    String qrconnectRedirectUrl = ((TestConfigStorage) this.wxService.getWxMpConfigStorage()).getQrconnectRedirectUrl();
    String qrConnectUrl = this.wxService.buildQrConnectUrl(qrconnectRedirectUrl,
      WeiboConsts.QrConnectScope.SNSAPI_LOGIN, null);
    Assert.assertNotNull(qrConnectUrl);
    System.out.println(qrConnectUrl);
  }

  public void testGetTicket() throws WeiboErrorException {
    String ticket = this.wxService.getTicket(TicketType.SDK, false);
    System.out.println(ticket);
    Assert.assertNotNull(ticket);
  }

  public void testRefreshAccessToken() throws WeiboErrorException {
    WeiboConfigStorage configStorage = this.wxService.getWxMpConfigStorage();
    String before = configStorage.getAccessToken();
    this.wxService.getAccessToken(false);

    String after = configStorage.getAccessToken();
    Assert.assertNotEquals(before, after);
    Assert.assertTrue(StringUtils.isNotBlank(after));
  }
}
