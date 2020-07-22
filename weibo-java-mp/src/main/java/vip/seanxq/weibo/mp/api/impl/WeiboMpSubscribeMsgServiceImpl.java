package vip.seanxq.weibo.mp.api.impl;

import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.common.util.http.URIUtil;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.api.WeiboMpSubscribeMsgService;
import vip.seanxq.weibo.mp.bean.subscribe.WxMpSubscribeMessage;
import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;

/**
 * 一次性订阅消息接口.
 *
 * @author Mklaus
 * @date 2018-01-22 上午11:19
 */
@RequiredArgsConstructor
public class WeiboMpSubscribeMsgServiceImpl implements WeiboMpSubscribeMsgService {
  private final WeiboMpService wxMpService;

  @Override
  public String subscribeMsgAuthorizationUrl(String redirectURI, int scene, String reserved) {
    WeiboConfigStorage storage = this.wxMpService.getWxMpConfigStorage();
    return String.format(WeiboMpApiUrl.SubscribeMsg.SUBSCRIBE_MESSAGE_AUTHORIZE_URL.getUrl(storage), storage.getAppId(), scene, storage.getTemplateId(),
      URIUtil.encodeURIComponent(redirectURI), reserved);
  }

  @Override
  public boolean sendSubscribeMessage(WxMpSubscribeMessage message) throws WeiboErrorException {
    if (message.getTemplateId() == null) {
      message.setTemplateId(this.wxMpService.getWxMpConfigStorage().getTemplateId());
    }

    String responseContent = this.wxMpService.post(WeiboMpApiUrl.SubscribeMsg.SEND_MESSAGE_URL, message.toJson());
    return responseContent != null;
  }
}
