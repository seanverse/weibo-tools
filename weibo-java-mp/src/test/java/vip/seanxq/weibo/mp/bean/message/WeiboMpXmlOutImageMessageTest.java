package vip.seanxq.weibo.mp.bean.message;

import org.testng.*;
import org.testng.annotations.*;

@Test
public class WeiboMpXmlOutImageMessageTest {

  public void test() {
    WeiboMpXmlOutImageMessage m = new WeiboMpXmlOutImageMessage();
    m.setMediaId("ddfefesfsdfef");
    m.setCreateTime(1122L);
    m.setFromUserName("from");
    m.setToUserName("to");

    String expected = "<xml>"
      + "<ToUserName><![CDATA[to]]></ToUserName>"
      + "<FromUserName><![CDATA[from]]></FromUserName>"
      + "<CreateTime>1122</CreateTime>"
      + "<MsgType><![CDATA[image]]></MsgType>"
      + "<Image><MediaId><![CDATA[ddfefesfsdfef]]></MediaId></Image>"
      + "</xml>";
    System.out.println(m.toXml());
    Assert.assertEquals(m.toXml().replaceAll("\\s", ""), expected.replaceAll("\\s", ""));
  }

  public void testBuild() {
    WeiboMpXmlOutImageMessage m = WeiboMpXmlOutMessage.IMAGE().mediaId("ddfefesfsdfef").fromUser("from").toUser("to").build();
    String expected = "<xml>"
      + "<ToUserName><![CDATA[to]]></ToUserName>"
      + "<FromUserName><![CDATA[from]]></FromUserName>"
      + "<CreateTime>1122</CreateTime>"
      + "<MsgType><![CDATA[image]]></MsgType>"
      + "<Image><MediaId><![CDATA[ddfefesfsdfef]]></MediaId></Image>"
      + "</xml>";
    System.out.println(m.toXml());
    Assert.assertEquals(
      m
        .toXml()
        .replaceAll("\\s", "")
        .replaceAll("<CreateTime>.*?</CreateTime>", ""),
      expected
        .replaceAll("\\s", "")
        .replaceAll("<CreateTime>.*?</CreateTime>", "")
    );

  }
}
