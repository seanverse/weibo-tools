package vip.seanxq.weibo.mp.api.impl;

import com.google.inject.Inject;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import vip.seanxq.weibo.mp.bean.tag.WeiboUserTag;
import org.testng.*;
import org.testng.annotations.*;

import java.util.List;

/**
 * Created by Binary Wang on 2016/9/2.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WeiboFansTagServiceImplTest {
  @Inject
  protected WeiboMpService wxService;

  private Long tagId = 2L;

  @Test
  public void testTagCreate() throws Exception {
    String tagName = "测试标签" + System.currentTimeMillis();
    WeiboUserTag res = this.wxService.getUserTagService().tagCreate(tagName);
    System.out.println(res);
    this.tagId = res.getId();
    Assert.assertEquals(tagName, res.getName());
  }

  @Test
  public void testTagGet() throws Exception {
    List<WeiboUserTag> res = this.wxService.getUserTagService().tagGet();
    System.out.println(res);
    Assert.assertNotNull(res);
  }

  @Test(dependsOnMethods = {"testTagCreate"})
  public void testTagUpdate() throws Exception {
    String tagName = "修改标签" + System.currentTimeMillis();
    Boolean res = this.wxService.getUserTagService().tagUpdate(this.tagId, tagName);
    System.out.println(res);
    Assert.assertTrue(res);
  }

  @Test(dependsOnMethods = {"testTagCreate"})
  public void testTagDelete() throws Exception {
    Boolean res = this.wxService.getUserTagService().tagDelete(this.tagId);
    System.out.println(res);
    Assert.assertTrue(res);
  }

  @Test
  public void testTagListUser() throws Exception {
    WeiboTagListUser res = this.wxService.getUserTagService().tagListUser(this.tagId, null);
    System.out.println(res);
    Assert.assertNotNull(res);
  }

  @Test
  public void testBatchTagging() throws Exception {
    String[] openids = new String[]{((TestConfigStorage) this.wxService.getWxMpConfigStorage()).getOpenid()};
    boolean res = this.wxService.getUserTagService().batchTagging(this.tagId, openids);
    System.out.println(res);
    Assert.assertTrue(res);
  }

  @Test
  public void testBatchUntagging() throws Exception {
    String[] openids = new String[]{((TestConfigStorage) this.wxService.getWxMpConfigStorage()).getOpenid()};
    boolean res = this.wxService.getUserTagService().batchUntagging(this.tagId, openids);
    System.out.println(res);
    Assert.assertTrue(res);
  }

  @Test
  public void testUserTagList() throws Exception {
    List<Long> res = this.wxService.getUserTagService().userTagList(
      ((TestConfigStorage) this.wxService.getWxMpConfigStorage()).getOpenid());
    System.out.println(res);
    Assert.assertNotNull(res);
  }
}
