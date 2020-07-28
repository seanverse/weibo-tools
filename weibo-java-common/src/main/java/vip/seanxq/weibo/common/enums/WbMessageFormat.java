package vip.seanxq.weibo.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 微博消息推送服务完全兼容“微信XML格式”，以方便基于微信公众平台做了开发的第三方能够更为顺畅得迁移；
 * 第三方可以通过http://open.weibo.com/wiki/Eps/push/set_format 接口来选择自己需要的格式是XML还是JSON
 */
@Getter
@RequiredArgsConstructor
public enum WbMessageFormat {
  /**
   * xml
   */
  XML("xml"),
  /**
   * json
   */
  JSON("json");
  /**
   * format
   */
  private final String data_format;
}
