package vip.seanxq.weibo.mp.bean.message;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import vip.seanxq.weibo.common.api.WeiboConsts;
import vip.seanxq.weibo.common.util.xml.XStreamMediaIdConverter;

@XStreamAlias("xml")
@Data
@EqualsAndHashCode(callSuper = true)
public class WeiboMpXmlOutVoiceMessage extends WeiboMpXmlOutMessage {
  private static final long serialVersionUID = 240367390249860551L;

  @XStreamAlias("Voice")
  @XStreamConverter(value = XStreamMediaIdConverter.class)
  private String mediaId;

  public WeiboMpXmlOutVoiceMessage() {
    this.msgType = WeiboConsts.XmlMsgType.VOICE;
  }

}
