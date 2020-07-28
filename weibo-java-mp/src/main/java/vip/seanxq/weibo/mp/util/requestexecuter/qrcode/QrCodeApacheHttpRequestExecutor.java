package vip.seanxq.weibo.mp.util.requestexecuter.qrcode;

import org.apache.http.Consts;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.bean.HttpDataParam;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.fs.FileUtils;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.apache.InputStreamResponseHandler;
import vip.seanxq.weibo.common.util.http.apache.Utf8ResponseHandler;
import vip.seanxq.weibo.mp.bean.result.WeiboFansQrCodeTicket;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.entity.ContentType;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.UUID;

/**
 * QRCode 下载图片
 */
public class QrCodeApacheHttpRequestExecutor extends QrCodeRequestExecutor<CloseableHttpClient, HttpHost> {
  public QrCodeApacheHttpRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public File execute(String uri, WeiboFansQrCodeTicket ticket, WeiboType weiboType) throws WeiboErrorException, IOException {
    HttpDataParam param = new HttpDataParam();
    if (ticket != null) {
      param.put("ticket", ticket.getTicket());
    }

    HttpPost httpPost = new HttpPost(uri);
    if (requestHttp.getRequestHttpProxy() != null) {
      RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
      httpPost.setConfig(config);
    }
    //weibo接收的是 application/x-www-form-urlencoded：数据被编码为名称/值对。这是标准的编码格式
    if (ticket.getTicket() != null) {
      StringEntity entity = new StringEntity(param.toParamString(), Consts.UTF_8);
      entity.setContentType("application/x-www-form-urlencoded; charset=utf-8");
      httpPost.setEntity(entity);
    }

    try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost);
         InputStream inputStream = InputStreamResponseHandler.INSTANCE.handleResponse(response);) {
      Header[] contentTypeHeader = response.getHeaders("Content-Type");
      if (contentTypeHeader != null && contentTypeHeader.length > 0) {
        // 出错
        if (ContentType.TEXT_PLAIN.getMimeType()
          .equals(ContentType.parse(contentTypeHeader[0].getValue()).getMimeType())) {
          String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
          throw new WeiboErrorException(WeiboError.fromJson(responseContent, WeiboType.MP));
        }
      }
      return FileUtils.createTmpFile(inputStream, UUID.randomUUID().toString(), "jpg");
    } finally {
      httpPost.releaseConnection();
    }
  }
}
