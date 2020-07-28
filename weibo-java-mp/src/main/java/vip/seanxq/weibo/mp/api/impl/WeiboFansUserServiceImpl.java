package vip.seanxq.weibo.mp.api.impl;

import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboFansUserService;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUser;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUserList;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

/**
 * 粉丝用户信息操作
 *
 */
@RequiredArgsConstructor
public class WeiboFansUserServiceImpl implements WeiboFansUserService {
  private final WeiboMpService weiboMpService;


  @Override
  public WeiboFansUser userInfo(Long uid) throws WeiboErrorException {
    String responseContent = this.weiboMpService.get(WeiboMpApiUrl.FansUser.USER_GETUSERINFO, "uid=" + uid.toString());
    return WeiboFansUser.fromJson(responseContent);
  }
  @Override
  public WeiboFansUserList getSubscribers(Long next_uid) throws WeiboErrorException
  {
    String responseContent = this.weiboMpService.get(WeiboMpApiUrl.FansUser.USER_GETSUBSCRIBERS, next_uid == null ? null : "next_uid=" + next_uid.toString());
    return WeiboFansUserList.fromJson(responseContent);
  }
}
