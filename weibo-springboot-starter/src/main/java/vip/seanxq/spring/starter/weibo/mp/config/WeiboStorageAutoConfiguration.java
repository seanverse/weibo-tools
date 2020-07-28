package vip.seanxq.spring.starter.weibo.mp.config;

import vip.seanxq.spring.starter.weibo.mp.properties.WeiboMpProperties;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.redis.JedisWeiboRedisOps;
import vip.seanxq.weibo.common.redis.RedisTemplateWeiboRedisOps;
import vip.seanxq.weibo.common.redis.WeiboRedisOps;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.mp.config.impl.WeiboDefaultConfigImpl;
import vip.seanxq.weibo.mp.config.impl.WeiboRedisConfigImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 微博公众号存储策略自动配置.
 *
 * @author someone
 */
@Configuration
@RequiredArgsConstructor
public class WeiboStorageAutoConfiguration {

  private final ApplicationContext applicationContext;

  private final WeiboMpProperties weiboMpProperties;

  @Value("${weibo.mp.config-storage.redis.host:")
  private String redisHost;

  @Value("${wx.mp.configStorage.redis.host:")
  private String redisHost2;

  @Bean
  @ConditionalOnMissingBean(WeiboConfigStorage.class)
  public WeiboConfigStorage wxMpConfigStorage() {
    WeiboMpProperties.StorageType type = weiboMpProperties.getConfigStorage().getType();
    WeiboConfigStorage config;
    if (type == WeiboMpProperties.StorageType.redis || type == WeiboMpProperties.StorageType.jedis) {
      config = wxMpInJedisConfigStorage();
    } else if (type == WeiboMpProperties.StorageType.redistemplate) {
      config = wxMpInRedisTemplateConfigStorage();
    } else {
      config = wxMpInMemoryConfigStorage();
    }
    return config;
  }

  private WeiboConfigStorage wxMpInMemoryConfigStorage() {
    WeiboDefaultConfigImpl config = new WeiboDefaultConfigImpl();
    setWxMpInfo(config);
    return config;
  }

  private WeiboConfigStorage wxMpInJedisConfigStorage() {
    JedisPool jedisPool;
    if (StringUtils.isNotEmpty(redisHost) || StringUtils.isNotEmpty(redisHost2)) {
      jedisPool = getJedisPool();
    } else {
      jedisPool = applicationContext.getBean(JedisPool.class);
    }
    WeiboRedisOps redisOps = new JedisWeiboRedisOps(jedisPool);
    WeiboRedisConfigImpl wxMpRedisConfig = new WeiboRedisConfigImpl(redisOps, weiboMpProperties.getConfigStorage().getKeyPrefix());
    setWxMpInfo(wxMpRedisConfig);
    return wxMpRedisConfig;
  }

  private WeiboConfigStorage wxMpInRedisTemplateConfigStorage() {
    StringRedisTemplate redisTemplate = applicationContext.getBean(StringRedisTemplate.class);
    WeiboRedisOps redisOps = new RedisTemplateWeiboRedisOps(redisTemplate);
    WeiboRedisConfigImpl wxMpRedisConfig = new WeiboRedisConfigImpl(redisOps, weiboMpProperties.getConfigStorage().getKeyPrefix());
    setWxMpInfo(wxMpRedisConfig);
    return wxMpRedisConfig;
  }

  private void setWxMpInfo(WeiboDefaultConfigImpl config) {
    WeiboMpProperties properties = weiboMpProperties;
    WeiboMpProperties.ConfigStorage configStorageProperties = properties.getConfigStorage();
    config.setAppId(properties.getAppId());
    config.setSecret(properties.getSecret());
    config.setToken(properties.getToken());
    config.setAesKey(properties.getAesKey());
    config.setMessageFormat(properties.getMessageFormat());

    config.setHttpProxyHost(configStorageProperties.getHttpProxyHost());
    config.setHttpProxyUsername(configStorageProperties.getHttpProxyUsername());
    config.setHttpProxyPassword(configStorageProperties.getHttpProxyPassword());
    if (configStorageProperties.getHttpProxyPort() != null) {
      config.setHttpProxyPort(configStorageProperties.getHttpProxyPort());
    }
  }

  private JedisPool getJedisPool() {
    WeiboMpProperties.ConfigStorage storage = weiboMpProperties.getConfigStorage();
    WeiboMpProperties.RedisProperties redis = storage.getRedis();

    JedisPoolConfig config = new JedisPoolConfig();
    if (redis.getMaxActive() != null) {
      config.setMaxTotal(redis.getMaxActive());
    }
    if (redis.getMaxIdle() != null) {
      config.setMaxIdle(redis.getMaxIdle());
    }
    if (redis.getMaxWaitMillis() != null) {
      config.setMaxWaitMillis(redis.getMaxWaitMillis());
    }
    if (redis.getMinIdle() != null) {
      config.setMinIdle(redis.getMinIdle());
    }
    config.setTestOnBorrow(true);
    config.setTestWhileIdle(true);

    return new JedisPool(config, redis.getHost(), redis.getPort(), redis.getTimeout(), redis.getPassword(),
      redis.getDatabase());
  }
}
