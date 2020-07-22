package vip.seanxq.weibo.mp.bean.message;

import org.testng.*;
import org.testng.annotations.*;

@Test
public class WeiboMpXmlOutNewsMessageTest {

  public void test() {
    WeiboMpXmlOutNewsMessage m = new WeiboMpXmlOutNewsMessage();
    m.setCreateTime(1122L);
    m.setFromUserName("fromUser");
    m.setToUserName("toUser");

    WeiboMpXmlOutNewsMessage.Item item = new WeiboMpXmlOutNewsMessage.Item();
    item.setDescription("description");
    item.setPicUrl("picUrl");
    item.setTitle("title");
    item.setUrl("url");
    m.addArticle(item);
    m.addArticle(item);
    String expected = "<xml>"
      + "<ToUserName><![CDATA[toUser]]></ToUserName>"
      + "<FromUserName><![CDATA[fromUser]]></FromUserName>"
      + "<CreateTime>1122</CreateTime>"
      + "<MsgType><![CDATA[news]]></MsgType>"
      + "    <ArticleCount>2</ArticleCount>"
      + "    <Articles>"
      + "        <item>"
      + "            <Title><![CDATA[title]]></Title>"
      + "            <Description><![CDATA[description]]></Description>"
      + "            <PicUrl><![CDATA[picUrl]]></PicUrl>"
      + "            <Url><![CDATA[url]]></Url>"
      + "        </item>"
      + "        <item>"
      + "            <Title><![CDATA[title]]></Title>"
      + "            <Description><![CDATA[description]]></Description>"
      + "            <PicUrl><![CDATA[picUrl]]></PicUrl>"
      + "            <Url><![CDATA[url]]></Url>"
      + "        </item>"
      + "    </Articles>"
      + "</xml>";
    System.out.println(m.toXml());
    Assert.assertEquals(m.toXml().replaceAll("\\s", ""), expected.replaceAll("\\s", ""));
  }

  public void testBuild() {
    WeiboMpXmlOutNewsMessage.Item item = new WeiboMpXmlOutNewsMessage.Item();
    item.setDescription("description");
    item.setPicUrl("picUrl");
    item.setTitle("title");
    item.setUrl("url");

    WeiboMpXmlOutNewsMessage m = WeiboMpXmlOutMessage.NEWS()
      .fromUser("fromUser")
      .toUser("toUser")
      .addArticle(item,item)
      .build();
    String expected = "<xml>"
      + "<ToUserName><![CDATA[toUser]]></ToUserName>"
      + "<FromUserName><![CDATA[fromUser]]></FromUserName>"
      + "<CreateTime>1122</CreateTime>"
      + "<MsgType><![CDATA[news]]></MsgType>"
      + "    <Articles>"
      + "        <item>"
      + "            <Title><![CDATA[title]]></Title>"
      + "            <Description><![CDATA[description]]></Description>"
      + "            <PicUrl><![CDATA[picUrl]]></PicUrl>"
      + "            <Url><![CDATA[url]]></Url>"
      + "        </item>"
      + "        <item>"
      + "            <Title><![CDATA[title]]></Title>"
      + "            <Description><![CDATA[description]]></Description>"
      + "            <PicUrl><![CDATA[picUrl]]></PicUrl>"
      + "            <Url><![CDATA[url]]></Url>"
      + "        </item>"
      + "    </Articles>"
      + "    <ArticleCount>2</ArticleCount>"
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
