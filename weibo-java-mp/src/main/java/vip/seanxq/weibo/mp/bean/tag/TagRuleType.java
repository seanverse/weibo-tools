package vip.seanxq.weibo.mp.bean.tag;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TagRuleType {
  /**
   * 0为关键词分组
   */
  BY_KEYWORD(0),
  /**
   * 2为自定义分组
   */
  BY_CUSTOM_GROUP(2);

  private final int rule_type;
}
