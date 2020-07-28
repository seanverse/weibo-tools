package vip.seanxq.weibo.mp.api.impl;

import com.google.inject.Inject;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.bean.result.WeiboMediaUploadResult;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.ApiTestModule;
import vip.seanxq.weibo.mp.api.test.TestConfigStorage;
import vip.seanxq.weibo.mp.api.test.TestConstants;
import vip.seanxq.weibo.mp.bean.mass.WeiboMpMassNews;
import vip.seanxq.weibo.mp.bean.mass.WeiboMassOpenIdsMessage;
import vip.seanxq.weibo.mp.bean.mass.WeiboMpMassTagMessage;
import vip.seanxq.weibo.mp.bean.mass.WeiboMpMassVideo;
import vip.seanxq.weibo.mp.bean.material.WeiboMpNewsArticle;
import vip.seanxq.weibo.mp.bean.result.WeiboMpMassSendResult;
import vip.seanxq.weibo.mp.bean.result.WeiboMpMassUploadResult;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Guice;
import org.testng.annotations.Test;

import java.io.IOException;
import java.io.InputStream;

import static org.testng.Assert.assertNotNull;

/**
 * 测试群发消息
 *
 * @author chanjarster
 */
@Test
@Guice(modules = ApiTestModule.class)
public class WeibFansMassMessageServiceImplTest {
  @Inject
  protected WeiboMpService wxService;

  @Test
  public void testTextMassOpenIdsMessageSend() throws WeiboErrorException {
    // 发送群发消息
    TestConfigStorage configProvider = (TestConfigStorage) this.wxService      .getWxMpConfigStorage();
    WeiboMassOpenIdsMessage massMessage = new WeiboMassOpenIdsMessage();
    massMessage.setMsgType(WeiboConsts.MassMsgType.TEXT);
    massMessage.setContent("测试群发消息\n欢迎欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");
    massMessage.getToUsers().add(configProvider.getOpenid());

    WeiboMpMassSendResult massResult = this.wxService.getMassMessageService().massOpenIdsMessageSend(massMessage);
    assertNotNull(massResult);
    assertNotNull(massResult.getMsgId());
  }

  @Test(dataProvider = "massMessages")
  public void testMediaMassOpenIdsMessageSend(String massMsgType, String mediaId) throws WeiboErrorException {
    // 发送群发消息
    TestConfigStorage configProvider = (TestConfigStorage) this.wxService.getWxMpConfigStorage();
    WeiboMassOpenIdsMessage massMessage = new WeiboMassOpenIdsMessage();
    massMessage.setMsgType(massMsgType);
    massMessage.setMediaId(mediaId);
    massMessage.getToUsers().add(configProvider.getOpenid());

    WeiboMpMassSendResult massResult = this.wxService.getMassMessageService().massOpenIdsMessageSend(massMessage);
    assertNotNull(massResult);
    assertNotNull(massResult.getMsgId());
  }

  @Test
  public void testTextMassGroupMessageSend() throws WeiboErrorException {
    WeiboMpMassTagMessage massMessage = new WeiboMpMassTagMessage();
    massMessage.setMsgType(WeiboConsts.MassMsgType.TEXT);
    massMessage.setContent("测试群发消息\n欢迎欢迎\n换行测试\n超链接:<a href=\"http://www.baidu.com\">Hello World</a>");
    massMessage
      .setTagId(this.wxService.getUserTagService().tagGet().get(0).getId());

    WeiboMpMassSendResult massResult = this.wxService.getMassMessageService().massGroupMessageSend(massMessage);
    assertNotNull(massResult);
    assertNotNull(massResult.getMsgId());
  }

  @Test(dataProvider = "massMessages")
  public void testMediaMassGroupMessageSend(String massMsgType, String mediaId)    throws WeiboErrorException {
    WeiboMpMassTagMessage massMessage = new WeiboMpMassTagMessage();
    massMessage.setMsgType(massMsgType);
    massMessage.setMediaId(mediaId);
    massMessage.setTagId(this.wxService.getUserTagService().tagGet().get(0).getId());

    WeiboMpMassSendResult massResult = this.wxService.getMassMessageService().massGroupMessageSend(massMessage);
    assertNotNull(massResult);
    assertNotNull(massResult.getMsgId());
  }

  @DataProvider
  public Object[][] massMessages() throws WeiboErrorException, IOException {
    Object[][] messages = new Object[4][];

    /*
     * 视频素材
     */
    try (InputStream inputStream = ClassLoader
      .getSystemResourceAsStream("mm.mp4")) {
      // 上传视频到媒体库
      WeiboMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
        .mediaUpload(WeiboConsts.MediaFileType.VIDEO, TestConstants.FILE_MP4, inputStream);
      assertNotNull(uploadMediaRes);
      assertNotNull(uploadMediaRes.getMediaId());

      // 把视频变成可被群发的媒体
      WeiboMpMassVideo video = new WeiboMpMassVideo();
      video.setTitle("测试标题");
      video.setDescription("测试描述");
      video.setMediaId(uploadMediaRes.getMediaId());
      WeiboMpMassUploadResult uploadResult = this.wxService.getMassMessageService().massVideoUpload(video);
      assertNotNull(uploadResult);
      assertNotNull(uploadResult.getMediaId());
      messages[0] = new Object[]{WeiboConsts.MassMsgType.MPVIDEO, uploadResult.getMediaId()};
    }

    /*
     * 图片素材
     */
    try (InputStream inputStream = ClassLoader
      .getSystemResourceAsStream("mm.jpeg")) {
      WeiboMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
        .mediaUpload(WeiboConsts.MediaFileType.IMAGE, TestConstants.FILE_JPG, inputStream);
      assertNotNull(uploadMediaRes);
      assertNotNull(uploadMediaRes.getMediaId());
      messages[1] = new Object[]{WeiboConsts.MassMsgType.IMAGE, uploadMediaRes.getMediaId()};
    }

    /*
     * 语音素材
     */
    try (InputStream inputStream = ClassLoader
      .getSystemResourceAsStream("mm.mp3")) {
      WeiboMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
        .mediaUpload(WeiboConsts.MediaFileType.VOICE, TestConstants.FILE_MP3, inputStream);
      assertNotNull(uploadMediaRes);
      assertNotNull(uploadMediaRes.getMediaId());
      messages[2] = new Object[]{WeiboConsts.MassMsgType.VOICE, uploadMediaRes.getMediaId()};
    }

    /*
     * 图文素材
     */
    try (InputStream inputStream = ClassLoader
      .getSystemResourceAsStream("mm.jpeg")) {
      // 上传照片到媒体库
      WeiboMediaUploadResult uploadMediaRes = this.wxService.getMaterialService()
        .mediaUpload(WeiboConsts.MediaFileType.IMAGE, TestConstants.FILE_JPG, inputStream);
      assertNotNull(uploadMediaRes);
      assertNotNull(uploadMediaRes.getMediaId());

      // 上传图文消息
      WeiboMpMassNews news = new WeiboMpMassNews();
      WeiboMpNewsArticle article1 = new WeiboMpNewsArticle();
      article1.setTitle("标题1");
      article1.setContent("内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1内容1");
      article1.setThumbMediaId(uploadMediaRes.getMediaId());
      news.addArticle(article1);

      WeiboMpNewsArticle article2 = new WeiboMpNewsArticle();
      article2.setTitle("标题2");
      article2.setContent("内容2内容2内容2内容2内容2内容2内容2内容2内2内容2内容2内容2内容2内容2内容2内容2内容2内容2");
      article2.setThumbMediaId(uploadMediaRes.getMediaId());
      article2.setShowCoverPic(true);
      article2.setAuthor("作者2");
      article2.setContentSourceUrl("www.baidu.com");
      article2.setDigest("摘要2");
      news.addArticle(article2);

      WeiboMpMassUploadResult massUploadResult = this.wxService.getMassMessageService()
        .massNewsUpload(news);
      assertNotNull(massUploadResult);
      assertNotNull(uploadMediaRes.getMediaId());
      messages[3] = new Object[]{WeiboConsts.MassMsgType.MPNEWS, massUploadResult.getMediaId()};
    }

    return messages;
  }

  @Test
  public void testMassDelete() throws Exception {
    this.wxService.getMassMessageService().delete(1L, 2);
  }

}
