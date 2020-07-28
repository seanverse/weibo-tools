package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.bean.HttpDataParam;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboFansTagService;
import vip.seanxq.weibo.mp.bean.tag.TagRuleType;
import vip.seanxq.weibo.mp.bean.tag.WeiboUserTag;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.util.ArrayList;
import java.util.List;

/**
 * 关注用户的分组标签管理
 */
@RequiredArgsConstructor
public class WeiboFansTagServiceImpl implements WeiboFansTagService {
  private final WeiboMpService weiboMpService;

  @Override
  public WeiboUserTag tagCreate(String name) throws WeiboErrorException {
    String responseContent = this.weiboMpService.post(WeiboMpApiUrl.FansUser.TAGS_CREATE, name);
    return WeiboUserTag.fromJson(responseContent);
  }

  @Override
  public List<WeiboUserTag> tagGet(TagRuleType ruleType) throws WeiboErrorException {
    String responseContent = this.weiboMpService.get(WeiboMpApiUrl.FansUser.TAGS_GET, String.valueOf(ruleType.getRule_type()));
    return WeiboUserTag.listFromJson(responseContent);
  }

  @Override
  public Boolean tagUpdate(Long id, String name) throws WeiboErrorException {
    HttpDataParam param = new HttpDataParam();
    param.put("id", id.toString());
    param.put("name", name);
    String responseContent = this.weiboMpService.post(WeiboMpApiUrl.FansUser.TAGS_UPDATE, param.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }

    throw new WeiboErrorException(weiboError);
  }

  @Override
  public Boolean tagDelete(Long id) throws WeiboErrorException {
    HttpDataParam param = new HttpDataParam();
    param.put("id", id.toString());

    String responseContent = this.weiboMpService.post(WeiboMpApiUrl.FansUser.TAGS_DELETE, param.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }

    throw new WeiboErrorException(weiboError);
  }

  @Override
  public List<Long> userTagList(Long followerId) throws WeiboErrorException {
    HttpDataParam param = new HttpDataParam();
    param.put("follower_id", followerId.toString());

    String responseContent = this.weiboMpService.post(WeiboMpApiUrl.FansUser.TAGS_USERTAGLIST, param.toString());
    /** todo:seanx:文档中表述只返回一个id，需要API具体测试
    {
      "groupid": 123123123
    }
    */

    Long l = new JsonParser().parse(responseContent).getAsJsonObject().get("groupid").getAsLong();

    //预留list，等调试确认
    List<Long> list = new ArrayList<Long>();
    list.add(l);
    return list;

    //return WbMpGsonBuilder.create().fromJson(responseContent);
      /*new JsonParser().parse(responseContent).getAsJsonObject().get("tagid_list"),
      new TypeToken<List<Long>>() {
      }.getType());*/
  }

  @Override
  public Boolean userTagUpdate(Long followerId, Long to_groupId) throws WeiboErrorException {
    HttpDataParam param = new HttpDataParam();
    param.put("follower_id", followerId.toString());
    param.put("to_groupid", to_groupId.toString());
    String responseContent = this.weiboMpService.post(WeiboMpApiUrl.FansUser.TAGS_USERTAGUPDATE, param.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }
    throw new WeiboErrorException(weiboError);
  }
}
