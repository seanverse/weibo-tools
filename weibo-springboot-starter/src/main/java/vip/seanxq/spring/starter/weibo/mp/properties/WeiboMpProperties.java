package vip.seanxq.spring.starter.weibo.mp.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import vip.seanxq.weibo.common.enums.WbMessageFormat;

import java.io.Serializable;

import static vip.seanxq.spring.starter.weibo.mp.properties.WeiboMpProperties.PREFIX;
import static vip.seanxq.spring.starter.weibo.mp.properties.WeiboMpProperties.StorageType.memory;


/**
 * 微博接入相关配置属性.
 *
 * @author someone
 */
@Data
@ConfigurationProperties(PREFIX)
public class WeiboMpProperties {
  public static final String PREFIX = "weibo.mp";

  /**
   * 设置微博公众号的appid.
   */
  private String appId;

  /**
   * 设置微博公众号的app secret.
   */
  private String secret;

  /**
   * 设置微博公众号的token.
   */
  private String token;

  /**
   * 设置微博公众号的EncodingAESKey.
   */
  private String aesKey;

  /**
   * 设置消息的格式，weibo默认为json，兼容时为xml
   */
  private WbMessageFormat messageFormat = WbMessageFormat.XML;

  /**
   * 存储策略
   */
  private ConfigStorage configStorage = new ConfigStorage();

  @Data
  public static class ConfigStorage implements Serializable {
    private static final long serialVersionUID = 4815731027000065434L;

    /**
     * 存储类型.
     */
    private StorageType type = memory;



    /**
     * 指定key前缀.
     */
    private String keyPrefix = "weibo";

    /**
     * redis连接配置.
     */
    private RedisProperties redis = new RedisProperties();

    /**
     * http客户端类型.
     */
    private HttpClientType httpClientType = HttpClientType.httpclient;

    /**
     * http代理主机.
     */
    private String httpProxyHost;

    /**
     * http代理端口.
     */
    private Integer httpProxyPort;

    /**
     * http代理用户名.
     */
    private String httpProxyUsername;

    /**
     * http代理密码.
     */
    private String httpProxyPassword;

  }

  public enum StorageType {
    /**
     * 内存.
     */
    memory,
    /**
     * jedis.
     */
    redis,
    /**
     * redis(JedisClient).
     */
    jedis,
    /**
     * redis(RedisTemplate).
     */
    redistemplate
  }

  public enum HttpClientType {
    /**
     * HttpClient.
     */
    httpclient,
    /**
     * OkHttp.
     */
    okhttp,
    /**
     * JoddHttp.
     */
    joddhttp
  }

  @Data
  public static class RedisProperties implements Serializable {
    private static final long serialVersionUID = -5924815351660074401L;

    /**
     * 主机地址.
     */
    private String host = "127.0.0.1";

    /**
     * 端口号.
     */
    private int port = 6379;

    /**
     * 密码.
     */
    private String password;

    /**
     * 超时.
     */
    private int timeout = 2000;

    /**
     * 数据库.
     */
    private int database = 0;

    private Integer maxActive;
    private Integer maxIdle;
    private Integer maxWaitMillis;
    private Integer minIdle;
  }

}
