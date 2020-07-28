package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeibFansMassMessageService;
import vip.seanxq.weibo.mp.bean.mass.*;
import vip.seanxq.weibo.mp.bean.result.WeiboMpMassGetResult;
import vip.seanxq.weibo.mp.bean.result.WeiboMpMassSendResult;
import vip.seanxq.weibo.mp.bean.result.WeiboMpMassSpeedGetResult;
import vip.seanxq.weibo.mp.bean.result.WeiboMpMassUploadResult;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

/**
 * <pre>
 * 群发消息服务类
 * Created by Binary Wang on 2017-8-16.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Slf4j
@RequiredArgsConstructor
public class WeibFansMassMessageServiceImpl implements WeibFansMassMessageService {
  private final WeiboMpService wxMpService;

  @Override
  public WeiboMpMassUploadResult massNewsUpload(WeiboMpMassNews news) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MEDIA_UPLOAD_NEWS_URL, news.toJson());
    return WeiboMpMassUploadResult.fromJson(responseContent);
  }

  @Override
  public WeiboMpMassUploadResult massVideoUpload(WeiboMpMassVideo video) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MEDIA_UPLOAD_VIDEO_URL, video.toJson());
    return WeiboMpMassUploadResult.fromJson(responseContent);
  }

  @Override
  public WeiboMpMassSendResult massGroupMessageSend(WeiboMpMassTagMessage message) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_SENDALL_URL, message.toJson());
    return WeiboMpMassSendResult.fromJson(responseContent);
  }

  @Override
  public WeiboMpMassSendResult massOpenIdsMessageSend(WeiboMassOpenIdsMessage message) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_SEND_URL, message.toJson());
    return WeiboMpMassSendResult.fromJson(responseContent);
  }

  @Override
  public WeiboMpMassSendResult massMessagePreview(WeiboMpMassPreviewMessage weiboMpMassPreviewMessage) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_PREVIEW_URL, weiboMpMassPreviewMessage.toJson());
    return WeiboMpMassSendResult.fromJson(responseContent);
  }

  @Override
  public void delete(Long msgId, Integer articleIndex) throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("msg_id", msgId);
    jsonObject.addProperty("article_idx", articleIndex);
    this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_DELETE_URL, jsonObject.toString());
  }


  @Override
  public WeiboMpMassSpeedGetResult messageMassSpeedGet() throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    String response = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_SPEED_GET_URL, jsonObject.toString());
    return WeiboMpMassSpeedGetResult.fromJson(response);
  }


  @Override
  public void messageMassSpeedSet(Integer speed) throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("speed", speed);
    this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_SPEED_SET_URL, jsonObject.toString());
  }


  @Override
  public WeiboMpMassGetResult messageMassGet(Long msgId) throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("msg_id", msgId);
    String response = this.wxMpService.post(WeiboMpApiUrl.MassMessage.MESSAGE_MASS_GET_URL, jsonObject.toString());
    return WeiboMpMassGetResult.fromJson(response);
  }

}
