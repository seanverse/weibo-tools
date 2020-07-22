package vip.seanxq.weibo.mp.bean.result;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;

/**
 * 语义理解查询结果对象
 * <p>
 * http://mp.weixin.qq.com/wiki/index.php?title=语义理解
 *
 * @author Daniel Qian
 */
@Data
public class WeiboMpSemanticQueryResult implements Serializable {
  private static final long serialVersionUID = 4811088544804441365L;

  private String query;
  private String type;
  private String semantic;
  private String result;
  private String answer;
  private String text;

  public static WeiboMpSemanticQueryResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpSemanticQueryResult.class);
  }

}
