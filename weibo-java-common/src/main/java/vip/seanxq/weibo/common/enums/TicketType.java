package vip.seanxq.weibo.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * <pre>
 * ticket类型枚举
 * Created by Binary Wang on 2018/11/18.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Getter
@RequiredArgsConstructor
public enum TicketType {
  /**
   * jsapi
   */
  JSAPI("jsapi"),
  /**
   * sdk
   */
  SDK("sdk");


  /**
   * type代码
   */
  private final String code;

}
