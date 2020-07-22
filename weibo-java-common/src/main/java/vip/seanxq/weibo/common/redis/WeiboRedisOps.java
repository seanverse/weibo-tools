package vip.seanxq.weibo.common.redis;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * 微博Redis相关操作
 * <p>
 * 该接口不承诺稳定, 外部实现请继承{@link BaseWeiboRedisOps}
 *
 * @see BaseWeiboRedisOps 实现需要继承该类
 * @see JedisWeiboRedisOps jedis实现
 * @see RedissonWeiboRedisOps redisson实现
 * @see RedisTemplateWeiboRedisOps redisTemplate实现
 */
public interface WeiboRedisOps {

  String getValue(String key);

  void setValue(String key, String value, int expire, TimeUnit timeUnit);

  Long getExpire(String key);

  void expire(String key, int expire, TimeUnit timeUnit);

  Lock getLock(String key);
}
