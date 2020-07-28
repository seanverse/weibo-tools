package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.bean.HttpDataParam;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboFansQrcodeService;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.bean.result.WeiboFansQrCodeTicket;
import vip.seanxq.weibo.mp.util.requestexecuter.qrcode.QrCodeRequestExecutor;
import org.apache.commons.lang3.StringUtils;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 二维码请求服务
 */
@RequiredArgsConstructor
public class WeiboFansQrcodeServiceImpl implements WeiboFansQrcodeService {
  private final WeiboMpService weiboMpService;

  @Override
  public WeiboFansQrCodeTicket qrCodeCreateTmpTicket(int sceneId, String sceneStr, Integer expireSeconds) throws WeiboErrorException {
    if (sceneId == 0) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg("临时二维码场景值不能为0！").build());
    }
    //QR_SEENE为临时， QR_LIMIT_SCENT为永久
    return this.createQrCode("QR_SCENE", null, sceneId, expireSeconds);
  }


  private WeiboFansQrCodeTicket createQrCode(String actionName, String sceneStr, Integer sceneId, Integer expireSeconds)
    throws WeiboErrorException {
    //expireSeconds 该二维码有效时间，以秒为单位。 最大不超过1800秒（即30分钟），此字段如果不填，则默认有效期为1800秒。
    if (expireSeconds != null && expireSeconds > 1800) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1)
        .errorMsg("临时二维码有效时间最大不能超过1800s（即30分钟）！").build());
    }

    if (expireSeconds == null) {
      expireSeconds = 1800;
    }

    return this.getQrCodeTicket(actionName, sceneStr, sceneId, expireSeconds);
  }

  private WeiboFansQrCodeTicket getQrCodeTicket(String actionName, String sceneStr, Integer sceneId, Integer expireSeconds)
    throws WeiboErrorException {
    HttpDataParam param = new HttpDataParam();
    param.put("action_name", actionName);
    if (expireSeconds != null) {
      param.put("expire_seconds", expireSeconds.toString());
    }

    if (sceneId != null) {  param.put("scene_id", sceneId.toString());}

    JsonObject actionInfo = new JsonObject();
    JsonObject scene = new JsonObject();
    if (sceneStr != null) {
      scene.addProperty("scene_str", sceneStr); //weibo没有，预留
    } else if (sceneId != null) {
      scene.addProperty("scene_id", sceneId);
    }
    actionInfo.add("scene", scene);
    param.put("action_info", actionInfo.toString()); //	二维码详细信息，里面带了场景Id，场景说明

    String responseContent = this.weiboMpService.post(WeiboMpApiUrl.Qrcode.QRCODE_CREATE, param.toString());
    return WeiboFansQrCodeTicket.fromJson(responseContent);
  }

  @Override
  public WeiboFansQrCodeTicket qrCodeCreateLastTicket(int sceneId, String sceneStr) throws WeiboErrorException {
    if (sceneId < 1 || sceneId > 100000) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1)
        .errorMsg("永久二维码的场景值目前只支持1--100000！")
        .build());
    }

    return this.getQrCodeTicket("QR_LIMIT_SCENE", null, sceneId, null);
  }


  @Override
  public File qrCodePicture(WeiboFansQrCodeTicket ticket) throws WeiboErrorException {
    return this.weiboMpService.execute(QrCodeRequestExecutor.create(this.weiboMpService.getRequestHttp()), WeiboMpApiUrl.Qrcode.SHOW_QRCODE, ticket);
  }

  @Override
  public String qrCodePictureUrl(WeiboFansQrCodeTicket ticket) throws WeiboErrorException {
    //File file = this.qrCodePicture(ticket);
    //todo: seanx: 生成本地网站的url
    return  null;
  }

}
