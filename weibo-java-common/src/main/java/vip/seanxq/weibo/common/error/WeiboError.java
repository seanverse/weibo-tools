package vip.seanxq.weibo.common.error;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import vip.seanxq.weibo.common.WeiboType;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

import java.io.Serializable;

/**
 * 微博错误码.
 */
@Data
@Builder
public class WeiboError implements Serializable {
  private static final long serialVersionUID = 7869786563361406291L;

  /**
   * 微博返回错误信息时都会给出调用出错的url
   */
  private String request;

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
   * 微博接口返回的错误原始信息（英文）. 这才是服务器返回的信息
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

    //todo:seanx;WeiboMpErrorMsgEnum　里面的还未对errormsg进行对应翻译，
    // 微博的文档并没有详细列出code对应的中文错误，只能就常见的错误进行中文处理
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
