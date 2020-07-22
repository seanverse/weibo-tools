package vip.seanxq.weibo.mp.util.requestexecuter.qrcode;

import java.io.File;
import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;
import vip.seanxq.weibo.mp.bean.result.WeiboMpQrCodeTicket;

/**
 * 获得QrCode图片 请求执行器.
 *
 * @author chanjarster
 */
public abstract class QrCodeRequestExecutor<H, P> implements RequestExecutor<File, WeiboMpQrCodeTicket> {
  protected RequestHttp<H, P> requestHttp;

  public QrCodeRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, WeiboMpQrCodeTicket data, ResponseHandler<File> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<File, WeiboMpQrCodeTicket> create(RequestHttp requestHttp) throws WeiboErrorException {
    return new QrCodeApacheHttpRequestExecutor(requestHttp);
    /*switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new QrCodeApacheHttpRequestExecutor(requestHttp);
      case JODD_HTTP:
        return new QrCodeJoddHttpRequestExecutor(requestHttp);
      case OK_HTTP:
        return new QrCodeOkhttpRequestExecutor(requestHttp);
      default:
        throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg("不支持的http框架").build());
    }*/
  }

}
