package vip.seanxq.weibo.mp.builder.outxml;

import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutNewsMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 图文消息builder
 *
 * @author chanjarster
 */
public final class NewsBuilder extends BaseBuilder<NewsBuilder, WeiboMpXmlOutNewsMessage> {
  private List<WeiboMpXmlOutNewsMessage.Item> articles = new ArrayList<>();

  public NewsBuilder addArticle(WeiboMpXmlOutNewsMessage.Item... items) {
    Collections.addAll(this.articles, items);
    return this;
  }

  public NewsBuilder articles(List<WeiboMpXmlOutNewsMessage.Item> articles){
    this.articles = articles;
    return this;
  }

  @Override
  public WeiboMpXmlOutNewsMessage build() {
    WeiboMpXmlOutNewsMessage m = new WeiboMpXmlOutNewsMessage();
    for (WeiboMpXmlOutNewsMessage.Item item : this.articles) {
      m.addArticle(item);
    }
    setCommon(m);
    return m;
  }

}
