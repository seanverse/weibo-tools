package vip.seanxq.weibo.common.bean;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.extern.slf4j.XSlf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;


@Data
@RequiredArgsConstructor
@Builder
@Slf4j
@EqualsAndHashCode(callSuper = false)
public class HttpDataParam extends HashMap<String, String> implements Serializable {

  /**
   * <pre>
   *   k/v拼成k1=v1&k2=v2...这样的http query查询字符串组合。即生成 application/x-www-form-urlencoded：数据被编码为名称/值对。
   *   编码格式就是application/x-www-form-urlencoded（将键值对的参数用&连接起来，如果有空格，将空格转换为+加号；有特殊符号，将特殊符号转换为ASCII HEX值）。
   * </pre>
   * @return http query查询组合字符串
   */
  public String toParamString()  {
    StringJoiner builder = new StringJoiner("&");
    if (this.size() > 0) {
      try {
        for (Map.Entry<String, String> entry : this.entrySet()) {
          String v = entry.getValue();
          if (StringUtils.isNotEmpty(v))
            builder.add(String.format("%s=%s", entry.getKey(),
              URLEncoder.encode(v, "UTF-8")));
        }
      }catch (UnsupportedEncodingException u)
      {//UTF-8的编码错误基本不发生，所以直接吃掉，让使用者更简单
        log.error("encode UTF-8 error.", u);
      }
    }
    return builder.toString();
  }

  @Override
  public String toString()
  {
     return this.toParamString();
  }

}
