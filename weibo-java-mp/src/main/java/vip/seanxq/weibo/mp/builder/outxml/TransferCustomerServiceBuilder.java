package vip.seanxq.weibo.mp.builder.outxml;

import org.apache.commons.lang3.StringUtils;

import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutTransferKefuMessage;

/**
 * 客服消息builder
 * <pre>
 * 用法: WeiboMpXmlOutTransferKefuMessage m = WeiboMpXmlOutMessage.TRANSFER_CUSTOMER_SERVICE().kfAccount("").toUser("").build();
 * </pre>
 *
 * @author chanjarster
 */
public final class TransferCustomerServiceBuilder
  extends BaseBuilder<TransferCustomerServiceBuilder, WeiboMpXmlOutTransferKefuMessage> {
  private String kfAccount;

  public TransferCustomerServiceBuilder kfAccount(String kf) {
    this.kfAccount = kf;
    return this;
  }

  @Override
  public WeiboMpXmlOutTransferKefuMessage build() {
    WeiboMpXmlOutTransferKefuMessage m = new WeiboMpXmlOutTransferKefuMessage();
    setCommon(m);
    if (StringUtils.isNotBlank(this.kfAccount)) {
      WeiboMpXmlOutTransferKefuMessage.TransInfo transInfo = new WeiboMpXmlOutTransferKefuMessage.TransInfo();
      transInfo.setKfAccount(this.kfAccount);
      m.setTransInfo(transInfo);
    }

    return m;
  }
}
