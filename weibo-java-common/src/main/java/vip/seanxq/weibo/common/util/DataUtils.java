package vip.seanxq.weibo.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.StringJoiner;

/**
 * <pre>
 *  数据处理工具类
 *  Created by BinaryWang on 2018/5/8.
 * </pre>
 *
 * @author <a href="https://github.com/binarywang">Binary Wang</a>
 */
public class DataUtils {
  /**
   * 将数据中包含的secret字符使用星号替换，防止日志打印时被输出
   */
  public static <E> E handleDataWithSecret(E data) {
    E dataForLog = data;
    if (data instanceof String && StringUtils.contains((String) data, "&secret=")) {
      dataForLog = (E) RegExUtils.replaceAll((String) data, "&secret=\\w+&", "&secret=******&");
    }
    return dataForLog;
  }

  /**
   * <pre>
   *   通过JsonObject的第一层属性拼成k1=v1&k2=v2...这样的http query查询字符串组合。
   *   即生成 application/x-www-form-urlencoded：数据被编码为名称/值对。
   *   如果第二层是一个对象，会被处理于json字符串，即 &K3={..} 这样。这种情况也符合大多样的API的传参要求
   * </pre>
   *
   * @param json 对象
   * @return http query查询组合字符串
   */
  public static String JsonObjectToHttpParam(JsonObject json) throws UnsupportedEncodingException {
    StringJoiner builder = new StringJoiner("&");
    if (json != null && (json.size() > 0)) {
      for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
        JsonElement el = entry.getValue();
        if (el != null && el.isJsonNull())
          builder.add(String.format("%s = %s", entry.getKey(),
            URLEncoder.encode(el.getAsString(), "UTF-8")));
      }
    }
    return builder.toString();
  }
}
