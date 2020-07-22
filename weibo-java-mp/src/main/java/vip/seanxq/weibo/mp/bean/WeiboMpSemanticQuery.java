package vip.seanxq.weibo.mp.bean;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * 语义理解查询用对象
 * <p>
 * http://mp.weixin.qq.com/wiki/index.php?title=语义理解
 *
 * @author Daniel Qian
 */
@Data
public class WeiboMpSemanticQuery implements Serializable {
  private static final long serialVersionUID = 7685873048199870690L;

  private String query;
  private String category;
  private Float latitude;
  private Float longitude;
  private String city;
  private String region;
  private String appid;
  private String uid;

  public String toJson() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}
