package vip.seanxq.weibo.common.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;

/**
 * WxErrorException处理器.
 *
 * @author Daniel Qian
 */
public interface WeiboErrorExceptionHandler {

  void handle(WeiboErrorException e);

}
