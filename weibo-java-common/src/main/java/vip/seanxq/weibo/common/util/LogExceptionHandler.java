package vip.seanxq.weibo.common.util;

import vip.seanxq.weibo.common.api.WeiboErrorExceptionHandler;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LogExceptionHandler implements WeiboErrorExceptionHandler {

  private Logger log = LoggerFactory.getLogger(WeiboErrorExceptionHandler.class);

  @Override
  public void handle(WeiboErrorException e) {

    this.log.error("Error happens", e);

  }

}
