package vip.seanxq.weibo.mp.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import vip.seanxq.weibo.common.api.WeiboErrorExceptionHandler;
import vip.seanxq.weibo.common.api.WeiboMessageDuplicateChecker;
import vip.seanxq.weibo.common.api.WeiboMessageInMemoryDuplicateChecker;
import vip.seanxq.weibo.common.session.InternalSession;
import vip.seanxq.weibo.common.session.InternalSessionManager;
import vip.seanxq.weibo.common.session.StandardSessionManager;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.common.util.LogExceptionHandler;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;

/**
 * <pre>
 * 微博消息路由器，通过代码化的配置，把来自微博的消息交给handler处理
 *
 * 说明：
 * 1. 配置路由规则时要按照从细到粗的原则，否则可能消息可能会被提前处理
 * 2. 默认情况下消息只会被处理一次，除非使用 {@link WeiboFansMessageRouterRule#next()}
 * 3. 规则的结束必须用{@link WeiboFansMessageRouterRule#end()}或者{@link WeiboFansMessageRouterRule#next()}，否则不会生效
 * 4. 通过在公众号后台配置的url，WX会通过http Post方法推送消息过来。因实，在这个url controller中post方法，进行接收消息并
 *    转给R{@link WeiboFansMessageRouter}实例进行处理.
 * 使用方法：
 * WeiboFansMessageRouter router = new WeiboFansMessageRouter();
 * router
 *   .rule()
 *       .msgType("MSG_TYPE").event("EVENT").eventKey("EVENT_KEY").content("CONTENT")
 *       .interceptor(interceptor, ...).handler(handler, ...)
 *   .end()
 *   .rule()
 *       // 另外一个匹配规则
 *   .end()
 * ;
 *
 * // 将WxXmlMessage交给消息路由器
 * router.route(message);
 *
 *
 *
 * </pre>
 *
 * @author Daniel Qian
 */
public class WeiboFansMessageRouter {
  private static final int DEFAULT_THREAD_POOL_SIZE = 100;
  protected final Logger log = LoggerFactory.getLogger(WeiboFansMessageRouter.class);
  private final List<WeiboFansMessageRouterRule> rules = new ArrayList<>();

  private final WeiboMpService weiboMpService;

  private ExecutorService executorService;

  private WeiboMessageDuplicateChecker messageDuplicateChecker;

  private WeiboSessionManager sessionManager;

  private WeiboErrorExceptionHandler exceptionHandler;

  public WeiboFansMessageRouter(WeiboMpService weiboMpService) {
    this.weiboMpService = weiboMpService;
    this.executorService = Executors.newFixedThreadPool(DEFAULT_THREAD_POOL_SIZE);
    this.messageDuplicateChecker = new WeiboMessageInMemoryDuplicateChecker();
    this.sessionManager = new StandardSessionManager();
    this.exceptionHandler = new LogExceptionHandler();
  }

  /**
   * 使用自定义的 {@link ExecutorService}.
   */
  public WeiboFansMessageRouter(WeiboMpService weiboMpService, ExecutorService executorService) {
    this.weiboMpService = weiboMpService;
    this.executorService = executorService;
    this.messageDuplicateChecker = new WeiboMessageInMemoryDuplicateChecker();
    this.sessionManager = new StandardSessionManager();
    this.exceptionHandler = new LogExceptionHandler();
  }

  /**
   * 如果使用默认的 {@link ExecutorService}，则系统退出前，应该调用该方法.
   */
  public void shutDownExecutorService() {
    this.executorService.shutdown();
  }


  /**
   * <pre>
   * 设置自定义的 {@link ExecutorService}
   * 如果不调用该方法，默认使用 Executors.newFixedThreadPool(100)
   * </pre>
   */
  public void setExecutorService(ExecutorService executorService) {
    this.executorService = executorService;
  }

  /**
   * <pre>
   * 设置自定义的 {@link WeiboMessageDuplicateChecker}
   * 如果不调用该方法，默认使用 {@link WeiboMessageInMemoryDuplicateChecker}
   * </pre>
   */
  public void setMessageDuplicateChecker(WeiboMessageDuplicateChecker messageDuplicateChecker) {
    this.messageDuplicateChecker = messageDuplicateChecker;
  }

  /**
   * <pre>
   * 设置自定义的{@link WeiboSessionManager}
   * 如果不调用该方法，默认使用 {@link StandardSessionManager}
   * </pre>
   */
  public void setSessionManager(WeiboSessionManager sessionManager) {
    this.sessionManager = sessionManager;
  }

  /**
   * <pre>
   * 设置自定义的{@link WeiboErrorExceptionHandler}
   * 如果不调用该方法，默认使用 {@link LogExceptionHandler}
   * </pre>
   */
  public void setExceptionHandler(WeiboErrorExceptionHandler exceptionHandler) {
    this.exceptionHandler = exceptionHandler;
  }

  List<WeiboFansMessageRouterRule> getRules() {
    return this.rules;
  }

  /**
   * 开始一个新的Route规则.
   */
  public WeiboFansMessageRouterRule rule() {
    return new WeiboFansMessageRouterRule(this);
  }

  /**
   * 处理微博消息.
   */
  public WeiboMpXmlOutMessage route(final WeiboMpXmlMessage wxMessage, final Map<String, Object> context) {
    return route(wxMessage, context, null);
  }

  /**
   * 处理不同appid微博消息
   */
  public WeiboMpXmlOutMessage route(final String appid, final WeiboMpXmlMessage wxMessage, final Map<String, Object> context) {
    return route(wxMessage, context, this.weiboMpService.switchoverTo(appid));
  }

  /**
   * 处理微博消息.
   */
  public WeiboMpXmlOutMessage route(final WeiboMpXmlMessage wxMessage, final Map<String, Object> context, WeiboMpService wxMpService) {
    if (wxMpService == null) {
      wxMpService = this.weiboMpService;
    }
    final WeiboMpService mpService = wxMpService;
    if (isMsgDuplicated(wxMessage)) {
      // 如果是重复消息，那么就不做处理
      return null;
    }

    final List<WeiboFansMessageRouterRule> matchRules = new ArrayList<>();
    // 收集匹配的规则
    for (final WeiboFansMessageRouterRule rule : this.rules) {
      if (rule.test(wxMessage)) {
        matchRules.add(rule);
        if (!rule.isReEnter()) {
          break;
        }
      }
    }

    if (matchRules.size() == 0) {
      return null;
    }

    WeiboMpXmlOutMessage res = null;
    final List<Future<?>> futures = new ArrayList<>();
    for (final WeiboFansMessageRouterRule rule : matchRules) {
      // 返回最后一个非异步的rule的执行结果
      if (rule.isAsync()) {
        futures.add(
          this.executorService.submit(new Runnable() {
            @Override
            public void run() {
              rule.service(wxMessage, context, mpService, WeiboFansMessageRouter.this.sessionManager, WeiboFansMessageRouter.this.exceptionHandler);
            }
          })
        );
      } else {
        res = rule.service(wxMessage, context, mpService, this.sessionManager, this.exceptionHandler);
        // 在同步操作结束，session访问结束
        this.log.debug("End session access: async=false, sessionId={}", wxMessage.getFromUser());
        sessionEndAccess(wxMessage);
      }
    }

    if (futures.size() > 0) {
      this.executorService.submit(new Runnable() {
        @Override
        public void run() {
          for (Future<?> future : futures) {
            try {
              future.get();
              WeiboFansMessageRouter.this.log.debug("End session access: async=true, sessionId={}", wxMessage.getFromUser());
              // 异步操作结束，session访问结束
              sessionEndAccess(wxMessage);
            } catch (InterruptedException e) {
              WeiboFansMessageRouter.this.log.error("Error happened when wait task finish", e);
              Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
              WeiboFansMessageRouter.this.log.error("Error happened when wait task finish", e);
            }
          }
        }
      });
    }
    return res;
  }

  public WeiboMpXmlOutMessage route(final WeiboMpXmlMessage wxMessage) {
    return this.route(wxMessage, new HashMap<String, Object>(2));
  }

  public WeiboMpXmlOutMessage route(String appid, final WeiboMpXmlMessage wxMessage) {
    return this.route(appid, wxMessage, new HashMap<String, Object>(2));
  }

  private boolean isMsgDuplicated(WeiboMpXmlMessage wxMessage) {
    StringBuilder messageId = new StringBuilder();
    if (wxMessage.getMsgId() == null) {
      messageId.append(wxMessage.getCreateTime())
        .append("-").append(wxMessage.getFromUser())
        .append("-").append(StringUtils.trimToEmpty(wxMessage.getEventKey()))
        .append("-").append(StringUtils.trimToEmpty(wxMessage.getEvent()));
    } else {
      messageId.append(wxMessage.getMsgId())
        .append("-").append(wxMessage.getCreateTime())
        .append("-").append(wxMessage.getFromUser());
    }

    if (StringUtils.isNotEmpty(wxMessage.getUserCardCode())) {
      messageId.append("-").append(wxMessage.getUserCardCode());
    }

    return this.messageDuplicateChecker.isDuplicate(messageId.toString());

  }

  /**
   * 对session的访问结束.
   */
  private void sessionEndAccess(WeiboMpXmlMessage wxMessage) {
    InternalSession session = ((InternalSessionManager) this.sessionManager).findSession(wxMessage.getFromUser());
    if (session != null) {
      session.endAccess();
    }
  }
}
