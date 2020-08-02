package vip.seanxq.weibo.common.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.RegExUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
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
  public static String JsonObjectToHttpParam(JsonObject json) {
    StringJoiner builder = new StringJoiner("&");
    if (json != null && (json.size() > 0)) {
      try {
        for (Map.Entry<String, JsonElement> entry : json.entrySet()) {
          JsonElement el = entry.getValue();
          if (el != null && el.isJsonNull())
            builder.add(String.format("%s = %s", entry.getKey(),
              URLEncoder.encode(el.getAsString(), "UTF-8")));
        }
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException(e.getMessage());
      }
    }

    return builder.toString();
  }

  //类似Thu May 18 2017 00:00:00 GMT+0800 (中国标准时间)格式的时间转换成2017/05/18 或取其时分秒，方法如下：

  /**
   * @param gmtStr Thu May 18 2017 00:00:00 GMT+0800 (中国标准时间)
   * @return 年月日;
   */
  public static String parseDate(String gmtStr) {
    gmtStr = gmtStr.replace("GMT", "").replaceAll("\\(.*\\)", "");
    //将字符串转化为date类型，格式2016-10-12
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
    Date dateTrans = null;
    try {
      dateTrans = format.parse(gmtStr);
      return new SimpleDateFormat("yyyy-MM-dd").format(dateTrans).replace("-","/");
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return gmtStr;

  }

  /**
   * @param gmtStr "Tue Jul 12 12:10:11 GMT+08:00 2016";
   * @return 时分秒
   */
  public static String parseTime(String gmtStr) {

    gmtStr = gmtStr.replace("GMT", "").replaceAll("\\(.*\\)", "");
    SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd yyyy HH:mm:ss z", Locale.ENGLISH);
    Date dateTrans = null;
    try {
      dateTrans = format.parse(gmtStr);
      return new SimpleDateFormat("HH:mm:ss").format(dateTrans);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return gmtStr;
  }
}
