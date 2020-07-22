package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.session.StandardSessionManager;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;
import org.testng.*;
import org.testng.annotations.*;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.Map;

/**
 * 测试消息路由器
 *
 * @author chanjarster
 */
@Test
public class WeibMpMessageRouterTest {

  @Test(enabled = false)
  public void prepare(boolean async, StringBuffer sb, WeibMpMessageRouter router) {
    router
      .rule()
      .async(async)
      .msgType(WeiboConsts.XmlMsgType.TEXT).event(WeiboConsts.EventType.CLICK).eventKey("KEY_1").content("CONTENT_1")
      .handler(new WeibEchoMpMessageHandler(sb, "COMBINE_4"))
      .end()
      .rule()
      .async(async)
      .msgType(WeiboConsts.XmlMsgType.TEXT).event(WeiboConsts.EventType.CLICK).eventKey("KEY_1")
      .handler(new WeibEchoMpMessageHandler(sb, "COMBINE_3"))
      .end()
      .rule()
      .async(async)
      .msgType(WeiboConsts.XmlMsgType.TEXT).event(WeiboConsts.EventType.CLICK)
      .handler(new WeibEchoMpMessageHandler(sb, "COMBINE_2"))
      .end()
      .rule().async(async).msgType(WeiboConsts.XmlMsgType.TEXT).handler(new WeibEchoMpMessageHandler(sb, WeiboConsts.XmlMsgType.TEXT)).end()
      .rule().async(async).event(WeiboConsts.EventType.CLICK).handler(new WeibEchoMpMessageHandler(sb, WeiboConsts.EventType.CLICK)).end()
      .rule().async(async).eventKey("KEY_1").handler(new WeibEchoMpMessageHandler(sb, "KEY_1")).end()
      .rule().async(async).eventKeyRegex("KEY_1*").handler(new WeibEchoMpMessageHandler(sb, "KEY_123")).end()
      .rule().async(async).content("CONTENT_1").handler(new WeibEchoMpMessageHandler(sb, "CONTENT_1")).end()
      .rule().async(async).rContent(".*bc.*").handler(new WeibEchoMpMessageHandler(sb, "abcd")).end()
      .rule().async(async).matcher(new WeibMpMessageMatcher() {
      @Override
      public boolean match(WeiboMpXmlMessage message) {
        return "strangeformat".equals(message.getFormat());
      }
    }).handler(new WeibEchoMpMessageHandler(sb, "matcher")).end()
      .rule().async(async).handler(new WeibEchoMpMessageHandler(sb, "ALL")).end();
  }

  @Test(dataProvider = "messages-1")
  public void testSync(WeiboMpXmlMessage message, String expected) {
    StringBuffer sb = new StringBuffer();
    WeibMpMessageRouter router = new WeibMpMessageRouter(null);
    prepare(false, sb, router);
    router.route(message);
    Assert.assertEquals(sb.toString(), expected);
  }

  @Test(dataProvider = "messages-1")
  public void testAsync(WeiboMpXmlMessage message, String expected) throws InterruptedException {
    StringBuffer sb = new StringBuffer();
    WeibMpMessageRouter router = new WeibMpMessageRouter(null);
    prepare(true, sb, router);
    router.route(message);
    Thread.sleep(500);
    router.shutDownExecutorService();
    Assert.assertEquals(sb.toString(), expected);
  }

  @Test(dataProvider = "messages-1")
  public void testExternalExcutorService(WeiboMpXmlMessage message, String expected) throws InterruptedException {
    StringBuffer sb = new StringBuffer();
    ExecutorService executorService = Executors.newFixedThreadPool(100);
    WeibMpMessageRouter router = new WeibMpMessageRouter(null, executorService);
    prepare(true, sb, router);
    router.route(message);
    Thread.sleep(500);
    executorService.shutdown();
    Assert.assertEquals(sb.toString(), expected);
  }


  public void testConcurrency() throws InterruptedException {
    final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
    router.rule().handler(new WeibMpMessageHandler() {
      @Override
      public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage, Map<String, Object> context, WeiboMpService wxMpService,
                                         WeiboSessionManager sessionManager) {
        return null;
      }
    }).end();

    final WeiboMpXmlMessage m = new WeiboMpXmlMessage();
    Runnable r = new Runnable() {
      @Override
      public void run() {
        router.route(m);
        try {
          Thread.sleep(1000);
        } catch (InterruptedException e) {
        }
      }
    };
    for (int i = 0; i < 10; i++) {
      new Thread(r).start();
    }

    Thread.sleep(2000);
  }

  @DataProvider(name = "messages-1")
  public Object[][] messages2() {
    WeiboMpXmlMessage message1 = new WeiboMpXmlMessage();
    message1.setMsgType(WeiboConsts.XmlMsgType.TEXT);

    WeiboMpXmlMessage message2 = new WeiboMpXmlMessage();
    message2.setEvent(WeiboConsts.EventType.CLICK);

    WeiboMpXmlMessage message3 = new WeiboMpXmlMessage();
    message3.setEventKey("KEY_1");

    WeiboMpXmlMessage message4 = new WeiboMpXmlMessage();
    message4.setContent("CONTENT_1");

    WeiboMpXmlMessage message5 = new WeiboMpXmlMessage();
    message5.setContent("BLA");

    WeiboMpXmlMessage message6 = new WeiboMpXmlMessage();
    message6.setContent("abcd");

    WeiboMpXmlMessage message7 = new WeiboMpXmlMessage();
    message7.setFormat("strangeformat");

    WeiboMpXmlMessage c2 = new WeiboMpXmlMessage();
    c2.setMsgType(WeiboConsts.XmlMsgType.TEXT);
    c2.setEvent(WeiboConsts.EventType.CLICK);

    WeiboMpXmlMessage c3 = new WeiboMpXmlMessage();
    c3.setMsgType(WeiboConsts.XmlMsgType.TEXT);
    c3.setEvent(WeiboConsts.EventType.CLICK);
    c3.setEventKey("KEY_1");

    WeiboMpXmlMessage c4 = new WeiboMpXmlMessage();
    c4.setMsgType(WeiboConsts.XmlMsgType.TEXT);
    c4.setEvent(WeiboConsts.EventType.CLICK);
    c4.setEventKey("KEY_1");
    c4.setContent("CONTENT_1");

    return new Object[][]{
      new Object[]{message1, WeiboConsts.XmlMsgType.TEXT + ","},
      new Object[]{message2, WeiboConsts.EventType.CLICK + ","},
      new Object[]{message3, "KEY_1,"},
      new Object[]{message4, "CONTENT_1,"},
      new Object[]{message5, "ALL,"},
      new Object[]{message6, "abcd,"},
      new Object[]{message7, "matcher,"},
      new Object[]{c2, "COMBINE_2,"},
      new Object[]{c3, "COMBINE_3,"},
      new Object[]{c4, "COMBINE_4,"}
    };

  }

  @DataProvider
  public Object[][] standardSessionManager() {

    // 故意把session存活时间变短，清理更频繁
    StandardSessionManager ism = new StandardSessionManager();
    ism.setMaxInactiveInterval(1);
    ism.setProcessExpiresFrequency(1);
    ism.setBackgroundProcessorDelay(1);

    return new Object[][]{
      new Object[]{ism}
    };

  }

  @Test(dataProvider = "standardSessionManager")
  public void testSessionClean1(StandardSessionManager ism) throws InterruptedException {

    // 两个同步请求，看是否处理完毕后会被清理掉
    final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
    router.setSessionManager(ism);
    router
      .rule().async(false).handler(new WeibSessionMessageHandler()).next()
      .rule().async(false).handler(new WeibSessionMessageHandler()).end();

    WeiboMpXmlMessage msg = new WeiboMpXmlMessage();
    msg.setFromUser("abc");
    router.route(msg);

    Thread.sleep(2000);
    Assert.assertEquals(ism.getActiveSessions(), 0);

  }

  @Test(dataProvider = "standardSessionManager")
  public void testSessionClean2(StandardSessionManager ism) throws InterruptedException {

    // 1个同步,1个异步请求，看是否处理完毕后会被清理掉
    {
      final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
      router.setSessionManager(ism);
      router
        .rule().async(false).handler(new WeibSessionMessageHandler()).next()
        .rule().async(true).handler(new WeibSessionMessageHandler()).end();

      WeiboMpXmlMessage msg = new WeiboMpXmlMessage();
      msg.setFromUser("abc");
      router.route(msg);

      Thread.sleep(2000);
      Assert.assertEquals(ism.getActiveSessions(), 0);
    }
    {
      final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
      router.setSessionManager(ism);
      router
        .rule().async(true).handler(new WeibSessionMessageHandler()).next()
        .rule().async(false).handler(new WeibSessionMessageHandler()).end();

      WeiboMpXmlMessage msg = new WeiboMpXmlMessage();
      msg.setFromUser("abc");
      router.route(msg);

      Thread.sleep(2000);
      Assert.assertEquals(ism.getActiveSessions(), 0);
    }

  }

  @Test(dataProvider = "standardSessionManager")
  public void testSessionClean3(StandardSessionManager ism) throws InterruptedException {

    // 2个异步请求，看是否处理完毕后会被清理掉
    final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
    router.setSessionManager(ism);
    router
      .rule().async(true).handler(new WeibSessionMessageHandler()).next()
      .rule().async(true).handler(new WeibSessionMessageHandler()).end();

    WeiboMpXmlMessage msg = new WeiboMpXmlMessage();
    msg.setFromUser("abc");
    router.route(msg);

    Thread.sleep(2000);
    Assert.assertEquals(ism.getActiveSessions(), 0);

  }

  @Test(dataProvider = "standardSessionManager")
  public void testSessionClean4(StandardSessionManager ism) throws InterruptedException {

    // 一个同步请求，看是否处理完毕后会被清理掉
    {
      final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
      router.setSessionManager(ism);
      router
        .rule().async(false).handler(new WeibSessionMessageHandler()).end();

      WeiboMpXmlMessage msg = new WeiboMpXmlMessage();
      msg.setFromUser("abc");
      router.route(msg);

      Thread.sleep(2000);
      Assert.assertEquals(ism.getActiveSessions(), 0);
    }

    {
      final WeibMpMessageRouter router = new WeibMpMessageRouter(null);
      router.setSessionManager(ism);
      router
        .rule().async(true).handler(new WeibSessionMessageHandler()).end();

      WeiboMpXmlMessage msg = new WeiboMpXmlMessage();
      msg.setFromUser("abc");
      router.route(msg);

      Thread.sleep(2000);
      Assert.assertEquals(ism.getActiveSessions(), 0);
    }
  }

  public static class WeibEchoMpMessageHandler implements WeibMpMessageHandler {

    private StringBuffer sb;
    private String echoStr;

    public WeibEchoMpMessageHandler(StringBuffer sb, String echoStr) {
      this.sb = sb;
      this.echoStr = echoStr;
    }

    @Override
    public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage, Map<String, Object> context, WeiboMpService wxMpService,
                                       WeiboSessionManager sessionManager) {
      this.sb.append(this.echoStr).append(',');
      return null;
    }

  }

  public static class WeibSessionMessageHandler implements WeibMpMessageHandler {

    @Override
    public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage, Map<String, Object> context, WeiboMpService wxMpService,
                                       WeiboSessionManager sessionManager) {
      sessionManager.getSession(wxMessage.getFromUser());
      return null;
    }

  }

}
