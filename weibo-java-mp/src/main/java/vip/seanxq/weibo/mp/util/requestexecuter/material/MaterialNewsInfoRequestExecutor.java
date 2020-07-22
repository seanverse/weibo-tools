package vip.seanxq.weibo.mp.util.requestexecuter.material;

import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialNews;

public abstract class MaterialNewsInfoRequestExecutor<H, P> implements RequestExecutor<WeiboMpMaterialNews, String> {
  protected RequestHttp<H, P> requestHttp;

  public MaterialNewsInfoRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<WeiboMpMaterialNews> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<WeiboMpMaterialNews, String> create(RequestHttp requestHttp) {
    return new MaterialNewsInfoApacheHttpRequestExecutor(requestHttp);
   /* switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new MaterialNewsInfoApacheHttpRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new MaterialNewsInfoJoddHttpRequestExecutor(requestHttp);
      case OK_HTTP:
        return new MaterialNewsInfoOkhttpRequestExecutor(requestHttp);
      default:
        //TODO 需要优化抛出异常
        return null;
    }*/
  }

}
