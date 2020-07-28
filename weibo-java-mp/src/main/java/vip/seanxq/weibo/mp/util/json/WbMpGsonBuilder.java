package vip.seanxq.weibo.mp.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import vip.seanxq.weibo.mp.bean.mass.*;
import vip.seanxq.weibo.mp.bean.material.*;
import vip.seanxq.weibo.mp.bean.result.*;
import vip.seanxq.weibo.mp.bean.subscribe.WxMpSubscribeMessage;
import vip.seanxq.weibo.mp.bean.tag.WeiboUserTag;

/**
 * @author someone
 */
public class WbMpGsonBuilder {

  private static final GsonBuilder INSTANCE = new GsonBuilder();

  //也可以使用@JsonAdapter注解标注在实体类上

  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(WeiboFansUser.class, new WeiboFansUserGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboUserTag.class, new WeiboUserTagGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboFansUserList.class, new WeiboUserListGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboFansQrCodeTicket.class, new WeiboQrCodeTicketAdapter());
    //-------------------------
    INSTANCE.registerTypeAdapter(WeiboMpMassNews.class, new WeiboMpMassNewsGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMassTagMessage.class, new WeiboMpMassTagMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMassOpenIdsMessage.class, new WeiboMpMassOpenIdsMessageGsonAdapter());

    INSTANCE.registerTypeAdapter(WeiboMpChangeOpenid.class, new WbMpChangeOpenidGsonAdapter());

    INSTANCE.registerTypeAdapter(WeiboMpMassVideo.class, new WeiboMpMassVideoAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMassSendResult.class, new WeiboMpMassSendResultAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMassUploadResult.class, new WeiboMpMassUploadResultAdapter());

    INSTANCE.registerTypeAdapter(WxMpSubscribeMessage.class, new WeiboMpSubscribeMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpSemanticQueryResult.class, new WeiboMpSemanticQueryResultAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpOAuth2AccessToken.class, new WeiboMpOAuth2AccessTokenAdapter());


    INSTANCE.registerTypeAdapter(WeiboMpMaterialUploadResult.class, new WeiboMpMaterialUploadResultAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialVideoInfoResult.class, new WeiboMpMaterialVideoInfoResultAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialArticleUpdate.class, new WeiboMpMaterialArticleUpdateGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialCountResult.class, new WeiboMpMaterialCountResultAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialNews.class, new WeiboMpMaterialNewsGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpNewsArticle.class, new WeiboMpNewsArticleGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialNewsBatchGetResult.class, new WeiboMpMaterialNewsBatchGetGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialNewsBatchGetResult.WxMaterialNewsBatchGetNewsItem.class, new WeiboMpMaterialNewsBatchGetGsonItemAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialFileBatchGetResult.class, new WeiboMpMaterialFileBatchGetGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMaterialFileBatchGetResult.WxMaterialFileBatchGetNewsItem.class, new WeiboMpMaterialFileBatchGetGsonItemAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpMassPreviewMessage.class, new WeiboMpMassPreviewMessageGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMediaImgUploadResult.class, new WbMediaImgUploadResultGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMpUserBlacklistGetResult.class, new WeiboUserBlacklistGetResultGsonAdapter());
  }

  public static Gson create() {
    return INSTANCE.create();
  }

}
