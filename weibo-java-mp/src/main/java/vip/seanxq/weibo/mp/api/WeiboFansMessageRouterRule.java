package vip.seanxq.weibo.mp.api;

import lombok.Getter;
import lombok.Setter;
import vip.seanxq.weibo.common.api.WeiboErrorExceptionHandler;
import vip.seanxq.weibo.common.enums.MsgType;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.session.WeiboSessionManager;
import org.apache.commons.lang3.StringUtils;
import vip.seanxq.weibo.mp.bean.message.EventSubType;
import vip.seanxq.weibo.mp.bean.message.WeiboReceiveMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

@Setter
@Getter
public class WeiboFansMessageRouterRule {

  private final WeiboFansMessageRouter routerBuilder;

  private boolean async = true;

  private String senderId;

  private MsgType msgType;

  private String text;
  private String text_Regex;

  private EventSubType eventSubType;

  private String event_dataKey;

  private String event_dataKey_Regex;

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
  public WeiboFansMessageRouterRule msgType(MsgType msgType) {
    this.msgType =  msgType;
    return this;
  }

  /**
   * 如果event等于某值
   */
  public WeiboFansMessageRouterRule event(EventSubType event) {
    this.eventSubType = event;
    return this;
  }

  /**
   * 如果eventKey等于某值
   */
  public WeiboFansMessageRouterRule eventKey(EventSubType event, String eventKey) {
    this.eventSubType = event;
    this.event_dataKey = eventKey;
    return this;
  }

  /**
   * 如果eventKey匹配该正则表达式
   */
  public WeiboFansMessageRouterRule eventKeyRegex(String regex) {
    this.event_dataKey_Regex = regex;
    return this;
  }

  /**
   * 如果content等于某值
   */
  public WeiboFansMessageRouterRule content(String text) {
    this.text = text;
    return this;
  }

  /**
   * 如果content匹配该正则表达式
   */
  public WeiboFansMessageRouterRule rContent(String text_regex) {
    this.text_Regex = text_regex;
    return this;
  }

  /**
   * 如果fromUser等于某值
   */
  public WeiboFansMessageRouterRule fromUser(String fromUserId) {
    this.senderId = fromUserId;
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
  protected boolean test(WeiboReceiveMessage wbMessage) {
    return
      (this.senderId == null || this.senderId.equals(wbMessage.getSenderId()))
        &&
        (this.msgType == null || this.msgType == wbMessage.getType())
        &&
        (this.eventSubType == null || this.eventSubType==wbMessage.getEventData().getSubType())
        &&
        (this.event_dataKey == null || this.event_dataKey.equalsIgnoreCase(wbMessage.getEventData().getDataKey()))
        &&
        (this.event_dataKey_Regex == null || Pattern.matches(this.event_dataKey_Regex, StringUtils.trimToEmpty(wbMessage.getEventData().getDataKey())))
        &&
        (this.text == null || this.text.equals(StringUtils.trimToNull(wbMessage.getText())))
        &&
        (this.text_Regex == null || Pattern.matches(this.text_Regex, StringUtils.trimToEmpty(wbMessage.getText())))
        &&
        (this.matcher == null || this.matcher.match(wbMessage))
      ;
  }

  /**
   * 处理微博推送过来的消息
   *
   * @param wbMessage
   * @return true 代表继续执行别的router，false 代表停止执行别的router
   */
  protected WeiboReceiveMessage service(WeiboReceiveMessage wbMessage,
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
        if (!interceptor.intercept(wbMessage, context, wxMpService, sessionManager)) {
          return null;
        }
      }

      // 交给handler处理
      WeiboReceiveMessage res = null;
      for (WeiboFansMessageHandler handler : this.handlers) {
        // 返回最后handler的结果
        if (handler == null) {
          continue;
        }
        res = handler.handle(wbMessage, context, wxMpService, sessionManager);
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


}
