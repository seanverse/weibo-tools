package vip.seanxq.weibo.mp.demo;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboMpService;
import vip.seanxq.weibo.mp.bean.result.WeiboMpOAuth2AccessToken;
import vip.seanxq.weibo.mp.bean.result.WeiboMpUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class WbMpOAuth2Servlet extends HttpServlet {
  private static final long serialVersionUID = 1L;

  protected WeiboMpService wbMpService;

  public WbMpOAuth2Servlet(WeiboMpService wbMpService) {
    this.wbMpService = wbMpService;
  }

  @Override
  protected void service(HttpServletRequest request, HttpServletResponse response)
    throws IOException {

    response.setContentType("text/html;charset=utf-8");
    response.setStatus(HttpServletResponse.SC_OK);

    String code = request.getParameter("code");
    try {
      response.getWriter().println("<h1>code</h1>");
      response.getWriter().println(code);

      WeiboMpOAuth2AccessToken weiboMpOAuth2AccessToken = this.wbMpService.oauth2getAccessToken(code);
      response.getWriter().println("<h1>access token</h1>");
      response.getWriter().println(weiboMpOAuth2AccessToken.toString());

      WeiboMpUser weiboMpUser = this.wbMpService.oauth2getUserInfo(weiboMpOAuth2AccessToken, null);
      response.getWriter().println("<h1>user info</h1>");
      response.getWriter().println(weiboMpUser.toString());

      weiboMpOAuth2AccessToken = this.wbMpService.oauth2refreshAccessToken(weiboMpOAuth2AccessToken.getRefreshToken());
      response.getWriter().println("<h1>after refresh</h1>");
      response.getWriter().println(weiboMpOAuth2AccessToken.toString());

    } catch (WeiboErrorException e) {
      e.printStackTrace();
    }

    response.getWriter().flush();
    response.getWriter().close();

  }

}
