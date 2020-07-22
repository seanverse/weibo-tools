package vip.seanxq.weibo.mp.demo;

import java.io.InputStream;
import java.util.concurrent.locks.ReentrantLock;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import vip.seanxq.weibo.common.util.xml.XStreamInitializer;
import vip.seanxq.weibo.mp.config.impl.WeiboDefaultConfigImpl;

/**
 * @author Daniel Qian
 */
@XStreamAlias("xml")
class WeiboDemoInMemoryConfigStorage extends WeiboDefaultConfigImpl {
  private static final long serialVersionUID = -3706236839197109704L;

  public static WeiboDemoInMemoryConfigStorage fromXml(InputStream is) {
    XStream xstream = XStreamInitializer.getInstance();
    xstream.processAnnotations(WeiboDemoInMemoryConfigStorage.class);
    WeiboDemoInMemoryConfigStorage wxMpDemoInMemoryConfigStorage = (WeiboDemoInMemoryConfigStorage) xstream.fromXML(is);
    wxMpDemoInMemoryConfigStorage.accessTokenLock = new ReentrantLock();
    wxMpDemoInMemoryConfigStorage.cardApiTicketLock = new ReentrantLock();
    wxMpDemoInMemoryConfigStorage.jsapiTicketLock = new ReentrantLock();
    return wxMpDemoInMemoryConfigStorage;
  }

}
