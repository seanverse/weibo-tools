package vip.seanxq.weibo.common.bean;

import lombok.Data;
import vip.seanxq.weibo.common.util.json.WeiboGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 网络检测.
 * @author billytomato
 */
@Data
public class WeiboNetCheckResult implements Serializable {

  private static final long serialVersionUID = 6918924418847404172L;

  private List<WxNetCheckDnsInfo> dnsInfos = new ArrayList<>();
  private List<WxNetCheckPingInfo> pingInfos = new ArrayList<>();

  public static WeiboNetCheckResult fromJson(String json) {
    return WeiboGsonBuilder.create().fromJson(json, WeiboNetCheckResult.class);
  }

  @Data
  public static class WxNetCheckDnsInfo implements Serializable{
    private static final long serialVersionUID = 82631178029516008L;
    private String ip;
    private String realOperator;
  }

  @Data
  public static class WxNetCheckPingInfo implements Serializable{
    private static final long serialVersionUID = -1871970825359178319L;
    private String ip;
    private String fromOperator;
    private String packageLoss;
    private String time;
  }
}


