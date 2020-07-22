package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.bean.result.WeiboMediaUploadResult;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.api.WeibMpMessageHandler;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.test.TestConstants;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutImageMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;

import java.util.Map;

public class DemoImageHandler implements WeibMpMessageHandler {
  @Override
  public WeiboMpXmlOutMessage handle(WeiboMpXmlMessage wxMessage, Map<String, Object> context,
                                     WeiboMpService wxMpService, WeiboSessionManager sessionManager) {
    try {
      WeiboMediaUploadResult weiboMediaUploadResult = wxMpService.getMaterialService()
        .mediaUpload(WeiboConsts.MediaFileType.IMAGE, TestConstants.FILE_JPG, ClassLoader.getSystemResourceAsStream("mm.jpeg"));
      WeiboMpXmlOutImageMessage m
        = WeiboMpXmlOutMessage
        .IMAGE()
        .mediaId(weiboMediaUploadResult.getMediaId())
        .fromUser(wxMessage.getToUser())
        .toUser(wxMessage.getFromUser())
        .build();
      return m;
    } catch (WeiboErrorException e) {
      e.printStackTrace();
    }

    return null;
  }
}
