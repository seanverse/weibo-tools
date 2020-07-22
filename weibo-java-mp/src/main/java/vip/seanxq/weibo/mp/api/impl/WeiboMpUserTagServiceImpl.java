package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboMpUserTagService;
import vip.seanxq.weibo.mp.bean.tag.WeiboTagListUser;
import vip.seanxq.weibo.mp.bean.tag.WeiboUserTag;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;
import org.apache.commons.lang3.StringUtils;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

import java.util.List;

/**
 * Created by Binary Wang on 2016/9/2.
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@RequiredArgsConstructor
public class WeiboMpUserTagServiceImpl implements WeiboMpUserTagService {
  private final WeiboMpService wxMpService;

  @Override
  public WeiboUserTag tagCreate(String name) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    JsonObject tagJson = new JsonObject();
    tagJson.addProperty("name", name);
    json.add("tag", tagJson);

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAGS_CREATE, json.toString());
    return WeiboUserTag.fromJson(responseContent);
  }

  @Override
  public List<WeiboUserTag> tagGet() throws WeiboErrorException {
    String responseContent = this.wxMpService.get(WeiboMpApiUrl.UserTag.TAGS_GET, null);
    return WeiboUserTag.listFromJson(responseContent);
  }

  @Override
  public Boolean tagUpdate(Long id, String name) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    JsonObject tagJson = new JsonObject();
    tagJson.addProperty("id", id);
    tagJson.addProperty("name", name);
    json.add("tag", tagJson);

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAGS_UPDATE, json.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }

    throw new WeiboErrorException(weiboError);
  }

  @Override
  public Boolean tagDelete(Long id) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    JsonObject tagJson = new JsonObject();
    tagJson.addProperty("id", id);
    json.add("tag", tagJson);

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAGS_DELETE, json.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }

    throw new WeiboErrorException(weiboError);
  }

  @Override
  public WeiboTagListUser tagListUser(Long tagId, String nextOpenid) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("tagid", tagId);
    json.addProperty("next_openid", StringUtils.trimToEmpty(nextOpenid));

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAG_GET, json.toString());
    return WeiboTagListUser.fromJson(responseContent);
  }

  @Override
  public boolean batchTagging(Long tagId, String[] openids) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("tagid", tagId);
    JsonArray openidArrayJson = new JsonArray();
    for (String openid : openids) {
      openidArrayJson.add(openid);
    }
    json.add("openid_list", openidArrayJson);

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAGS_MEMBERS_BATCHTAGGING, json.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }

    throw new WeiboErrorException(weiboError);
  }

  @Override
  public boolean batchUntagging(Long tagId, String[] openids) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("tagid", tagId);
    JsonArray openidArrayJson = new JsonArray();
    for (String openid : openids) {
      openidArrayJson.add(openid);
    }
    json.add("openid_list", openidArrayJson);

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAGS_MEMBERS_BATCHUNTAGGING, json.toString());
    WeiboError weiboError = WeiboError.fromJson(responseContent, WeiboType.MP);
    if (weiboError.getErrorCode() == 0) {
      return true;
    }

    throw new WeiboErrorException(weiboError);
  }

  @Override
  public List<Long> userTagList(String openid) throws WeiboErrorException {
    JsonObject json = new JsonObject();
    json.addProperty("openid", openid);

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.UserTag.TAGS_GETIDLIST, json.toString());

    return WbMpGsonBuilder.create().fromJson(
      new JsonParser().parse(responseContent).getAsJsonObject().get("tagid_list"),
      new TypeToken<List<Long>>() {
      }.getType());
  }
}
