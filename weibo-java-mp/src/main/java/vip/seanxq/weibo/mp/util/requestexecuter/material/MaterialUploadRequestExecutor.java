package vip.seanxq.weibo.mp.util.requestexecuter.material;

import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterial;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialUploadResult;

/**
 * @author codepiano
 */
public abstract class MaterialUploadRequestExecutor<H, P> implements RequestExecutor<WeiboMpMaterialUploadResult, WeiboMpMaterial> {
  protected RequestHttp<H, P> requestHttp;

  public MaterialUploadRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, WeiboMpMaterial data, ResponseHandler<WeiboMpMaterialUploadResult> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<WeiboMpMaterialUploadResult, WeiboMpMaterial> create(RequestHttp requestHttp) {
    return new MaterialUploadApacheHttpRequestExecutor(requestHttp);
    /*switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new MaterialUploadApacheHttpRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new MaterialUploadJoddHttpRequestExecutor(requestHttp);
      case OK_HTTP:
        return new MaterialUploadOkhttpRequestExecutor(requestHttp);
      default:
        return null;
    }*/
  }

}
