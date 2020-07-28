package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.mp.api.WeiboFansMessageRouter;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlMessage;
import vip.seanxq.weibo.mp.bean.message.WeiboMpXmlOutMessage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Daniel Qian
 */
public class WbMpEndpointServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected WeiboConfigStorage weiboConfigStorage;
  protected WeiboMpService wxMpService;
  protected WeiboFansMessageRouter weiboFansMessageRouter;

  public WbMpEndpointServlet(WeiboConfigStorage weiboConfigStorage, WeiboMpService wxMpService,
                             WeiboFansMessageRouter weiboFansMessageRouter) {
    this.weiboConfigStorage = weiboConfigStorage;
    this.wxMpService = wxMpService;
    this.weiboFansMessageRouter = weiboFansMessageRouter;
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException {

    response.setContentType("text/html;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);

    String signature = request.getParameter("signature");
    String nonce = request.getParameter("nonce");
    String timestamp = request.getParameter("timestamp");

    if (!this.wxMpService.checkSignature(timestamp, nonce, signature)) {
      // 消息签名不正确，说明不是公众平台发过来的消息
      response.getWriter().println("非法请求");
      return;
    }

    String echostr = request.getParameter("echostr");
    if (StringUtils.isNotBlank(echostr)) {
      // 说明是一个仅仅用来验证的请求，回显echostr
      response.getWriter().println(echostr);
      return;
    }

    String encryptType = StringUtils.isBlank(request.getParameter("encrypt_type")) ?
      "raw" :
      request.getParameter("encrypt_type");

    if ("raw".equals(encryptType)) {
      // 明文传输的消息
      WeiboMpXmlMessage inMessage = WeiboMpXmlMessage.fromXml(request.getInputStream());
      WeiboMpXmlOutMessage outMessage = this.weiboFansMessageRouter.route(inMessage);
      if (outMessage != null) {
        response.getWriter().write(outMessage.toXml());
      }
      return;
    }

    if ("aes".equals(encryptType)) {
      // 是aes加密的消息
      String msgSignature = request.getParameter("msg_signature");
      WeiboMpXmlMessage inMessage = WeiboMpXmlMessage.fromEncryptedXml(request.getInputStream(), this.weiboConfigStorage, timestamp, nonce, msgSignature);
      WeiboMpXmlOutMessage outMessage = this.weiboFansMessageRouter.route(inMessage);
      response.getWriter().write(outMessage.toEncryptedXml(this.weiboConfigStorage));
      return;
    }

    response.getWriter().println("不可识别的加密类型");
    return;
  }

}
