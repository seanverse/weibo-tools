package vip.seanxq.weibo.mp.util.requestexecuter.voice;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.apache.Utf8ResponseHandler;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.File;
import java.io.IOException;

/**
 * <pre>
 *  Created by BinaryWang on 2018/6/9.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class VoiceUploadApacheHttpRequestExecutor extends VoiceUploadRequestExecutor<CloseableHttpClient, HttpHost> {
  public VoiceUploadApacheHttpRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public Boolean execute(String uri, File data, WeiboType weiboType) throws WeiboErrorException, IOException {
    if (data == null) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(-1).errorMsg("文件对象为空").build());
    }

    HttpPost httpPost = new HttpPost(uri);
    if (requestHttp.getRequestHttpProxy() != null) {
      RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
      httpPost.setConfig(config);
    }

    HttpEntity entity = MultipartEntityBuilder
      .create()
      .addBinaryBody("media", data)
      .setMode(HttpMultipartMode.RFC6532)
      .build();
    httpPost.setEntity(entity);
    httpPost.setHeader("Content-Type", ContentType.MULTIPART_FORM_DATA.toString());

    try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
      String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
      WeiboError error = WeiboError.fromJson(responseContent, WeiboType.MP);
      if (error.getErrorCode() != 0) {
        throw new WeiboErrorException(error);
      }

      return true;
    } finally {
      httpPost.releaseConnection();
    }
  }
}
