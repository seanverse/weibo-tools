package vip.seanxq.weibo.common.bean;

import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.bean.menu.WeiboMenuButton;
import org.testng.*;
import org.testng.annotations.*;

@Test
public class WeiboMenuTest {

  @Test(dataProvider = "wxReturnMenu")
  public void testFromJson(String json) {
    WeiboMenu menu = WeiboMenu.fromJson(json);
    Assert.assertEquals(menu.getButtons().size(), 3);
  }

  @Test(dataProvider = "wxPushMenu")
  public void testToJson(String json) {
    WeiboMenu menu = new WeiboMenu();
    WeiboMenuButton button1 = new WeiboMenuButton();
    button1.setType("click");
    button1.setName("今日歌曲");
    button1.setKey("V1001_TODAY_MUSIC");

    WeiboMenuButton button2 = new WeiboMenuButton();
    button2.setType("click");
    button2.setName("歌手简介");
    button2.setKey("V1001_TODAY_SINGER");

    WeiboMenuButton button3 = new WeiboMenuButton();
    button3.setName("菜单");

    menu.getButtons().add(button1);
    menu.getButtons().add(button2);
    menu.getButtons().add(button3);

    WeiboMenuButton button31 = new WeiboMenuButton();
    button31.setType("view");
    button31.setName("搜索");
    button31.setUrl("http://www.soso.com/");

    WeiboMenuButton button32 = new WeiboMenuButton();
    button32.setType("view");
    button32.setName("视频");
    button32.setUrl("http://v.qq.com/");

    WeiboMenuButton button33 = new WeiboMenuButton();
    button33.setType("click");
    button33.setName("赞一下我们");
    button33.setKey("V1001_GOOD");

    button3.getSubButtons().add(button31);
    button3.getSubButtons().add(button32);
    button3.getSubButtons().add(button33);

    Assert.assertEquals(menu.toJson(), json);
  }

  @Test(dataProvider = "wxAddConditionalMenu")
  public void testAddConditionalToJson(String json) {
    WeiboMenu menu = new WeiboMenu();
    WeiboMenuButton button1 = new WeiboMenuButton();
    button1.setType("click");
    button1.setName("今日歌曲");
    button1.setKey("V1001_TODAY_MUSIC");

    menu.getButtons().add(button1);

    WeiboMenuRule weiboMenuRule = new WeiboMenuRule();
    weiboMenuRule.setTagId("2");
    weiboMenuRule.setSex("1");
    weiboMenuRule.setCountry("中国");
    weiboMenuRule.setProvince("广东");
    weiboMenuRule.setCity("广州");
    weiboMenuRule.setClientPlatformType("2");
    weiboMenuRule.setLanguage("zh_CN");
    menu.setMatchRule(weiboMenuRule);

    Assert.assertEquals(menu.toJson(), json);
  }

  @DataProvider
  public Object[][] wxReturnMenu() {
    Object[][] res = menuJson();
    String json = "{ \"menu\" : " + res[0][0] + " }";
    return new Object[][]{
      new Object[]{json}
    };
  }

  @DataProvider(name = "wxPushMenu")
  public Object[][] menuJson() {
    String json =
      "{"
        + "\"button\":["
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"今日歌曲\","
        + "\"key\":\"V1001_TODAY_MUSIC\""
        + "},"
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"歌手简介\","
        + "\"key\":\"V1001_TODAY_SINGER\""
        + "},"
        + "{"
        + "\"name\":\"菜单\","
        + "\"sub_button\":["
        + "{"
        + "\"type\":\"view\","
        + "\"name\":\"搜索\","
        + "\"url\":\"http://www.soso.com/\""
        + "},"
        + "{"
        + "\"type\":\"view\","
        + "\"name\":\"视频\","
        + "\"url\":\"http://v.qq.com/\""
        + "},"
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"赞一下我们\","
        + "\"key\":\"V1001_GOOD\""
        + "}"
        + "]"
        + "}"
        + "]"
        + "}";
    return new Object[][]{
      new Object[]{json}
    };
  }

  @DataProvider(name = "wxAddConditionalMenu")
  public Object[][] addConditionalMenuJson() {
    String json =
      "{"
        + "\"button\":["
        + "{"
        + "\"type\":\"click\","
        + "\"name\":\"今日歌曲\","
        + "\"key\":\"V1001_TODAY_MUSIC\""
        + "}"
        + "],"
        + "\"matchrule\":{"
        + "\"group_id\":\"2\","
        + "\"sex\":\"1\","
        + "\"country\":\"中国\","
        + "\"province\":\"广东\","
        + "\"city\":\"广州\","
        + "\"client_platform_type\":\"2\","
        + "\"language\":\"zh_CN\""
        + "}"
        + "}";
    return new Object[][]{
      new Object[]{json}
    };
  }

}
