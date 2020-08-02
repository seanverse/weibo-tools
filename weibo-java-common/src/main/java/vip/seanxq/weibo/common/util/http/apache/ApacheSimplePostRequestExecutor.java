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
 * ApacheSimplePostRequestExecutor
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
    //weibo极大部分接收的是 application/x-www-form-urlencoded：数据被编码为名称/值对。这是标准的编码格式
    //只有个别几个API接收Json数据
    if (postEntity != null) {
      StringEntity entity = new StringEntity(postEntity, Consts.UTF_8);
      if (postEntity.trim().startsWith("{"))
        entity.setContentType("application/json; charset=utf-8");
      else
        entity.setContentType("application/x-www-form-urlencoded; charset=utf-8");
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
