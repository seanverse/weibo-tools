package vip.seanxq.weibo.mp.util.requestexecuter.material;

import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;

public abstract class MaterialDeleteRequestExecutor<H, P> implements RequestExecutor<Boolean, String> {
  protected RequestHttp<H, P> requestHttp;

  public MaterialDeleteRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<Boolean> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<Boolean, String> create(RequestHttp requestHttp) {
    return new MaterialDeleteApacheHttpRequestExecutor(requestHttp);
    /*switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new MaterialDeleteApacheHttpRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new MaterialDeleteJoddHttpRequestExecutor(requestHttp);
      case OK_HTTP:
        return new MaterialDeleteOkhttpRequestExecutor(requestHttp);
      default:
        return null;
    }*/
  }

}
