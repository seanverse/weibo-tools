package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.api.WeiboErrorExceptionHandler;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class WeiboFansMessageRouterRule {

  private final WeiboFansMessageRouter routerBuilder;

  private boolean async = true;

  private String fromUser;

  private String msgType;

  private String event;

  private String eventKey;

  private String eventKeyRegex;

  private String content;

  private String rContent;

  private WeiboFansMessageMatcher matcher;

  private boolean reEnter = false;

  private List<WeiboFansMessageHandler> handlers = new ArrayList<>();

  private List<WeiboFansMessageInterceptor> interceptors = new ArrayList<>();

  public WeiboFansMessageRouterRule(WeiboFansMessageRouter routerBuilder) {
    this.routerBuilder = routerBuilder;
  }

  /**
   * 设置是否异步执行，默认是true
   */
  public WeiboFansMessageRouterRule async(boolean async) {
    this.async = async;
    return this;
  }

  /**
   * 如果msgType等于某值
   */
  public WeiboFansMessageRouterRule msgType(String msgType) {
    this.msgType = msgType;
    return this;
  }

  /**
   * 如果event等于某值
   */
  public WeiboFansMessageRouterRule event(String event) {
    this.event = event;
    return this;
  }

  /**
   * 如果eventKey等于某值
   */
  public WeiboFansMessageRouterRule eventKey(String eventKey) {
    this.eventKey = eventKey;
    return this;
  }

  /**
   * 如果eventKey匹配该正则表达式
   */
  public WeiboFansMessageRouterRule eventKeyRegex(String regex) {
    this.eventKeyRegex = regex;
    return this;
  }

  /**
   * 如果content等于某值
   */
  public WeiboFansMessageRouterRule content(String content) {
    this.content = content;
    return this;
  }

  /**
   * 如果content匹配该正则表达式
   */
  public WeiboFansMessageRouterRule rContent(String regex) {
    this.rContent = regex;
    return this;
  }

  /**
   * 如果fromUser等于某值
   */
  public WeiboFansMessageRouterRule fromUser(String fromUser) {
    this.fromUser = fromUser;
    return this;
  }

  /**
   * 如果消息匹配某个matcher，用在用户需要自定义更复杂的匹配规则的时候
   */
  public WeiboFansMessageRouterRule matcher(WeiboFansMessageMatcher matcher) {
    this.matcher = matcher;
    return this;
  }

  /**
   * 设置微博消息拦截器
   */
  public WeiboFansMessageRouterRule interceptor(WeiboFansMessageInterceptor interceptor) {
    return interceptor(interceptor, (WeiboFansMessageInterceptor[]) null);
  }

  /**
   * 设置微博消息拦截器
   */
  public WeiboFansMessageRouterRule interceptor(WeiboFansMessageInterceptor interceptor, WeiboFansMessageInterceptor... otherInterceptors) {
    this.interceptors.add(interceptor);
    if (otherInterceptors != null && otherInterceptors.length > 0) {
      for (WeiboFansMessageInterceptor i : otherInterceptors) {
        this.interceptors.add(i);
      }
    }
    return this;
  }

  /**
   * 设置微博消息处理器
   */
  public WeiboFansMessageRouterRule handler(WeiboFansMessageHandler handler) {
    return handler(handler, (WeiboFansMessageHandler[]) null);
  }

  /**
   * 设置微博消息处理器
   */
  public WeiboFansMessageRouterRule handler(WeiboFansMessageHandler handler, WeiboFansMessageHandler... otherHandlers) {
    this.handlers.add(handler);
    if (otherHandlers != null && otherHandlers.length > 0) {
      for (WeiboFansMessageHandler i : otherHandlers) {
        this.handlers.add(i);
      }
    }
    return this;
  }

  /**
   * 规则结束，代表如果一个消息匹配该规则，那么它将不再会进入其他规则
   */
  public WeiboFansMessageRouter end() {
    this.routerBuilder.getRules().add(this);
    return this.routerBuilder;
  }

  /**
   * 规则结束，但是消息还会进入其他规则
   */
  public WeiboFansMessageRouter next() {
    this.reEnter = true;
    return end();
  }

  /**
   * 将微博自定义的事件修正为不区分大小写,
   * 比如框架定义的事件常量为click，但微博传递过来的却是CLICK
   */
  protected boolean test(WeiboMpXmlMessage wxMessage) {
    return
      (this.fromUser == null || this.fromUser.equals(wxMessage.getFromUser()))
        &&
        (this.msgType == null || this.msgType.equalsIgnoreCase(wxMessage.getMsgType()))
        &&
        (this.event == null || this.event.equalsIgnoreCase(wxMessage.getEvent()))
        &&
        (this.eventKey == null || this.eventKey.equalsIgnoreCase(wxMessage.getEventKey()))
        &&
        (this.eventKeyRegex == null || Pattern.matches(this.eventKeyRegex, StringUtils.trimToEmpty(wxMessage.getEventKey())))
        &&
        (this.content == null || this.content.equals(StringUtils.trimToNull(wxMessage.getContent())))
        &&
        (this.rContent == null || Pattern.matches(this.rContent, StringUtils.trimToEmpty(wxMessage.getContent())))
        &&
        (this.matcher == null || this.matcher.match(wxMessage))
      ;
  }

  /**
   * 处理微博推送过来的消息
   *
   * @param wxMessage
   * @return true 代表继续执行别的router，false 代表停止执行别的router
   */
  protected WeiboMpXmlOutMessage service(WeiboMpXmlMessage wxMessage,
                                         Map<String, Object> context,
                                         WeiboMpService wxMpService,
                                         WeiboSessionManager sessionManager,
                                         WeiboErrorExceptionHandler exceptionHandler) {

    if (context == null) {
      context = new HashMap<>();
    }

    try {
      // 如果拦截器不通过
      for (WeiboFansMessageInterceptor interceptor : this.interceptors) {
        if (!interceptor.intercept(wxMessage, context, wxMpService, sessionManager)) {
          return null;
        }
      }

      // 交给handler处理
      WeiboMpXmlOutMessage res = null;
      for (WeiboFansMessageHandler handler : this.handlers) {
        // 返回最后handler的结果
        if (handler == null) {
          continue;
        }
        res = handler.handle(wxMessage, context, wxMpService, sessionManager);
      }
      return res;
    } catch (WeiboErrorException e) {
      exceptionHandler.handle(e);
    }
    return null;

  }

  public WeiboFansMessageRouter getRouterBuilder() {
    return this.routerBuilder;
  }

  public boolean isAsync() {
    return this.async;
  }

  public void setAsync(boolean async) {
    this.async = async;
  }

  public String getFromUser() {
    return this.fromUser;
  }

  public void setFromUser(String fromUser) {
    this.fromUser = fromUser;
  }

  public String getMsgType() {
    return this.msgType;
  }

  public void setMsgType(String msgType) {
    this.msgType = msgType;
  }

  public String getEvent() {
    return this.event;
  }

  public void setEvent(String event) {
    this.event = event;
  }

  public String getEventKey() {
    return this.eventKey;
  }

  public void setEventKey(String eventKey) {
    this.eventKey = eventKey;
  }

  public String getContent() {
    return this.content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public String getrContent() {
    return this.rContent;
  }

  public void setrContent(String rContent) {
    this.rContent = rContent;
  }

  public WeiboFansMessageMatcher getMatcher() {
    return this.matcher;
  }

  public void setMatcher(WeiboFansMessageMatcher matcher) {
    this.matcher = matcher;
  }

  public boolean isReEnter() {
    return this.reEnter;
  }

  public void setReEnter(boolean reEnter) {
    this.reEnter = reEnter;
  }

  public List<WeiboFansMessageHandler> getHandlers() {
    return this.handlers;
  }

  public void setHandlers(List<WeiboFansMessageHandler> handlers) {
    this.handlers = handlers;
  }

  public List<WeiboFansMessageInterceptor> getInterceptors() {
    return this.interceptors;
  }

  public void setInterceptors(List<WeiboFansMessageInterceptor> interceptors) {
    this.interceptors = interceptors;
  }
}
