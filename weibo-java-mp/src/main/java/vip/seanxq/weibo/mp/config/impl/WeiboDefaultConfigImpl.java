package vip.seanxq.weibo.mp.config.impl;

import java.io.File;
import java.io.Serializable;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lombok.Data;
import vip.seanxq.weibo.common.bean.WeiboAccessToken;
import vip.seanxq.weibo.common.enums.WbMessageFormat;
import vip.seanxq.weibo.common.util.http.apache.ApacheHttpClientBuilder;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.common.enums.TicketType;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * 基于内存的微博配置provider，在实际生产环境中应该将这些配置持久化.
 *
 * @author chanjarster
 */
@Data
public class WeiboDefaultConfigImpl implements WeiboConfigStorage, Serializable {
  private static final long serialVersionUID = -6646519023303395185L;

  protected volatile String appId;
  protected volatile String secret;
  protected volatile String token;
  protected volatile WbMessageFormat messageFormat = WbMessageFormat.XML;
  protected volatile String accessToken;
  protected volatile String aesKey;
  protected volatile long expiresTime;

  protected volatile String oauth2redirectUri;

  protected volatile String httpProxyHost;
  protected volatile int httpProxyPort;
  protected volatile String httpProxyUsername;
  protected volatile String httpProxyPassword;

  protected volatile String jsapiTicket;
  protected volatile long jsapiTicketExpiresTime;

  protected volatile String sdkTicket;
  protected volatile long sdkTicketExpiresTime;

  protected volatile String cardApiTicket;
  protected volatile long cardApiTicketExpiresTime;

  protected Lock accessTokenLock = new ReentrantLock();
  protected Lock jsapiTicketLock = new ReentrantLock();
  protected Lock sdkTicketLock = new ReentrantLock();

  protected volatile File tmpDirFile;

  protected volatile ApacheHttpClientBuilder apacheHttpClientBuilder;

  /**
   * 取得config里配置消息格式是xml/json
   *
   * @return
   */
  @Override
  public WbMessageFormat getMessageFormat() {
    return this.messageFormat;
  }

  @Override
  public boolean isAccessTokenExpired() {
    return System.currentTimeMillis() > this.expiresTime;
  }

  @Override
  public synchronized void updateAccessToken(WeiboAccessToken accessToken) {
    updateAccessToken(accessToken.getAccessToken(), accessToken.getExpiresIn());
  }

  @Override
  public synchronized void updateAccessToken(String accessToken, int expiresInSeconds) {
    this.accessToken = accessToken;
    this.expiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
  }

  @Override
  public void expireAccessToken() {
    this.expiresTime = 0;
  }

  @Override
  public String getTicket(TicketType type) {
    switch (type) {
      case SDK:
        return this.sdkTicket;
      case JSAPI:
        return this.jsapiTicket;
      default:
        return null;
    }
  }

  public void setTicket(TicketType type, String ticket) {
    switch (type) {
      case JSAPI:
        this.jsapiTicket = ticket;
        break;
      case SDK:
        this.sdkTicket = ticket;
        break;
      default:
    }
  }

  @Override
  public Lock getTicketLock(TicketType type) {
    switch (type) {
      case SDK:
        return this.sdkTicketLock;
      case JSAPI:
        return this.jsapiTicketLock;
      default:
        return null;
    }
  }

  @Override
  public boolean isTicketExpired(TicketType type) {
    switch (type) {
      case SDK:
        return System.currentTimeMillis() > this.sdkTicketExpiresTime;
      case JSAPI:
        return System.currentTimeMillis() > this.jsapiTicketExpiresTime;
      default:
        return false;
    }
  }

  @Override
  public synchronized void updateTicket(TicketType type, String ticket, int expiresInSeconds) {
    switch (type) {
      case JSAPI:
        this.jsapiTicket = ticket;
        // 预留200秒的时间
        this.jsapiTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        break;
      case SDK:
        this.sdkTicket = ticket;
        // 预留200秒的时间
        this.sdkTicketExpiresTime = System.currentTimeMillis() + (expiresInSeconds - 200) * 1000L;
        break;
      default:
    }
  }

  @Override
  public void expireTicket(TicketType type) {
    switch (type) {
      case JSAPI:
        this.jsapiTicketExpiresTime = 0;
        break;
      case SDK:
        this.sdkTicketExpiresTime = 0;
        break;
      default:
    }
  }

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @Override
  public boolean autoRefreshToken() {
    return true;
  }

  @Override
  public WeiboMpHostConfig getHostConfig() {
    return null;
  }

}
