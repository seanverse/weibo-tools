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
public class WeiboMpXmlOutImageMessage extends WeiboMpXmlOutMessage {
  private static final long serialVersionUID = -2684778597067990723L;

  @XStreamAlias("Image")
  @XStreamConverter(value = XStreamMediaIdConverter.class)
  private String mediaId;

  public WeiboMpXmlOutImageMessage() {
    this.msgType = WeiboConsts.XmlMsgType.IMAGE;
  }

}
