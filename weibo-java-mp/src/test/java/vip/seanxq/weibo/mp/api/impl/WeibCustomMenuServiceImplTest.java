package vip.seanxq.weibo.mp.api.impl;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.bean.menu.WeiboMenuButton;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.bean.menu.WeiboMpGetSelfMenuInfoResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import static org.testng.Assert.assertNotNull;

/**
 * 测试菜单
 *
 * @author chanjarster
 * @author Binary Wang
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WeibCustomMenuServiceImplTest {

  @Inject
  protected WeiboMpService wxService;
  private String menuId = null;

  @Test(dataProvider = "menu")
  public void testMenuCreate(WeiboMenu weiboMenu) throws WeiboErrorException {
    System.out.println(weiboMenu.toJson());
    this.wxService.getMenuService().menuCreate(weiboMenu);
  }

  @Test
  public void testMenuTryMatch() throws Exception {
    WeiboMenu menu = this.wxService.getMenuService().menuTryMatch("...");
    System.out.println(menu);
  }

  @Test
  public void testGetSelfMenuInfo() throws Exception {
    WeiboMpGetSelfMenuInfoResult selfMenuInfo = this.wxService.getMenuService().getSelfMenuInfo();
    System.out.println(selfMenuInfo);
  }

  @Test
  public void testCreateConditionalMenu() throws WeiboErrorException {
    String json = "{\n" +
      " 	\"button\":[\n" +
      " 	{	\n" +
      "    	\"type\":\"click\",\n" +
      "    	\"name\":\"今日歌曲\",\n" +
      "     	\"key\":\"V1001_TODAY_MUSIC\" \n" +
      "	},\n" +
      "	{ \n" +
      "		\"name\":\"菜单\",\n" +
      "		\"sub_button\":[\n" +
      "		{	\n" +
      "			\"type\":\"view\",\n" +
      "			\"name\":\"搜索\",\n" +
      "			\"url\":\"http://www.soso.com/\"\n" +
      "		},\n" +
      "		{\n" +
      "			\"type\":\"view\",\n" +
      "			\"name\":\"视频\",\n" +
      "			\"url\":\"http://v.qq.com/\"\n" +
      "		},\n" +
      "		{\n" +
      "			\"type\":\"click\",\n" +
      "			\"name\":\"赞一下我们\",\n" +
      "			\"key\":\"V1001_GOOD\"\n" +
      "		}]\n" +
      " }],\n" +
      "\"matchrule\":{\n" +
      "  \"tag_id\":\"2\",\n" +
      "  \"sex\":\"1\",\n" +
      "  \"country\":\"中国\",\n" +
      "  \"province\":\"广东\",\n" +
      "  \"city\":\"广州\",\n" +
      "  \"client_platform_type\":\"2\",\n" +
      "  \"language\":\"zh_CN\"\n" +
      "  }\n" +
      "}";

    this.menuId = this.wxService.getMenuService().menuCreate(json);
    System.out.println(this.menuId);
  }

  @Test
  public void testMultiCreateConditionalMenu() throws WeiboErrorException {
    String json = "{\n" +
      " 	\"button\":[\n" +
      " 	{	\n" +
      "    	\"type\":\"click\",\n" +
      "    	\"name\":\"今日歌曲\",\n" +
      "     	\"key\":\"V1001_TODAY_MUSIC\" \n" +
      "	},\n" +
      "	{ \n" +
      "		\"name\":\"菜单\",\n" +
      "		\"sub_button\":[\n" +
      "		{	\n" +
      "			\"type\":\"view\",\n" +
      "			\"name\":\"搜索\",\n" +
      "			\"url\":\"http://www.soso.com/\"\n" +
      "		},\n" +
      "		{\n" +
      "			\"type\":\"view\",\n" +
      "			\"name\":\"视频\",\n" +
      "			\"url\":\"http://v.qq.com/\"\n" +
      "		},\n" +
      "		{\n" +
      "			\"type\":\"click\",\n" +
      "			\"name\":\"赞一下我们\",\n" +
      "			\"key\":\"V1001_GOOD\"\n" +
      "		}]\n" +
      " }],\n" +
      "\"matchrule\":{\n" +
      "  \"tag_id\":\"2\",\n" +
      "  \"sex\":\"1\",\n" +
      "  \"country\":\"中国\",\n" +
      "  \"province\":\"广东\",\n" +
      "  \"city\":\"广州\",\n" +
      "  \"client_platform_type\":\"2\",\n" +
      "  \"language\":\"zh_CN\"\n" +
      "  }\n" +
      "}";
    this.menuId = this.wxService.getMenuService().menuCreate(json);
    System.out.println(this.menuId);
  }

  @Test(dependsOnMethods = {"testCreateConditionalMenu"})
  public void testMenuGet_AfterCreateConditionalMenu() throws WeiboErrorException {
    WeiboMpMenu wxMenu = this.wxService.getMenuService().menuGet();
    assertNotNull(wxMenu);
    System.out.println(wxMenu.toJson());
    assertNotNull(wxMenu.getConditionalMenu().get(0).getRule().getTagId());
  }

  @Test(dependsOnMethods = {"testCreateConditionalMenu"})
  public void testDeleteConditionalMenu() throws WeiboErrorException {
    this.wxService.getMenuService().menuDelete(menuId);
  }

  @Test
  public void testCreateMenu_by_json() throws WeiboErrorException {
    String a = "{\n" +
      "  \"button\": [\n" +
      "    {\n" +
      "      \"type\": \"click\",\n" +
      "      \"name\": \"今日歌曲\",\n" +
      "      \"key\": \"V1001_TODAY_MUSIC\"\n" +
      "    },\n" +
      "    {\n" +
      "      \"name\": \"菜单\",\n" +
      "      \"sub_button\": [\n" +
      "        {\n" +
      "          \"type\": \"view\",\n" +
      "          \"name\": \"搜索\",\n" +
      "          \"url\": \"http://www.soso.com/\"\n" +
      "        },\n" +
      "        {\n" +
      "          \"type\": \"miniprogram\",\n" +
      "          \"name\": \"wxa\",\n" +
      "          \"url\": \"http://mp.weixin.qq.com\",\n" +
      "          \"appid\": \"wx286b93c14bbf93aa\",\n" +
      "          \"pagepath\": \"pages/lunar/index.html\"\n" +
      "        },\n" +
      "        {\n" +
      "          \"type\": \"click\",\n" +
      "          \"name\": \"赞一下我们\",\n" +
      "          \"key\": \"V1001_GOOD\"\n" +
      "        }\n" +
      "      ]\n" +
      "    }\n" +
      "  ]\n" +
      "}";

    WeiboMenu menu = WeiboMenu.fromJson(a);
    System.out.println(menu.toJson());
    this.wxService.getMenuService().menuCreate(menu);
  }

  @Test(dependsOnMethods = {"testMenuCreate"})
  public void testMenuGet() throws WeiboErrorException {
    WeiboMpMenu wxMenu = this.wxService.getMenuService().menuGet();
    assertNotNull(wxMenu);
    System.out.println(wxMenu.toJson());
  }

  @Test(dependsOnMethods = {"testMenuGet", "testMenuCreate"})
  public void testMenuDelete() throws WeiboErrorException {
    this.wxService.getMenuService().menuDelete();
  }

  @DataProvider(name = "menu")
  public Object[][] getMenu() {
    WeiboMenu menu = new WeiboMenu();
    WeiboMenuButton button1 = new WeiboMenuButton();
    button1.setType(WeiboConsts.MenuButtonType.CLICK);
    button1.setName("今日歌曲");
    button1.setKey("V1001_TODAY_MUSIC");

//    WeiboMenuButton button2 = new WeiboMenuButton();
//    button2.setType(WeiboConsts.MenuButtonType.MINIPROGRAM);
//    button2.setName("小程序");
//    button2.setAppId("wx286b93c14bbf93aa");
//    button2.setPagePath("pages/lunar/index.html");
//    button2.setUrl("http://mp.weixin.qq.com");

    WeiboMenuButton button3 = new WeiboMenuButton();
    button3.setName("菜单");

    menu.getButtons().add(button1);
//    menu.getButtons().add(button2);
    menu.getButtons().add(button3);

    WeiboMenuButton button31 = new WeiboMenuButton();
    button31.setType(WeiboConsts.MenuButtonType.VIEW);
    button31.setName("搜索");
    button31.setUrl("http://www.soso.com/");

    WeiboMenuButton button32 = new WeiboMenuButton();
    button32.setType(WeiboConsts.MenuButtonType.VIEW);
    button32.setName("视频");
    button32.setUrl("http://v.qq.com/");

    WeiboMenuButton button33 = new WeiboMenuButton();
    button33.setType(WeiboConsts.MenuButtonType.CLICK);
    button33.setName("赞一下我们");
    button33.setKey("V1001_GOOD");

    button3.getSubButtons().add(button31);
    button3.getSubButtons().add(button32);
    button3.getSubButtons().add(button33);

    return new Object[][]{
      new Object[]{
        menu
      }
    };

  }


}
