package vip.seanxq.weibo.mp.builder.outxml;

import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutDeviceMessage;

/**
 * 设备消息 Builder
 * @author biggates
 * @see https://iot.weixin.qq.com/wiki/new/index.html?page=3-4-2
 */
public final class DeviceBuilder extends BaseBuilder<DeviceBuilder, WeiboMpXmlOutDeviceMessage> {

  private String deviceId;
  private String deviceType;
  private String content;
  private String sessionId;

  public DeviceBuilder deviceType(String deviceType) {
    this.deviceType = deviceType;
    return this;
  }

  public DeviceBuilder deviceId(String deviceId) {
    this.deviceId = deviceId;
    return this;
  }

  public DeviceBuilder content(String content) {
    this.content = content;
    return this;
  }

  public DeviceBuilder sessionId(String sessionId) {
    this.sessionId = sessionId;
    return this;
  }

  @Override
  public WeiboMpXmlOutDeviceMessage build() {
    WeiboMpXmlOutDeviceMessage m = new WeiboMpXmlOutDeviceMessage();
    setCommon(m);
    m.setDeviceId(this.deviceId);
    m.setDeviceType(this.deviceType);
    m.setContent(this.content);
    m.setSessionId(this.sessionId);
    return m;
  }

}
