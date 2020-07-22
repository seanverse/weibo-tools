package vip.seanxq.weibo.mp.api;

import lombok.extern.slf4j.Slf4j;
import vip.seanxq.weibo.common.error.WeiboError;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.RequestExecutor;
import vip.seanxq.weibo.mp.api.impl.WeiboMpServiceHttpClientImpl;
import org.testng.annotations.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Test
@Slf4j
public class WeiboMpBusyRetryTest {

  @DataProvider(name = "getService")
  public Object[][] getService() {
    WeiboMpService service = new WeiboMpServiceHttpClientImpl() {

      @Override
      public synchronized <T, E> T executeInternal(
        RequestExecutor<T, E> executor, String uri, E data)
        throws WeiboErrorException {
        log.info("Executed");
        throw new WeiboErrorException(WeiboError.builder().errorCode(-1).build());
      }
    };

    service.setMaxRetryTimes(3);
    service.setRetrySleepMillis(500);
    return new Object[][]{{service}};
  }

  @Test(dataProvider = "getService", expectedExceptions = RuntimeException.class)
  public void testRetry(WeiboMpService service) throws WeiboErrorException {
    service.execute(null, (String)null, null);
  }

  @Test(dataProvider = "getService")
  public void testRetryInThreadPool(final WeiboMpService service) throws InterruptedException, ExecutionException {
    // 当线程池中的线程复用的时候，还是能保证相同的重试次数
    ExecutorService executorService = Executors.newFixedThreadPool(1);
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          System.out.println("=====================");
          System.out.println(Thread.currentThread().getName() + ": testRetry");
          service.execute(null, (String)null, null);
        } catch (WeiboErrorException e) {
          throw new RuntimeException(e);
        } catch (RuntimeException e) {
          // OK
        }
      }
    };
    Future<?> submit1 = executorService.submit(runnable);
    Future<?> submit2 = executorService.submit(runnable);

    submit1.get();
    submit2.get();
  }

}
