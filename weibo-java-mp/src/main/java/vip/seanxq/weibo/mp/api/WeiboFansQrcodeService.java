package vip.seanxq.weibo.mp.api;

import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.bean.result.WeiboFansQrCodeTicket;

import java.io.File;

/**
 * <pre>
 * 二维码相关操作接口
 * 文档地址：https://open.weibo.com/wiki/创建二维码ticket
 * </pre>
 *
 */
public interface WeiboFansQrcodeService {
  /**
   * <pre>
   * 换取临时二维码ticket
   * 详情请见: <a href="https://open.weibo.com/wiki/创建二维码ticket">生成带参数的二维码</a>
   * http请求方式: post
   * </pre>
   *
   * @param sceneId       场景值ID，临时二维码时为32位非0整型，永久二维码时最大值为100000（目前参数只支持1--100000）。
   * @param expireSeconds 该二维码有效时间，以秒为单位。 最大不超过1800秒（即30分钟。但是可能是微博的错误，微信是30天，需要测试）。
   */
  WeiboFansQrCodeTicket qrCodeCreateTmpTicket(int sceneId, String sceneStr, Integer expireSeconds) throws WeiboErrorException;


  /**
   * <pre>
   * 换取永久二维码ticket
   * 详情请见: <a href="https://open.weibo.com/wiki/创建二维码ticket">生成带参数的二维码</a>
   * http请求方式: post
   * </pre>
   *
   * @param sceneId 场景值ID，最大值为100000（目前参数只支持1--100000）
   */
  WeiboFansQrCodeTicket qrCodeCreateLastTicket(int sceneId,String sceneStr) throws WeiboErrorException;


  /**
   * <pre>
   * 换取二维码图片文件，jpg格式
   * 详情请见: <a href="https://open.weibo.com/wiki/通过ticket换取二维码">生成带参数的二维码</a>
   * http请求方式：post,https://api.weibo.com/2/eps/qrcode/show
   * </pre>
   * @param ticket 二维码ticket
   */
  File qrCodePicture(WeiboFansQrCodeTicket ticket) throws WeiboErrorException;

  /**
   * <pre>
   * 换取二维码图片url地址
   * 详情请见: <a href="https://open.weibo.com/wiki/通过ticket换取二维码">生成带参数的二维码</a>
   * http请求方式：post,https://api.weibo.com/2/eps/qrcode/show
   * </pre>   *
   * @param ticket 二维码ticket
   */
  String qrCodePictureUrl(WeiboFansQrCodeTicket ticket) throws WeiboErrorException;

}
