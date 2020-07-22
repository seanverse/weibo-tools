package vip.seanxq.weibo.mp.builder.outxml;

import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutImageMessage;

/**
 * 图片消息builder
 *
 * @author chanjarster
 */
public final class ImageBuilder extends BaseBuilder<ImageBuilder, WeiboMpXmlOutImageMessage> {

  private String mediaId;

  public ImageBuilder mediaId(String media_id) {
    this.mediaId = media_id;
    return this;
  }

  @Override
  public WeiboMpXmlOutImageMessage build() {
    WeiboMpXmlOutImageMessage m = new WeiboMpXmlOutImageMessage();
    setCommon(m);
    m.setMediaId(this.mediaId);
    return m;
  }

}
