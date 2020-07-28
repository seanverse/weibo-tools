package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.bean.HttpDataParam;
import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeibCustomMenuService;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

/**
 * WeibCustomMenuServiceImpl
 */
@Slf4j
@RequiredArgsConstructor
public class WeibCustomMenuServiceImpl implements WeibCustomMenuService {
  private final WeiboMpService weiboMpService;

  @Override
  public Boolean menuCreate(WeiboMenu menu) throws WeiboErrorException {
    String menuJson = menu.toJson();
    WeiboMpApiUrl.Menu url = WeiboMpApiUrl.Menu.MENU_CREATE;

    log.debug("开始创建菜单：{}", menuJson);
    HttpDataParam param = new HttpDataParam();
    param.put("menus", menuJson);
    String result = this.weiboMpService.post(url, param.toParamString());
    log.debug("创建菜单：{},结果：{}", menuJson, result);

    return new JsonParser().parse(result).getAsJsonObject().get("result").getAsBoolean();
  }

  @Override
  public Boolean menuDelete() throws WeiboErrorException {
    String result = this.weiboMpService.get(WeiboMpApiUrl.Menu.MENU_DELETE, null);
    log.debug("删除菜单结果：{}", result);
    return new JsonParser().parse(result).getAsJsonObject().get("result").getAsBoolean();
  }

  @Override
  public WeiboMenu menuGet() throws WeiboErrorException {
      String resultContent = this.weiboMpService.get(WeiboMpApiUrl.Menu.MENU_GET, null);
      return WeiboMenu.fromJson(resultContent);
  }

}
