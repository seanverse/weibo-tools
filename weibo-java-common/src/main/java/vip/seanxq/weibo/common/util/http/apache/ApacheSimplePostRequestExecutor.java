package vip.seanxq.weibo.common.util.http.apache;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.SimplePostRequestExecutor;
import org.apache.http.Consts;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;


import java.io.IOException;

/**
 * .
 *
 * @author ecoolper
 * @date 2017/5/4
 */
public class ApacheSimplePostRequestExecutor extends SimplePostRequestExecutor<CloseableHttpClient, HttpHost> {
  public ApacheSimplePostRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public String execute(String uri, String postEntity, WeiboType weiboType) throws WeiboErrorException, IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (requestHttp.getRequestHttpProxy() != null) {
      RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
      httpPost.setConfig(config);
    }

    if (postEntity != null) {
      StringEntity entity = new StringEntity(postEntity, Consts.UTF_8);
      entity.setContentType("application/json; charset=utf-8");
      httpPost.setEntity(entity);
    }

    try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
      String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
      return this.handleResponse(weiboType, responseContent);
    } finally {
      httpPost.releaseConnection();
    }
  }

}
