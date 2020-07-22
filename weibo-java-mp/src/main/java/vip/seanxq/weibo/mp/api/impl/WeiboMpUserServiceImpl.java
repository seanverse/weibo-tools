package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboMpUserService;
import vip.seanxq.weibo.mp.bean.WeiboUserQuery;
import vip.seanxq.weibo.mp.bean.result.WeiboMpChangeOpenid;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUser;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUserList;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Binary Wang on 2016/7/21.
 *
 * @author BinaryWang
 */
@RequiredArgsConstructor
public class WeiboMpUserServiceImpl implements WeiboMpUserService {
  private final WeiboMpService wxMpService;

  @Override
  public void userUpdateRemark(String openid, String remark) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("openid", openid);
    json.addProperty("remark", remark);
    this.wxMpService.post(WeiboMpApiUrl.User.USER_INFO_UPDATE_REMARK_URL, json.toString());
  }

  @Override
  public WeiboMpUser userInfo(String openid) throws WeiboErrorException {
    return this.userInfo(openid, null);
  }

  @Override
  public WeiboMpUser userInfo(String openid, String lang) throws WeiboErrorException {
    lang = lang == null ? "zh_CN" : lang;
    String responseContent = this.wxMpService.get(WeiboMpApiUrl.User.USER_INFO_URL, "openid=" + openid + "&lang=" + lang);
    return WeiboMpUser.fromJson(responseContent);
  }

  @Override
  public WeiboMpUserList userList(String nextOpenid) throws WeiboErrorException {
    String responseContent = this.wxMpService.get(WeiboMpApiUrl.User.USER_GET_URL, nextOpenid == null ? null : "next_openid=" + nextOpenid);
    return WeiboMpUserList.fromJson(responseContent);
  }

  @Override
  public List<WeiboMpChangeOpenid> changeOpenid(String fromAppid, List<String> openidList) throws WeiboErrorException {
    Map<String, Object> map = new HashMap<>(2);
    map.put("from_appid", fromAppid);
    map.put("openid_list", openidList);
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.User.USER_CHANGE_OPENID_URL, WbMpGsonBuilder.create().toJson(map));

    return WeiboMpChangeOpenid.fromJsonList(responseContent);
  }

  @Override
  public List<WeiboMpUser> userInfoList(List<String> openidList)
    throws WeiboErrorException {
    return this.userInfoList(new WeiboUserQuery(openidList));
  }

  @Override
  public List<WeiboMpUser> userInfoList(WeiboUserQuery userQuery) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.User.USER_INFO_BATCH_GET_URL, userQuery.toJsonString());
    return WeiboMpUser.fromJsonList(responseContent);
  }

}
