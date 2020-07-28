package vip.seanxq.weibo.mp.util.requestexecuter.qrcode;

import java.io.File;
import java.io.IOException;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.ResponseHandler;
import vip.seanxq.weibo.mp.bean.result.WeiboFansQrCodeTicket;

/**
 * 获得QrCode图片 请求执行器.
 *
 * @author chanjarster
 */
public abstract class QrCodeRequestExecutor<H, P> implements RequestExecutor<File, WeiboFansQrCodeTicket> {
  protected RequestHttp<H, P> requestHttp;

  public QrCodeRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, WeiboFansQrCodeTicket data, ResponseHandler<File> handler, WeiboType weiboType) throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<File, WeiboFansQrCodeTicket> create(RequestHttp requestHttp) throws WeiboErrorException {
    return new QrCodeApacheHttpRequestExecutor(requestHttp);
  }

}
