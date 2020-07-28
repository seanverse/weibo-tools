package vip.seanxq.weibo.mp.bean.result;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 关注者列表
 *
 */
@Data
public class WeiboFansUserList implements Serializable {
  private static final long serialVersionUID = 1389073042674901032L;

  //总订阅数
  protected long total = -1;
  //本次拉取的用户数
  protected int count = -1;
  //本次拉取的粉丝用户id集合
  protected List<String> uids = new ArrayList<>();
  /**
   * 1、当账号的订阅者数量超过10000时，可通过填写next_uid的值，从而多次拉取列表的方式来满足需求；
   * 具体而言，就是在调用接口时，将上一次调用得到的返回中的next_uid值，作为下一次调用中的next_uid值；
   *
   * 2、如果一次拉去的用户数不到10000，则next_uid为""，空字符串；
   * 3、access_token中的uid已经开启开发模式；
   */
  protected String nextUid;

  public static WeiboFansUserList fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboFansUserList.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
