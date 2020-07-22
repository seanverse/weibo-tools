package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUserBlacklistGetResult;

import java.util.List;

/**
 * @author miller
 */
public interface WeiboMpUserBlacklistService {
  /**
   * <pre>
   * 获取公众号的黑名单列表
   * 详情请见http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA&token=&lang=zh_CN
   * </pre>
   */
  WeiboMpUserBlacklistGetResult getBlacklist(String nextOpenid) throws WeiboErrorException;

  /**
   * <pre>
   *   拉黑用户
   *   详情请见http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA&token=&lang=zh_CN
   * </pre>
   */
  void pushToBlacklist(List<String> openidList) throws WeiboErrorException;

  /**
   * <pre>
   *   取消拉黑用户
   *   详情请见http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1471422259_pJMWA&token=&lang=zh_CN
   * </pre>
   */
  void pullFromBlacklist(List<String> openidList) throws WeiboErrorException;
}
