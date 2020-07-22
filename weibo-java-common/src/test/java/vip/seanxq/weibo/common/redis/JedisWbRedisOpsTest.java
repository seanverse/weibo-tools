package vip.seanxq.weibo.common.redis;

import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import redis.clients.jedis.JedisPool;

public class JedisWbRedisOpsTest extends CommonWbRedisOpsTest {

  JedisPool jedisPool;

  @BeforeTest
  public void init() {
    this.jedisPool = new JedisPool("127.0.0.1", 6379);
    this.weiboRedisOps = new JedisWeiboRedisOps(jedisPool);
  }

  @AfterTest
  public void destroy() {
    this.jedisPool.close();
  }
}
