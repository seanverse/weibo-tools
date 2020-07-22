package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeibMpMenuService;
import vip.seanxq.weibo.mp.bean.menu.WeiboMpGetSelfMenuInfoResult;
import vip.seanxq.weibo.mp.bean.menu.WeiboMpMenu;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

/**
 * Created by Binary Wang on 2016/7/21.
 *
 * @author Binary Wang
 */
@Slf4j
@RequiredArgsConstructor
public class WeibMpMenuServiceImpl implements WeibMpMenuService {
  private final WeiboMpService wxMpService;

  @Override
  public String menuCreate(WeiboMenu menu) throws WeiboErrorException {
    String menuJson = menu.toJson();
    WeiboMpApiUrl.Menu url = WeiboMpApiUrl.Menu.MENU_CREATE;
    if (menu.getMatchRule() != null) {
      url = WeiboMpApiUrl.Menu.MENU_ADDCONDITIONAL;
    }

    log.debug("开始创建菜单：{}", menuJson);

    String result = this.wxMpService.post(url, menuJson);
    log.debug("创建菜单：{},结果：{}", menuJson, result);

    if (menu.getMatchRule() != null) {
      return new JsonParser().parse(result).getAsJsonObject().get("menuid").getAsString();
    }

    return null;
  }

  @Override
  public String menuCreate(String json) throws WeiboErrorException {
    JsonParser jsonParser = new JsonParser();
    JsonObject jsonObject = jsonParser.parse(json).getAsJsonObject();
    WeiboMpApiUrl.Menu url = WeiboMpApiUrl.Menu.MENU_CREATE;
    if (jsonObject.get("matchrule") != null) {
      url = WeiboMpApiUrl.Menu.MENU_ADDCONDITIONAL;
    }

    String result = this.wxMpService.post(url, json);
    if (jsonObject.get("matchrule") != null) {
      return jsonParser.parse(result).getAsJsonObject().get("menuid").getAsString();
    }

    return null;
  }

  @Override
  public void menuDelete() throws WeiboErrorException {
    String result = this.wxMpService.get(WeiboMpApiUrl.Menu.MENU_DELETE, null);
    log.debug("删除菜单结果：{}", result);
  }

  @Override
  public void menuDelete(String menuId) throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("menuid", menuId);
    String result = this.wxMpService.post(WeiboMpApiUrl.Menu.MENU_DELCONDITIONAL, jsonObject.toString());
    log.debug("根据MeunId({})删除个性化菜单结果：{}", menuId, result);
  }

  @Override
  public WeiboMpMenu menuGet() throws WeiboErrorException {
    try {
      String resultContent = this.wxMpService.get(WeiboMpApiUrl.Menu.MENU_GET, null);
      return WeiboMpMenu.fromJson(resultContent);
    } catch (WeiboErrorException e) {
      // 46003 不存在的菜单数据
      if (e.getError().getErrorCode() == 46003) {
        return null;
      }
      throw e;
    }
  }

  @Override
  public WeiboMenu menuTryMatch(String userid) throws WeiboErrorException {
    JsonObject jsonObject = new JsonObject();
    jsonObject.addProperty("user_id", userid);
    try {
      String resultContent = this.wxMpService.post(WeiboMpApiUrl.Menu.MENU_TRYMATCH, jsonObject.toString());
      return WeiboMenu.fromJson(resultContent);
    } catch (WeiboErrorException e) {
      // 46003 不存在的菜单数据；46002 不存在的菜单版本
      if (e.getError().getErrorCode() == 46003
        || e.getError().getErrorCode() == 46002) {
        return null;
      }
      throw e;
    }
  }

  @Override
  public WeiboMpGetSelfMenuInfoResult getSelfMenuInfo() throws WeiboErrorException {
    String resultContent = this.wxMpService.get(WeiboMpApiUrl.Menu.GET_CURRENT_SELFMENU_INFO, null);
    return WeiboMpGetSelfMenuInfoResult.fromJson(resultContent);
  }
}
