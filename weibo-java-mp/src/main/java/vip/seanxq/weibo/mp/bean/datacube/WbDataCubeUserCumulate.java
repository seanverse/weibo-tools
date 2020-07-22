package vip.seanxq.weibo.mp.bean.datacube;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import lombok.Data;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 * 累计用户数据接口的返回JSON数据包
 * 详情查看文档：<a href="http://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421141082&token=&lang=zh_CN">用户分析数据接口</a>
 * </pre>
 *
 * @author BinaryWang
 */
@Data
public class WbDataCubeUserCumulate implements Serializable {
  private static final JsonParser JSON_PARSER = new JsonParser();

  private static final long serialVersionUID = -3570981300225093657L;

  private Date refDate;

  private Integer cumulateUser;

  public static List<WbDataCubeUserCumulate> fromJson(String json) {
    return WbMpGsonBuilder.create().fromJson(
      JSON_PARSER.parse(json).getAsJsonObject().get("list"),
      new TypeToken<List<WbDataCubeUserCumulate>>() {
      }.getType());
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }
}
