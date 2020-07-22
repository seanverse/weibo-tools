package vip.seanxq.weibo.common.util.http;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.apache.ApacheSimplePostRequestExecutor;

import java.io.IOException;

/**
 * 简单的POST请求执行器，请求的参数是String, 返回的结果也是String
 *
 * @author Daniel Qian
 */
public abstract class SimplePostRequestExecutor<H, P> implements RequestExecutor<String, String> {
  protected RequestHttp<H, P> requestHttp;

  public SimplePostRequestExecutor(RequestHttp requestHttp) {
    this.requestHttp = requestHttp;
  }

  @Override
  public void execute(String uri, String data, ResponseHandler<String> handler, WeiboType weiboType)
    throws WeiboErrorException, IOException {
    handler.handle(this.execute(uri, data, weiboType));
  }

  public static RequestExecutor<String, String> create(RequestHttp requestHttp) {
    return new ApacheSimplePostRequestExecutor(requestHttp);
    /*switch (requestHttp.getRequestType()) {
      case APACHE_HTTP:
        return new ApacheSimplePostRequestExecutor(requestHttp);
      default:
        throw new IllegalArgumentException("非法请求参数");
    }*/
  }

  public String handleResponse(WeiboType weiboType, String responseContent) throws WeiboErrorException {
    if (responseContent.isEmpty()) {
      throw new WeiboErrorException(WeiboError.builder().errorCode(9999).errorMsg("无响应内容").build());
    }

    if (responseContent.startsWith("<xml>")) {
      //xml格式输出直接返回
      return responseContent;
    }

    WeiboError error = WeiboError.fromJson(responseContent, weiboType);
    if (error.getErrorCode() != 0) {
      throw new WeiboErrorException(error);
    }
    return responseContent;
  }
}
