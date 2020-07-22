package vip.seanxq.weibo.mp.api.impl;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.util.WeiboMpConfigStorageHolder;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * <pre>
 *  Created by BinaryWang on 2019/3/29.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Test
@Guice(modules = ApiTestModule.class)
public class BaseWxMpServiceImplTest {
  @Inject
  private WeiboMpService wxService;

  @Test
  public void testSwitchover() {
    assertTrue(this.wxService.switchover("another"));
    assertThat(WeiboMpConfigStorageHolder.get()).isEqualTo("another");
    assertFalse(this.wxService.switchover("whatever"));
  }

  @Test
  public void testSwitchoverTo() throws WeiboErrorException {
    assertThat(this.wxService.switchoverTo("another").getAccessToken()).isNotEmpty();
    assertThat(WeiboMpConfigStorageHolder.get()).isEqualTo("another");
  }

  @Test
  public void testNetCheck() throws WeiboErrorException {
    WeiboNetCheckResult result = this.wxService.netCheck(WeiboConsts.NetCheckArgs.ACTIONALL, WeiboConsts.NetCheckArgs.OPERATORDEFAULT);
    Assert.assertNotNull(result);

  }

  @Test
  public void testNectCheckResult() {
    String json = "{\n" +
      "    \"dns\": [\n" +
      "        {\n" +
      "            \"ip\": \"111.161.64.40\", \n" +
      "            \"real_operator\": \"UNICOM\"\n" +
      "        }, \n" +
      "        {\n" +
      "            \"ip\": \"111.161.64.48\", \n" +
      "            \"real_operator\": \"UNICOM\"\n" +
      "        }\n" +
      "    ], \n" +
      "    \"ping\": [\n" +
      "        {\n" +
      "            \"ip\": \"111.161.64.40\", \n" +
      "            \"from_operator\": \"UNICOM\"," +
      "            \"package_loss\": \"0%\", \n" +
      "            \"time\": \"23.079ms\"\n" +
      "        }, \n" +
      "        {\n" +
      "            \"ip\": \"111.161.64.48\", \n" +
      "            \"from_operator\": \"UNICOM\", \n" +
      "            \"package_loss\": \"0%\", \n" +
      "            \"time\": \"21.434ms\"\n" +
      "        }\n" +
      "    ]\n" +
      "}";
    WeiboNetCheckResult result = WeiboNetCheckResult.fromJson(json);
    Assert.assertNotNull(result);
    Assert.assertNotNull(result.getDnsInfos());

    WeiboNetCheckResult.WxNetCheckDnsInfo dnsInfo = new WeiboNetCheckResult.WxNetCheckDnsInfo();
    dnsInfo.setIp("111.161.64.40");
    dnsInfo.setRealOperator("UNICOM");
    Assert.assertEquals(result.getDnsInfos().get(0), dnsInfo);

    WeiboNetCheckResult.WxNetCheckPingInfo pingInfo = new WeiboNetCheckResult.WxNetCheckPingInfo();
    pingInfo.setTime("21.434ms");
    pingInfo.setFromOperator("UNICOM");
    pingInfo.setIp("111.161.64.48");
    pingInfo.setPackageLoss("0%");
    Assert.assertEquals(result.getPingInfos().get(1), pingInfo);

  }

  @Test
  public void testGetCallbackIP() throws WeiboErrorException {
    String[] ipArray = this.wxService.getCallbackIP();
    System.out.println(Arrays.toString(ipArray));
    Assert.assertNotNull(ipArray);
    Assert.assertNotEquals(ipArray.length, 0);
  }

  public void testShortUrl() throws WeiboErrorException {
    String shortUrl = this.wxService.shortUrl("http://www.baidu.com/test?access_token=123");
    assertThat(shortUrl).isNotEmpty();
    System.out.println(shortUrl);
  }

  @Test(expectedExceptions = WeiboErrorException.class)
  public void testShortUrl_with_exceptional_url() throws WeiboErrorException {
    this.wxService.shortUrl("http://www.baidu.com/test?redirect_count=1&access_token=123");
  }

  @Test
  public void refreshAccessTokenDuplicatelyTest() throws InterruptedException {
    // 测试多线程刷新accessToken时是否重复刷新
    wxService.getWxMpConfigStorage().expireAccessToken();
    final Set<String> set = Sets.newConcurrentHashSet();
    Runnable r = new Runnable() {
      @Override
      public void run() {
        try {
          String accessToken = wxService.getAccessToken();
          set.add(accessToken);
        } catch (WeiboErrorException e) {
          e.printStackTrace();
        }
      }
    };

    final int threadNumber = 10;
    ExecutorService executorService = Executors.newFixedThreadPool(threadNumber);
    for ( int i = 0; i < threadNumber; i++ ) {
      executorService.submit(r);
    }
    executorService.shutdown();
    boolean isTerminated = executorService.awaitTermination(15, TimeUnit.SECONDS);
    System.out.println("isTerminated: " + isTerminated);
    System.out.println("times of refreshing accessToken: " + set.size());

    assertEquals(set.size(), 1);

  }

  @Test
  public void testCheckSignature() {
  }

  @Test
  public void testGetTicket() {
  }

  @Test
  public void testTestGetTicket() {
  }

  @Test
  public void testGetJsapiTicket() {
  }

  @Test
  public void testTestGetJsapiTicket() {
  }

  @Test
  public void testCreateJsapiSignature() {
  }

  @Test
  public void testGetAccessToken() {
  }

  @Test
  public void testSemanticQuery() {
  }

  @Test
  public void testOauth2buildAuthorizationUrl() {
  }

  @Test
  public void testBuildQrConnectUrl() {
  }

  @Test
  public void testOauth2getAccessToken() {
  }

  @Test
  public void testOauth2refreshAccessToken() {
  }

  @Test
  public void testOauth2getUserInfo() {
  }

  @Test
  public void testOauth2validateAccessToken() {
  }

  @Test
  public void testGetCurrentAutoReplyInfo() {
  }

  @Test
  public void testClearQuota() {
  }

  @Test
  public void testGet() {
  }

  @Test
  public void testTestGet() {
  }

  @Test
  public void testPost() {
  }

  @Test
  public void testTestPost() {
  }

  @Test
  public void testExecute() {
  }

  @Test
  public void testTestExecute() {
  }

  @Test
  public void testExecuteInternal() {
  }

  @Test
  public void testGetWxMpConfigStorage() {
  }

  @Test
  public void testSetWxMpConfigStorage() {
  }

  @Test
  public void testSetMultiConfigStorages() {
  }

  @Test
  public void testTestSetMultiConfigStorages() {
  }

  @Test
  public void testAddConfigStorage() {
  }

  @Test
  public void testRemoveConfigStorage() {
  }

  @Test
  public void testSetRetrySleepMillis() {
  }

  @Test
  public void testSetMaxRetryTimes() {
  }

  @Test
  public void testGetKefuService() {
  }

  @Test
  public void testGetMaterialService() {
  }

  @Test
  public void testGetMenuService() {
  }

  @Test
  public void testGetUserService() {
  }

  @Test
  public void testGetUserTagService() {
  }

  @Test
  public void testGetQrcodeService() {
  }

  @Test
  public void testGetCardService() {
  }

  @Test
  public void testGetDataCubeService() {
  }

  @Test
  public void testGetBlackListService() {
  }

  @Test
  public void testGetStoreService() {
  }

  @Test
  public void testGetTemplateMsgService() {
  }

  @Test
  public void testGetSubscribeMsgService() {
  }

  @Test
  public void testGetDeviceService() {
  }

  @Test
  public void testGetShakeService() {
  }

  @Test
  public void testGetMemberCardService() {
  }

  @Test
  public void testGetRequestHttp() {
  }

  @Test
  public void testGetMassMessageService() {
  }

  @Test
  public void testSetKefuService() {
  }

  @Test
  public void testSetMaterialService() {
  }

  @Test
  public void testSetMenuService() {
  }

  @Test
  public void testSetUserService() {
  }

  @Test
  public void testSetTagService() {
  }

  @Test
  public void testSetQrCodeService() {
  }

  @Test
  public void testSetCardService() {
  }

  @Test
  public void testSetStoreService() {
  }

  @Test
  public void testSetDataCubeService() {
  }

  @Test
  public void testSetBlackListService() {
  }

  @Test
  public void testSetTemplateMsgService() {
  }

  @Test
  public void testSetDeviceService() {
  }

  @Test
  public void testSetShakeService() {
  }

  @Test
  public void testSetMemberCardService() {
  }

  @Test
  public void testSetMassMessageService() {
  }

  @Test
  public void testGetAiOpenService() {
  }

  @Test
  public void testSetAiOpenService() {
  }

  @Test
  public void testGetWifiService() {
  }

  @Test
  public void testGetOcrService() {
  }

  @Test
  public void testGetMarketingService() {
  }

  @Test
  public void testSetMarketingService() {
  }

  @Test
  public void testSetOcrService() {
  }

  @Test
  public void testGetCommentService() {
  }

  @Test
  public void testSetCommentService() {
  }

  @Test
  public void testGetImgProcService() {
  }

  @Test
  public void testSetImgProcService() {
  }
}
