package vip.seanxq.weibo.common.util.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import vip.seanxq.weibo.common.bean.WeiboAccessToken;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;
import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.bean.result.WeiboMediaUploadResult;

/**
 * .
 * @author chanjarster
 */
public class WeiboGsonBuilder {

  private static final GsonBuilder INSTANCE = new GsonBuilder();

  static {
    INSTANCE.disableHtmlEscaping();
    INSTANCE.registerTypeAdapter(WeiboAccessToken.class, new WeiboAccessTokenAdapter());
    INSTANCE.registerTypeAdapter(WeiboError.class, new WeiboErrorAdapter());
    INSTANCE.registerTypeAdapter(WeiboMenu.class, new WeiboMenuGsonAdapter());
    INSTANCE.registerTypeAdapter(WeiboMediaUploadResult.class, new WeiboMediaUploadResultAdapter());
    INSTANCE.registerTypeAdapter(WeiboNetCheckResult.class, new WeiboNetCheckResultGsonAdapter());

  }

  public static Gson create() {
    return INSTANCE.create();
  }

}
