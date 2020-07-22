package vip.seanxq.weibo.common.session;

import org.testng.*;
import org.testng.annotations.*;

@Test
public class SessionTest {

  @DataProvider
  public Object[][] getSessionManager() {

    return new Object[][]{
      new Object[]{new StandardSessionManager()}
    };

  }


  @Test(dataProvider = "getSessionManager", expectedExceptions = IllegalStateException.class)
  public void testInvalidate(WeiboSessionManager sessionManager) {

    WeiboSession session = sessionManager.getSession("abc");
    session.invalidate();
    session.getAttributeNames();

  }

  @Test(dataProvider = "getSessionManager")
  public void testInvalidate2(InternalSessionManager sessionManager) {

    Assert.assertEquals(sessionManager.getActiveSessions(), 0);
    WeiboSession session = ((WeiboSessionManager) sessionManager).getSession("abc");
    Assert.assertEquals(sessionManager.getActiveSessions(), 1);
    session.invalidate();
    Assert.assertEquals(sessionManager.getActiveSessions(), 0);

  }

  @Test(dataProvider = "getSessionManager")
  public void testGetSession(WeiboSessionManager sessionManager) {

    WeiboSession session1 = sessionManager.getSession("abc");
    WeiboSession session2 = sessionManager.getSession("abc");
    Assert.assertEquals(session1, session2);
    Assert.assertTrue(session1 == session2);

    WeiboSession abc1 = sessionManager.getSession("abc1");
    Assert.assertNotEquals(session1, abc1);

    WeiboSession abc1b = sessionManager.getSession("abc1", false);
    Assert.assertEquals(abc1, abc1b);

    WeiboSession def = sessionManager.getSession("def", false);
    Assert.assertNull(def);

  }

  @Test(dataProvider = "getSessionManager")
  public void testInvalidateAngGet(WeiboSessionManager sessionManager) {

    WeiboSession session1 = sessionManager.getSession("abc");
    session1.invalidate();
    WeiboSession session2 = sessionManager.getSession("abc");
    Assert.assertNotEquals(session1, session2);
    InternalSessionManager ism = (InternalSessionManager) sessionManager;
    Assert.assertEquals(ism.getActiveSessions(), 1);

  }

  @Test(dataProvider = "getSessionManager")
  public void testBackgroundProcess(WeiboSessionManager sessionManager) throws InterruptedException {

    InternalSessionManager ism = (InternalSessionManager) sessionManager;
    ism.setMaxInactiveInterval(1);
    ism.setProcessExpiresFrequency(1);
    ism.setBackgroundProcessorDelay(1);

    Assert.assertEquals(ism.getActiveSessions(), 0);

    InternalSession abc = ism.createSession("abc");
    abc.endAccess();

    Thread.sleep(2000);
    Assert.assertEquals(ism.getActiveSessions(), 0);

  }

  @Test(dataProvider = "getSessionManager")
  public void testBackgroundProcess2(WeiboSessionManager sessionManager) throws InterruptedException {

    InternalSessionManager ism = (InternalSessionManager) sessionManager;
    ism.setMaxInactiveInterval(100);
    ism.setProcessExpiresFrequency(1);
    ism.setBackgroundProcessorDelay(1);

    Assert.assertEquals(ism.getActiveSessions(), 0);

    InternalSession abc = ism.createSession("abc");
    abc.setMaxInactiveInterval(1);
    abc.endAccess();

    Thread.sleep(2000);
    Assert.assertEquals(ism.getActiveSessions(), 0);

  }

  @Test(dataProvider = "getSessionManager")
  public void testMaxActive(WeiboSessionManager sessionManager) {

    InternalSessionManager ism = (InternalSessionManager) sessionManager;
    ism.setMaxActiveSessions(2);

    ism.createSession("abc");
    ism.createSession("abc");
    ism.createSession("def");

  }

  @Test(dataProvider = "getSessionManager", expectedExceptions = TooManyActiveSessionsException.class)
  public void testMaxActive2(WeiboSessionManager sessionManager) {

    InternalSessionManager ism = (InternalSessionManager) sessionManager;
    ism.setMaxActiveSessions(2);

    ism.createSession("abc");
    ism.createSession("abc");
    ism.createSession("def");
    ism.createSession("xyz");

  }
}
