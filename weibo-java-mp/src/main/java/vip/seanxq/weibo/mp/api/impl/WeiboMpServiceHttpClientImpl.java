package vip.seanxq.weibo.mp.api.impl;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.HttpType;
import vip.seanxq.weibo.common.util.http.apache.ApacheHttpClientBuilder;
import vip.seanxq.weibo.common.util.http.apache.DefaultApacheHttpClientBuilder;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import org.apache.http.HttpHost;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.io.IOException;
import java.util.concurrent.locks.Lock;

/**
 * apache http client方式实现.
 *
 * @author someone
 */
public class WeiboMpServiceHttpClientImpl extends BaseWeiboMpServiceImpl<CloseableHttpClient, HttpHost> {
  private CloseableHttpClient httpClient;
  private HttpHost httpProxy;

  @Override
  public CloseableHttpClient getRequestHttpClient() {
    return httpClient;
  }

  @Override
  public HttpHost getRequestHttpProxy() {
    return httpProxy;
  }

  @Override
  public HttpType getRequestType() {
    return HttpType.APACHE_HTTP;
  }

  @Override
  public void initHttp() {
    //todo: seanx: mp的httpclient builder在这里获得builder实例
    WeiboConfigStorage configStorage = this.getWxMpConfigStorage();
    ApacheHttpClientBuilder apacheHttpClientBuilder = configStorage.getApacheHttpClientBuilder();
    if (null == apacheHttpClientBuilder) {
      apacheHttpClientBuilder = DefaultApacheHttpClientBuilder.get();
    }

    apacheHttpClientBuilder.httpProxyHost(configStorage.getHttpProxyHost())
      .httpProxyPort(configStorage.getHttpProxyPort())
      .httpProxyUsername(configStorage.getHttpProxyUsername())
      .httpProxyPassword(configStorage.getHttpProxyPassword());

    if (configStorage.getHttpProxyHost() != null && configStorage.getHttpProxyPort() > 0) {
      this.httpProxy = new HttpHost(configStorage.getHttpProxyHost(), configStorage.getHttpProxyPort());
    }

    this.httpClient = apacheHttpClientBuilder.build();
  }

  @Override
  public String getAccessToken(boolean forceRefresh) throws WeiboErrorException {
    final WeiboConfigStorage config = this.getWxMpConfigStorage();
    if (!config.isAccessTokenExpired() && !forceRefresh) {
      return config.getAccessToken();
    }

    Lock lock = config.getAccessTokenLock();
    lock.lock();
    try {
      if (!config.isAccessTokenExpired() && !forceRefresh) {
        return config.getAccessToken();
      }
      String url = String.format(WeiboMpApiUrl.Other.GET_ACCESS_TOKEN_URL.getUrl(config), config.getAppId(), config.getSecret());
      try {
        HttpGet httpGet = new HttpGet(url);
        if (this.getRequestHttpProxy() != null) {
          RequestConfig requestConfig = RequestConfig.custom().setProxy(this.getRequestHttpProxy()).build();
          httpGet.setConfig(requestConfig);
        }
        try (CloseableHttpResponse response = getRequestHttpClient().execute(httpGet)) {
          return this.extractAccessToken(new BasicResponseHandler().handleResponse(response));
        } finally {
          httpGet.releaseConnection();
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    } finally {
      lock.unlock();
    }
  }

}
