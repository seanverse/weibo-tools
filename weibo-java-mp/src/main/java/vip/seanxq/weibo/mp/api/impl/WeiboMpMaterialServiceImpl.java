package vip.seanxq.weibo.mp.api.impl;

import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.bean.result.WeiboMediaUploadResult;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.fs.FileUtils;
import vip.seanxq.weibo.common.util.http.BaseMediaDownloadRequestExecutor;
import vip.seanxq.weibo.common.util.http.MediaUploadRequestExecutor;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;
import vip.seanxq.weibo.mp.api.WeiboMpMaterialService;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;
import vip.seanxq.weibo.mp.util.requestexecuter.media.MediaImgUploadRequestExecutor;
import vip.seanxq.weibo.mp.bean.material.*;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;
import vip.seanxq.weibo.mp.util.requestexecuter.material.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by Binary Wang on 2016/7/21.
 *
 * @author Binary Wang
 */
@RequiredArgsConstructor
public class WeiboMpMaterialServiceImpl implements WeiboMpMaterialService {
  private final WeiboMpService wxMpService;

  @Override
  public WeiboMediaUploadResult mediaUpload(String mediaType, String fileType, InputStream inputStream) throws WeiboErrorException {
    File tmpFile = null;
    try {
      tmpFile = FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), fileType);
      return this.mediaUpload(mediaType, tmpFile);
    } catch (IOException e) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg(e.getMessage()).build(), e);
    } finally {
      if (tmpFile != null) {
        tmpFile.delete();
      }
    }
  }

  @Override
  public WeiboMediaUploadResult mediaUpload(String mediaType, File file) throws WeiboErrorException {
    String url = String.format(WeiboMpApiUrl.Material.MEDIA_UPLOAD_URL.getUrl(this.wxMpService.getWxMpConfigStorage()), mediaType);
    return this.wxMpService.execute(MediaUploadRequestExecutor.create(this.wxMpService.getRequestHttp()), url, file);
  }

  @Override
  public File mediaDownload(String mediaId) throws WeiboErrorException {
    return this.wxMpService.execute(
      BaseMediaDownloadRequestExecutor.create(this.wxMpService.getRequestHttp(), this.wxMpService.getWxMpConfigStorage().getTmpDirFile()),
      WeiboMpApiUrl.Material.MEDIA_GET_URL,
      "media_id=" + mediaId);
  }

  @Override
  public File jssdkMediaDownload(String mediaId) throws WeiboErrorException {
    return this.wxMpService.execute(
      BaseMediaDownloadRequestExecutor.create(this.wxMpService.getRequestHttp(), this.wxMpService.getWxMpConfigStorage().getTmpDirFile()),
      WeiboMpApiUrl.Material.JSSDK_MEDIA_GET_URL,
      "media_id=" + mediaId);
  }

  @Override
  public WeiboMediaImgUploadResult mediaImgUpload(File file) throws WeiboErrorException {
    return this.wxMpService.execute(MediaImgUploadRequestExecutor.create(this.wxMpService.getRequestHttp()), WeiboMpApiUrl.Material.IMG_UPLOAD_URL, file);
  }

  @Override
  public WeiboMpMaterialUploadResult materialFileUpload(String mediaType, WeiboMpMaterial material) throws WeiboErrorException {
    String url = String.format(WeiboMpApiUrl.Material.MATERIAL_ADD_URL.getUrl(this.wxMpService.getWxMpConfigStorage()), mediaType);
    return this.wxMpService.execute(MaterialUploadRequestExecutor.create(this.wxMpService.getRequestHttp()), url, material);
  }

  @Override
  public WeiboMpMaterialUploadResult materialNewsUpload(WeiboMpMaterialNews news) throws WeiboErrorException {
    if (news == null || news.isEmpty()) {
      throw new IllegalArgumentException("news is empty!");
    }
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.Material.NEWS_ADD_URL, news.toJson());
    return WeiboMpMaterialUploadResult.fromJson(responseContent);
  }

  @Override
  public InputStream materialImageOrVoiceDownload(String mediaId) throws WeiboErrorException {
    return this.wxMpService.execute(MaterialVoiceAndImageDownloadRequestExecutor
      .create(this.wxMpService.getRequestHttp(), this.wxMpService.getWxMpConfigStorage().getTmpDirFile()),
      WeiboMpApiUrl.Material.MATERIAL_GET_URL, mediaId);
  }

  @Override
  public WeiboMpMaterialVideoInfoResult materialVideoInfo(String mediaId) throws WeiboErrorException {
    return this.wxMpService.execute(MaterialVideoInfoRequestExecutor.create(this.wxMpService.getRequestHttp()),
      WeiboMpApiUrl.Material.MATERIAL_GET_URL, mediaId);
  }

  @Override
  public WeiboMpMaterialNews materialNewsInfo(String mediaId) throws WeiboErrorException {
    return this.wxMpService.execute(MaterialNewsInfoRequestExecutor.create(this.wxMpService.getRequestHttp()),
      WeiboMpApiUrl.Material.MATERIAL_GET_URL, mediaId);
  }

  @Override
  public boolean materialNewsUpdate(WeiboMpMaterialArticleUpdate weiboMpMaterialArticleUpdate) throws WeiboErrorException {
    String responseText = this.wxMpService.post(WeiboMpApiUrl.Material.NEWS_UPDATE_URL, weiboMpMaterialArticleUpdate.toJson());
    WeiboError weiboError = WeiboError.fromJson(responseText, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    } else {
      throw new WeiboErrorException(weiboError);
    }
  }

  @Override
  public boolean materialDelete(String mediaId) throws WeiboErrorException {
    return this.wxMpService.execute(MaterialDeleteRequestExecutor.create(this.wxMpService.getRequestHttp()),
      WeiboMpApiUrl.Material.MATERIAL_DEL_URL, mediaId);
  }

  @Override
  public WeiboMpMaterialCountResult materialCount() throws WeiboErrorException {
    String responseText = this.wxMpService.get(WeiboMpApiUrl.Material.MATERIAL_GET_COUNT_URL, null);
    WeiboError weiboError = WeiboError.fromJson(responseText, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return WbMpGsonBuilder.create().fromJson(responseText, WeiboMpMaterialCountResult.class);
    } else {
      throw new WeiboErrorException(weiboError);
    }
  }

  @Override
  public WeiboMpMaterialNewsBatchGetResult materialNewsBatchGet(int offset, int count) throws WeiboErrorException {
    Map<String, Object> params = new HashMap<>(4);
    params.put("type", WeiboConsts.MaterialType.NEWS);
    params.put("offset", offset);
    params.put("count", count);
    String responseText = this.wxMpService.post(WeiboMpApiUrl.Material.MATERIAL_BATCHGET_URL, WeiboGsonBuilder.create().toJson(params));
    WeiboError weiboError = WeiboError.fromJson(responseText, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return WbMpGsonBuilder.create().fromJson(responseText, WeiboMpMaterialNewsBatchGetResult.class);
    } else {
      throw new WeiboErrorException(weiboError);
    }
  }

  @Override
  public WeiboMpMaterialFileBatchGetResult materialFileBatchGet(String type, int offset, int count) throws WeiboErrorException {
    Map<String, Object> params = new HashMap<>(4);
    params.put("type", type);
    params.put("offset", offset);
    params.put("count", count);
    String responseText = this.wxMpService.post(WeiboMpApiUrl.Material.MATERIAL_BATCHGET_URL, WeiboGsonBuilder.create().toJson(params));
    WeiboError weiboError = WeiboError.fromJson(responseText, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return WbMpGsonBuilder.create().fromJson(responseText, WeiboMpMaterialFileBatchGetResult.class);
    } else {
      throw new WeiboErrorException(weiboError);
    }
  }

}
