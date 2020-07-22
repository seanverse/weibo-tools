package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeibMpQrcodeService;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.bean.result.WeiboMpQrCodeTicket;
import vip.seanxq.weibo.mp.util.requestexecuter.qrcode.QrCodeRequestExecutor;
import org.apache.commons.lang3.StringUtils;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Created by Binary Wang on 2016/7/21.
 *
 * @author Binary Wang
 */
@RequiredArgsConstructor
public class WeiboMpQrcodeServiceImpl implements WeibMpQrcodeService {
  private final WeiboMpService wxMpService;

  @Override
  public WeiboMpQrCodeTicket qrCodeCreateTmpTicket(int sceneId, Integer expireSeconds) throws WeiboErrorException {
    if (sceneId == 0) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg("临时二维码场景值不能为0！").build());
    }

    return this.createQrCode("QR_SCENE", null, sceneId, expireSeconds);
  }

  @Override
  public WeiboMpQrCodeTicket qrCodeCreateTmpTicket(String sceneStr, Integer expireSeconds) throws WeiboErrorException {
    if (StringUtils.isBlank(sceneStr)) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg("临时二维码场景值不能为空！").build());
    }

    return this.createQrCode("QR_STR_SCENE", sceneStr, null, expireSeconds);
  }

  private WeiboMpQrCodeTicket createQrCode(String actionName, String sceneStr, Integer sceneId, Integer expireSeconds)
    throws WeiboErrorException {
    //expireSeconds 该二维码有效时间，以秒为单位。 最大不超过2592000（即30天），此字段如果不填，则默认有效期为30秒。
    if (expireSeconds != null && expireSeconds > 2592000) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1)
        .errorMsg("临时二维码有效时间最大不能超过2592000（即30天）！").build());
    }

    if (expireSeconds == null) {
      expireSeconds = 30;
    }

    return this.getQrCodeTicket(actionName, sceneStr, sceneId, expireSeconds);
  }

  private WeiboMpQrCodeTicket getQrCodeTicket(String actionName, String sceneStr, Integer sceneId, Integer expireSeconds)
    throws WeiboErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("action_name", actionName);
    if (expireSeconds != null) {
      json.addProperty("expire_seconds", expireSeconds);
    }

    JsonObject actionInfo = new JsonObject();
    JsonObject scene = new JsonObject();
    if (sceneStr != null) {
      scene.addProperty("scene_str", sceneStr);
    } else if (sceneId != null) {
      scene.addProperty("scene_id", sceneId);
    }

    actionInfo.add("scene", scene);
    json.add("action_info", actionInfo);
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.Qrcode.QRCODE_CREATE, json.toString());
    return WeiboMpQrCodeTicket.fromJson(responseContent);
  }

  @Override
  public WeiboMpQrCodeTicket qrCodeCreateLastTicket(int sceneId) throws WeiboErrorException {
    if (sceneId < 1 || sceneId > 100000) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1)
        .errorMsg("永久二维码的场景值目前只支持1--100000！")
        .build());
    }

    return this.getQrCodeTicket("QR_LIMIT_SCENE", null, sceneId, null);
  }

  @Override
  public WeiboMpQrCodeTicket qrCodeCreateLastTicket(String sceneStr) throws WeiboErrorException {
    return this.getQrCodeTicket("QR_LIMIT_STR_SCENE", sceneStr, null, null);
  }

  @Override
  public File qrCodePicture(WeiboMpQrCodeTicket ticket) throws WeiboErrorException {
    return this.wxMpService.execute(QrCodeRequestExecutor.create(this.wxMpService.getRequestHttp()), WeiboMpApiUrl.Qrcode.SHOW_QRCODE, ticket);
  }

  @Override
  public String qrCodePictureUrl(String ticket, boolean needShortUrl) throws WeiboErrorException {
    try {
      String resultUrl = String.format(WeiboMpApiUrl.Qrcode.SHOW_QRCODE_WITH_TICKET.getUrl(this.wxMpService.getWxMpConfigStorage()),
        URLEncoder.encode(ticket, StandardCharsets.UTF_8.name()));
      if (needShortUrl) {
        return this.wxMpService.shortUrl(resultUrl);
      }

      return resultUrl;
    } catch (UnsupportedEncodingException e) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg(e.getMessage()).build());
    }
  }

  @Override
  public String qrCodePictureUrl(String ticket) throws WeiboErrorException {
    return this.qrCodePictureUrl(ticket, false);
  }

}
