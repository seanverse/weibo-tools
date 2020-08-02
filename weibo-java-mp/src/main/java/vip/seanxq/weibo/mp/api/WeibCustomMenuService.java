package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.error.WeiboErrorException;

/**
 * 菜单相关操作接口.
 */
public interface WeibCustomMenuService {

  /**
   * <pre>
   * 自定义菜单创建接口
   * Http POST curl "https://m.api.weibo.com/2/messages/menu/create.json?access_token=ACCESS_TOKEN" -d 'menus={ }'
   * </pre>
   */
  Boolean menuCreate(WeiboMenu menu) throws WeiboErrorException;

  /**
   * <pre>
   * 自定义菜单删除接口
   * http POST "https://m.api.weibo.com/2/messages/menu/delete.json" -d 'access_token=ACCESS_TOKEN'
   * </pre>
   */
  Boolean menuDelete() throws WeiboErrorException;

  /**
   * <pre>
   * 自定义菜单查询接口
   * http get: https://m.api.weibo.com/2/messages/menu/show.json?access_token=ACCESS_TOKEN
   * </pre>
   */
  WeiboMenu menuGet() throws WeiboErrorException;

}
