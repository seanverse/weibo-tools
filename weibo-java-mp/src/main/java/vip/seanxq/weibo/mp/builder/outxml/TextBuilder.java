package vip.seanxq.weibo.mp.builder.outxml;

import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutTextMessage;

/**
 * 文本消息builder
 *
 * @author chanjarster
 */
public final class TextBuilder extends BaseBuilder<TextBuilder, WeiboMpXmlOutTextMessage> {
  private String content;

  public TextBuilder content(String content) {
    this.content = content;
    return this;
  }

  @Override
  public WeiboMpXmlOutTextMessage build() {
    WeiboMpXmlOutTextMessage m = new WeiboMpXmlOutTextMessage();
    setCommon(m);
    m.setContent(this.content);
    return m;
  }
}
