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
public class WeiboMpXmlOutTextMessage extends WeiboMpXmlOutMessage {
  private static final long serialVersionUID = -3972786455288763361L;

  @XStreamAlias("Content")
  @XStreamConverter(value = XStreamCDataConverter.class)
  private String content;

  public WeiboMpXmlOutTextMessage() {
    this.msgType = WeiboConsts.XmlMsgType.TEXT;
  }

}
