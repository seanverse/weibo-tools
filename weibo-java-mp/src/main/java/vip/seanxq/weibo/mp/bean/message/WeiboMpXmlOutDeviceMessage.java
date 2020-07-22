package vip.seanxq.weibo.mp.bean.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.util.xml.XStreamCDataConverter;

@XStreamAlias("xml")
@Data
@EqualsAndHashCode(callSuper = true)
public class WeiboMpXmlOutDeviceMessage extends WeiboMpXmlOutMessage {
  private static final long serialVersionUID = -3093843149649157587L;

  @XStreamAlias("DeviceType")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String deviceType;

  @XStreamAlias("DeviceID")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String deviceId;

  @XStreamAlias("Content")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String content;

  @XStreamAlias("SessionID")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String sessionId;

  public WeiboMpXmlOutDeviceMessage() {
    this.msgType = WeiboConsts.XmlMsgType.DEVICE_TEXT;
  }
}
