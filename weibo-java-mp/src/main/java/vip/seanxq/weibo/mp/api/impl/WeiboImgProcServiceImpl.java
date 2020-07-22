package vip.seanxq.weibo.mp.api.impl;

import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboImgProcService;
import vip.seanxq.weibo.mp.bean.imgproc.WeiboImgProcAiCropResult;
import vip.seanxq.weibo.mp.bean.imgproc.WeiboImgProcQrCodeResult;
import vip.seanxq.weibo.mp.bean.imgproc.WeiboImgProcSuperResolutionResult;
import vip.seanxq.weibo.mp.util.requestexecuter.ocr.OcrDiscernRequestExecutor;
import org.apache.commons.lang3.StringUtils;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * 图像处理接口实现.
 * @author Theo Nie
 */
@RequiredArgsConstructor
public class WeiboImgProcServiceImpl implements WeiboImgProcService {
  private final WeiboMpService wxMpService;

  @Override
  public WeiboImgProcQrCodeResult qrCode(String imgUrl) throws WeiboErrorException {
    try {
      imgUrl = URLEncoder.encode(imgUrl, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      //ignore
    }

    final String result = this.wxMpService.get(String.format(WeiboMpApiUrl.ImgProc.QRCODE.getUrl(this.wxMpService.getWxMpConfigStorage()), imgUrl), null);
    return WeiboImgProcQrCodeResult.fromJson(result);
  }

  @Override
  public WeiboImgProcQrCodeResult qrCode(File imgFile) throws WeiboErrorException {
    String result = this.wxMpService.execute(OcrDiscernRequestExecutor.create(this.wxMpService.getRequestHttp()), WeiboMpApiUrl.ImgProc.FILE_QRCODE.getUrl(this.wxMpService.getWxMpConfigStorage()), imgFile);
    return WeiboImgProcQrCodeResult.fromJson(result);
  }

  @Override
  public WeiboImgProcSuperResolutionResult superResolution(String imgUrl) throws WeiboErrorException {
    try {
      imgUrl = URLEncoder.encode(imgUrl, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      //ignore
    }

    final String result = this.wxMpService.get(String.format(WeiboMpApiUrl.ImgProc.SUPER_RESOLUTION.getUrl(this.wxMpService.getWxMpConfigStorage()), imgUrl), null);
    return WeiboImgProcSuperResolutionResult.fromJson(result);
  }

  @Override
  public WeiboImgProcSuperResolutionResult superResolution(File imgFile) throws WeiboErrorException {
    String result = this.wxMpService.execute(OcrDiscernRequestExecutor.create(this.wxMpService.getRequestHttp()), WeiboMpApiUrl.ImgProc.FILE_SUPER_RESOLUTION.getUrl(this.wxMpService.getWxMpConfigStorage()), imgFile);
    return WeiboImgProcSuperResolutionResult.fromJson(result);
  }

  @Override
  public WeiboImgProcAiCropResult aiCrop(String imgUrl) throws WeiboErrorException {
    return this.aiCrop(imgUrl, "");
  }

  @Override
  public WeiboImgProcAiCropResult aiCrop(String imgUrl, String ratios) throws WeiboErrorException {
    try {
      imgUrl = URLEncoder.encode(imgUrl, StandardCharsets.UTF_8.name());
    } catch (UnsupportedEncodingException e) {
      //ignore
    }

    if (StringUtils.isEmpty(ratios)) {
      ratios = "";
    }

    final String result = this.wxMpService.get(String.format(WeiboMpApiUrl.ImgProc.AI_CROP.getUrl(this.wxMpService.getWxMpConfigStorage()), imgUrl, ratios), null);
    return WeiboImgProcAiCropResult.fromJson(result);
  }

  @Override
  public WeiboImgProcAiCropResult aiCrop(File imgFile) throws WeiboErrorException {
    return this.aiCrop(imgFile, "");
  }

  @Override
  public WeiboImgProcAiCropResult aiCrop(File imgFile, String ratios) throws WeiboErrorException {
    if (StringUtils.isEmpty(ratios)) {
      ratios = "";
    }

    String result = this.wxMpService.execute(OcrDiscernRequestExecutor.create(this.wxMpService.getRequestHttp()), String.format(WeiboMpApiUrl.ImgProc.FILE_AI_CROP.getUrl(this.wxMpService.getWxMpConfigStorage()), ratios), imgFile);
    return WeiboImgProcAiCropResult.fromJson(result);
  }
}
