package vip.seanxq.weibo.common.error;

import lombok.Builder;
import lombok.Data;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 微博错误码.
 */
@Data
@Builder
public class WeiboError implements Serializable {
  private static final long serialVersionUID = 7869786563361406291L;

  /**
   * 微博错误代码.
   */
  private int errorCode;

  /**
   * 微博错误信息.
   * （如果可以翻译为中文，就为中文）
   */
  private String errorMsg;

  /**
   * 微博接口返回的错误原始信息（英文）.
   */
  private String errorMsgEn;

  private String json;

  public static WeiboError fromJson(String json) {
    return fromJson(json, null);
  }

  public static WeiboError fromJson(String json, WeiboType type) {
    final WeiboError weiboError = WeiboGsonBuilder.create().fromJson(json, WeiboError.class);
    if (weiboError.getErrorCode() == 0 || type == null) {
      return weiboError;
    }

    if (StringUtils.isNotEmpty(weiboError.getErrorMsg())) {
      weiboError.setErrorMsgEn(weiboError.getErrorMsg());
    }

    switch (type) {
      case MP: {
        final String msg = WeiboMpErrorMsgEnum.findMsgByCode(weiboError.getErrorCode());
        if (msg != null) {
          weiboError.setErrorMsg(msg);
        }
        break;
      }
      case LightApp: {
        final String msg = WeiboLightAppErrorMsgEnum.findMsgByCode(weiboError.getErrorCode());
        if (msg != null) {
          weiboError.setErrorMsg(msg);
        }
        break;
      }
      default:
        return weiboError;
    }

    return weiboError;
  }

  @Override
  public String toString() {
    if (this.json == null) {
      return "错误代码：" + this.errorCode + ", 错误信息：" + this.errorMsg;
    }

    return "错误代码：" + this.errorCode + ", 错误信息：" + this.errorMsg + "，微博原始报文：" + this.json;
  }

}
