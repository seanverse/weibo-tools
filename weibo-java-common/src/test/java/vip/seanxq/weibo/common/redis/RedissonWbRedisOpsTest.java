package vip.seanxq.weibo.common.redis;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.redisson.config.TransportMode;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;

public class RedissonWbRedisOpsTest extends CommonWbRedisOpsTest {

  RedissonClient redissonClient;

  @BeforeTest
  public void init() {
    Config config = new Config();
    config.useSingleServer().setAddress("redis://127.0.0.1:6379");
    config.setTransportMode(TransportMode.NIO);
    this.redissonClient = Redisson.create(config);
    this.weiboRedisOps = new RedissonWeiboRedisOps(this.redissonClient);
  }

  @AfterTest
  public void destroy() {
    this.redissonClient.shutdown();
  }
}
