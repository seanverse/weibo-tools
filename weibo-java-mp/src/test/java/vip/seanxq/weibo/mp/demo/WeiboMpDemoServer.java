package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.mp.api.WeiboFansMessageRouter;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.mp.api.WeiboFansMessageHandler;
import vip.seanxq.weibo.mp.api.impl.WeiboMpServiceHttpClientImpl;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.io.IOException;
import java.io.InputStream;

public class WeiboMpDemoServer {

  private static WeiboConfigStorage weiboConfigStorage;
  private static WeiboMpService wbMpService;
  private static WeiboFansMessageRouter weiboFansMessageRouter;

  public static void main(String[] args) throws Exception {
    initWeibo();

    Server server = new Server(8080);

    ServletHandler servletHandler = new ServletHandler();
    server.setHandler(servletHandler);

    ServletHolder endpointServletHolder = new ServletHolder(new WbMpEndpointServlet(weiboConfigStorage, wbMpService,
      weiboFansMessageRouter));
    servletHandler.addServletWithMapping(endpointServletHolder, "/*");

    ServletHolder oauthServletHolder = new ServletHolder(new WbMpOAuth2Servlet(wbMpService));
    servletHandler.addServletWithMapping(oauthServletHolder, "/oauth2/*");

    server.start();
    server.join();
  }

  private static void initWeibo() {
    try (InputStream is1 = ClassLoader.getSystemResourceAsStream("test-config.xml")) {
      WeiboDemoInMemoryConfigStorage config = WeiboDemoInMemoryConfigStorage.fromXml(is1);

      weiboConfigStorage = config;
      wbMpService = new WeiboMpServiceHttpClientImpl();
      wbMpService.setWxMpConfigStorage(config);

      WeiboFansMessageHandler logHandler = new DemoLogHandler();
      WeiboFansMessageHandler textHandler = new DemoTextHandler();
      WeiboFansMessageHandler imageHandler = new DemoImageHandler();
      WeiboFansMessageHandler oauth2handler = new DemoOAuth2Handler();

      weiboFansMessageRouter = new WeiboFansMessageRouter(wbMpService);
      weiboFansMessageRouter.rule().handler(logHandler).next().rule()
        .handler(textHandler).end().rule().async(false).content("图片")
        .handler(imageHandler).end().rule().async(false).content("oauth")
        .handler(oauth2handler).end();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
