package vip.seanxq.weibo.mp.bean.result;

import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author miller
 */
@Data
public class WeiboMpUserBlacklistGetResult implements Serializable {
  private static final long serialVersionUID = -8780216463588687626L;

  protected int total = -1;
  protected int count = -1;
  protected List<String> openidList = new ArrayList<>();
  protected String nextOpenid;

  public static WeiboMpUserBlacklistGetResult fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(json, WeiboMpUserBlacklistGetResult.class);
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
