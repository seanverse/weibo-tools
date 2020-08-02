package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.bean.group.GroupRuleType;
import vip.seanxq.weibo.mp.bean.group.WeiboUserGroup;

import java.util.List;

/**
 * <pre>
 *  用户标签管理相关接口
 *  文档位置：<a href="https://open.weibo.com/wiki/分组管理/">粉丝服务分组管理</a>
 *</pre>
 */
public interface WeiboFansGroupService {

  /**
   * <pre>
   * 创建分组标签
   * 接口url格式： https://m.api.weibo.com/2/messages/custom_rule/create.json
   * </pre>
   *
   * @param name 标签名字（30个字符以内）
   */
  WeiboUserGroup groupCreate(String name) throws WeiboErrorException;

  /**
   * <pre>
   * 获取公众号已创建的标签
   * 详情请见：<a href="https://open.weibo.com/wiki/获取自定义规则分组">获得分组列表</a>
   * 接口url格式： https://m.api.weibo.com/2/messages/custom_rule/get.json
   * </pre>
   * @param ruleType: 0为关键词分组，2为自定义分组；
   */
  List<WeiboUserGroup> groupGet(GroupRuleType ruleType) throws WeiboErrorException;

  /**
   * <pre>
   * 编辑标签
   * 接口url格式： https://m.api.weibo.com/2/messages/custom_rule/update.json
   * </pre>
   */
  Boolean groupUpdate(Long tagId, String name) throws WeiboErrorException;

  /**
   * <pre>
   * 删除标签   *
   * 接口url格式： https://m.api.weibo.com/2/messages/custom_rule/delete.json
   * </pre>
   */
  Boolean groupDelete(Long tagId) throws WeiboErrorException;

  /**
   * <pre>
   * 获取用户身上的分组列表
   * 接口url格式： https://m.api.weibo.com/2/messages/custom_rule/getid.json
   * </pre>
   * @Param followerid: 粉丝id follow_id
   * @return 分组Id的列表
   */
  List<Long> userGroupList(Long followerId) throws WeiboErrorException;

  /**
   * <pre>
   *   设置粉丝的分组
   * 接口url格式：https://m.api.weibo.com/2/messages/custom_rule/member/update.json
   * </pre>
   */
   Boolean userGroupUpdate(Long followerId, Long to_groupId) throws WeiboErrorException;
}
