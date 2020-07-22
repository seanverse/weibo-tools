package vip.seanxq.weibo.mp.util.requestexecuter.material;


import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialVideoInfoResult;

public abstract class MaterialVideoInfoRequestExecutor<H, P> implements RequestExecutor<WeiboMpMaterialVideoInfoResult, String> {
  protected RequestHttp<H, P> requestHttp;

  public MaterialVideoInfoRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<WeiboMpMaterialVideoInfoResult> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<WeiboMpMaterialVideoInfoResult, String> create(RequestHttp requestHttp) {
    return new MaterialVideoInfoApacheHttpRequestExecutor(requestHttp);
    /*switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new MaterialVideoInfoApacheHttpRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new MaterialVideoInfoJoddHttpRequestExecutor(requestHttp);
      case OK_HTTP:
        return new MaterialVideoInfoOkhttpRequestExecutor(requestHttp);
      default:
        return null;
    }*/
  }

}
