package vip.seanxq.weibo.common.service;

import vip.seanxq.weibo.common.error.WeiboErrorException;

/**
 * 微博服务接口.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 * @date 2020-04-25
 */
public interface WeiboService {
  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有微博API中的GET请求.
   *
   * @param queryParam 参数
   * @param url        请求接口地址
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String get(String url, String queryParam) throws WeiboErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有微博API中的POST请求.
   *
   * @param postData 请求参数json值
   * @param url      请求接口地址
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String post(String url, String postData) throws WeiboErrorException;

  /**
   * 当本Service没有实现某个API的时候，可以用这个，针对所有微博API中的POST请求.
   *
   * @param url 请求接口地址
   * @param obj 请求对象
   * @return 接口响应字符串
   * @throws WeiboErrorException 异常
   */
  String post(String url, Object obj) throws WeiboErrorException;
}
