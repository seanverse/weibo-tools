package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.SimplePostRequestExecutor;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboMpUserBlacklistService;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUserBlacklistGetResult;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author miller
 */
@RequiredArgsConstructor
public class WeiboMpUserBlacklistServiceImpl implements WeiboMpUserBlacklistService {
  private final WeiboMpService wxMpService;

  @Override
  public WeiboMpUserBlacklistGetResult getBlacklist(String nextOpenid) throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("begin_openid", nextOpenid);
    String responseContent = this.wxMpService.execute(SimplePostRequestExecutor.create(this.wxMpService.getRequestHttp()),
      WeiboMpApiUrl.UserBlacklist.GETBLACKLIST, jsonObject.toString());
    return WeiboMpUserBlacklistGetResult.fromJson(responseContent);
  }

  @Override
  public void pushToBlacklist(List<String> openidList) throws WeiboErrorException {
    Map<String, Object> map = new HashMap<>(2);
    map.put("openid_list", openidList);
    this.wxMpService.execute(SimplePostRequestExecutor.create(this.wxMpService.getRequestHttp()), WeiboMpApiUrl.UserBlacklist.BATCHBLACKLIST,
      WbMpGsonBuilder.create().toJson(map));
  }

  @Override
  public void pullFromBlacklist(List<String> openidList) throws WeiboErrorException {
    Map<String, Object> map = new HashMap<>(2);
    map.put("openid_list", openidList);
    this.wxMpService.execute(SimplePostRequestExecutor.create(this.wxMpService.getRequestHttp()), WeiboMpApiUrl.UserBlacklist.BATCHUNBLACKLIST,
      WbMpGsonBuilder.create().toJson(map));
  }
}
