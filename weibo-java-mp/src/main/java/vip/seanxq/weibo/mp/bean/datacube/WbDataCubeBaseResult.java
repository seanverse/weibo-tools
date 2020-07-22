package vip.seanxq.weibo.mp.bean.datacube;

import java.io.Serializable;

import com.google.gson.JsonParser;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 *  统计接口的共用属性类.
 *  Created by Binary Wang on 2016/8/25.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
@Data
public abstract class WbDataCubeBaseResult implements Serializable {
  private static final long serialVersionUID = 8780389911053297600L;
  protected static final JsonParser JSON_PARSER = new JsonParser();

  /**
   * ref_date.
   * 数据的日期，需在begin_date和end_date之间
   */
  @SerializedName("ref_date")
  private String refDate;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

}
