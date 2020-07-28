package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUser;
import vip.seanxq.weibo.mp.bean.result.WeiboFansUserList;

import java.util.List;

/**
 * 订阅用户的相关操作接口.
 */
public interface WeiboFansUserService {
   /**
   * <pre>
   * 获取用户基本信息（语言为默认的zh_CN 简体）
    * 1、查看的用户必须是access_token中uid的粉丝；
    * 2、access_token中的uid启用开发模式；
   * 详情请见: https://open.weibo.com/wiki/获取用户基础信息
   * http请求方式: GET
   * 接口地址：https://api.weibo.com/2/eps/user/info.json
   * </pre>
   *
   * @param uid 粉丝的uid
   */
  WeiboFansUser userInfo(Long uid) throws WeiboErrorException;


  /**
   * <pre>
   * 开发者可以通过本接口来获取订阅用户uid列表，一次拉取调用最多拉取10000个关注者的uid，可以通过多次拉取的方式来满足需求。
   * 详情请见: https://open.weibo.com/wiki/获取订阅者列表
   * http请求方式: GET
   * 接口地址：https://m.api.weibo.com/2/messages/subscribers/get.json
   * </pre>
   * @param next_uid 第一个拉取的uid，不填默认从头开始拉取。
   */
  WeiboFansUserList getSubscribers(Long next_uid) throws WeiboErrorException;

}
