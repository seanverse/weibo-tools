package vip.seanxq.weibo.common.util.http;

import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboErrorException;

import java.io.IOException;

/**
 * http请求执行器.
 *
 * @param <T> 返回值类型
 * @param <E> 请求参数类型
 * @author Daniel Qian
 */
public interface RequestExecutor<T, E> {

  /**
   * 执行http请求.
   *
   * @param uri    uri
   * @param data   数据
   * @param weiboType 微博模块类型
   * @return 响应结果
   * @throws WeiboErrorException 自定义异常
   * @throws IOException      io异常
   */
  T execute(String uri, E data, WeiboType weiboType) throws WeiboErrorException, IOException;

  /**
   * 执行http请求.
   *
   * @param uri     uri
   * @param data    数据
   * @param handler http响应处理器
   * @param weiboType  微博模块类型
   * @throws WeiboErrorException 自定义异常
   * @throws IOException      io异常
   */
  void execute(String uri, E data, ResponseHandler<T> handler, WeiboType weiboType) throws WeiboErrorException, IOException;
}
