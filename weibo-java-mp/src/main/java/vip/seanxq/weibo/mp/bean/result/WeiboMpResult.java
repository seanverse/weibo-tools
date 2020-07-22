package vip.seanxq.weibo.mp.bean.result;

import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * 基础的微博公众号平台请求结果.
 */
@Data
public class WeiboMpResult implements Serializable {
  private static final long serialVersionUID = 2101652152604850904L;
  protected String errcode;
  protected String errmsg;

  /**
   * 请求是否成功.
   */
  public boolean isSuccess() {
    return StringUtils.equalsIgnoreCase(errcode, "0");
  }

  public static WeiboMpResult fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboMpResult.class);
  }

  @Override
  public String toString() {
    return WeiboGsonBuilder.create().toJson(this);
  }
}
