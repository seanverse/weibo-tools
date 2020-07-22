package vip.seanxq.weibo.mp.util.requestexecuter.media;

import java.io.File;
import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;
import vip.seanxq.weibo.mp.bean.material.WeiboMediaImgUploadResult;

/**
 * @author miller
 */
public abstract class MediaImgUploadRequestExecutor<H, P> implements RequestExecutor<WeiboMediaImgUploadResult, File> {
  protected RequestHttp<H, P> requestHttp;

  public MediaImgUploadRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, File data, ResponseHandler<WeiboMediaImgUploadResult> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<WeiboMediaImgUploadResult, File> create(RequestHttp requestHttp) {
      return new MediaImgUploadApacheHttpRequestExecutor(requestHttp);
    /*switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new MediaImgUploadApacheHttpRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new MediaImgUploadHttpRequestExecutor(requestHttp);
      case OK_HTTP:
        return new MediaImgUploadOkhttpRequestExecutor(requestHttp);
      default:
        return null;
    }*/
  }

}
