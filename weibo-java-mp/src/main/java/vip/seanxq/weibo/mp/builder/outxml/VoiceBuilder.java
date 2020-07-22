package vip.seanxq.weibo.mp.builder.outxml;

import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutVoiceMessage;

/**
 * 语音消息builder
 *
 * @author chanjarster
 */
public final class VoiceBuilder extends BaseBuilder<VoiceBuilder, WeiboMpXmlOutVoiceMessage> {

  private String mediaId;

  public VoiceBuilder mediaId(String mediaId) {
    this.mediaId = mediaId;
    return this;
  }

  @Override
  public WeiboMpXmlOutVoiceMessage build() {
    WeiboMpXmlOutVoiceMessage m = new WeiboMpXmlOutVoiceMessage();
    setCommon(m);
    m.setMediaId(this.mediaId);
    return m;
  }

}
