package vip.seanxq.weibo.mp.util.requestexecuter.material;

import com.google.common.collect.ImmutableMap;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestHttp;
import vip.seanxq.weibo.common.util.http.apache.Utf8ResponseHandler;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;
import vip.seanxq.weibo.mp.bean.material.WeiboMpMaterialNews;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * httpclient 实现的素材请求执行器.
 *
 * @author ecoolper
 * @date 2017/5/5
 */
public class MaterialNewsInfoApacheHttpRequestExecutor
    extends MaterialNewsInfoRequestExecutor<CloseableHttpClient, HttpHost> {
  private final Logger logger = LoggerFactory.getLogger(this.getClass());

  public MaterialNewsInfoApacheHttpRequestExecutor(RequestHttp requestHttp) {
    super(requestHttp);
  }

  @Override
  public WeiboMpMaterialNews execute(String uri, String materialId, WeiboType weiboType) throws WeiboErrorException, IOException {
    HttpPost httpPost = new HttpPost(uri);
    if (requestHttp.getRequestHttpProxy() != null) {
      RequestConfig config = RequestConfig.custom().setProxy(requestHttp.getRequestHttpProxy()).build();
      httpPost.setConfig(config);
    }

    httpPost.setEntity(new StringEntity(WeiboGsonBuilder.create().toJson(ImmutableMap.of("media_id", materialId))));
    try (CloseableHttpResponse response = requestHttp.getRequestHttpClient().execute(httpPost)) {
      String responseContent = Utf8ResponseHandler.INSTANCE.handleResponse(response);
      this.logger.debug("响应原始数据：{}", responseContent);
      WeiboError error = WeiboError.fromJson(responseContent, WeiboType.MP);
      if (error.getErrorCode() != 0) {
        throw new WeiboErrorException(error);
      } else {
        return WbMpGsonBuilder.create().fromJson(responseContent, WeiboMpMaterialNews.class);
      }
    } finally {
      httpPost.releaseConnection();
    }
  }
}
