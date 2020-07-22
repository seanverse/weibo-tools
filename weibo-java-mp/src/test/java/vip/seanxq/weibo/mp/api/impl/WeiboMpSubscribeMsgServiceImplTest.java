package vip.seanxq.weibo.mp.api.impl;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import vip.seanxq.weibo.mp.bean.subscribe.WxMpSubscribeMessage;
import org.testng.Assert;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

/**
 * @author Mklaus
 * @date 2018-01-22 下午2:02
 */
@Guice(modules = ApiTestModule.class)
public class WeiboMpSubscribeMsgServiceImplTest {

  @Inject
  protected WeiboMpService wxService;

  @Test
  public void testSendSubscribeMessage() throws WeiboErrorException {
    TestConfigStorage configStorage = (TestConfigStorage) this.wxService
      .getWxMpConfigStorage();

    WxMpSubscribeMessage message = WxMpSubscribeMessage.builder()
      .title("weixin test")
      .toUser(configStorage.getOpenid())
      .scene("1000")
      .contentColor("#FF0000")
      .contentValue("Send subscribe message test")
      .build();

    try {
      boolean send = this.wxService.getSubscribeMsgService().sendSubscribeMessage(message);
      Assert.assertTrue(send);
    } catch (WeiboErrorException e) {
      // 当用户没有授权，获取之前的授权已使用。微博会返回错误代码 {"errcode":43101,"errmsg":"user refuse to accept the msg hint: [xxxxxxxxxxx]"}
      if (e.getError().getErrorCode() != 43101) {
        throw e;
      }
    }

  }

}
