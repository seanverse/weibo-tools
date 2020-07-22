package vip.seanxq.weibo.common.redis;

import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

public class CommonWbRedisOpsTest {

  protected WeiboRedisOps weiboRedisOps;
  private String key = "access_token";
  private String value = String.valueOf(System.currentTimeMillis());

  @Test
  public void testGetValue() {
    weiboRedisOps.setValue(key, value, 3, TimeUnit.SECONDS);
    Assert.assertEquals(weiboRedisOps.getValue(key), value);
  }

  @Test
  public void testSetValue() {
    String key = "access_token", value = String.valueOf(System.currentTimeMillis());
    weiboRedisOps.setValue(key, value, -1, TimeUnit.SECONDS);
    weiboRedisOps.setValue(key, value, 0, TimeUnit.SECONDS);
    weiboRedisOps.setValue(key, value, 1, TimeUnit.SECONDS);
  }

  @Test
  public void testGetExpire() {
    String key = "access_token", value = String.valueOf(System.currentTimeMillis());
    weiboRedisOps.setValue(key, value, -1, TimeUnit.SECONDS);
    Assert.assertTrue(weiboRedisOps.getExpire(key) < 0);
    weiboRedisOps.setValue(key, value, 4, TimeUnit.SECONDS);
    Long expireSeconds = weiboRedisOps.getExpire(key);
    Assert.assertTrue(expireSeconds <= 4 && expireSeconds >= 0);
  }

  @Test
  public void testExpire() {
    String key = "access_token", value = String.valueOf(System.currentTimeMillis());
    weiboRedisOps.setValue(key, value, -1, TimeUnit.SECONDS);
    weiboRedisOps.expire(key, 4, TimeUnit.SECONDS);
    Long expireSeconds = weiboRedisOps.getExpire(key);
    Assert.assertTrue(expireSeconds <= 4 && expireSeconds >= 0);
  }

  @Test
  public void testGetLock() {
    Assert.assertNotNull(weiboRedisOps.getLock("access_token_lock"));
  }
}
