/**
 * 对公众平台发送给公众账号的消息加解密示例代码.
 *
 * @copyright Copyright (c) 1998-2014 Tencent Inc.
 * <p>
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */

// ------------------------------------------------------------------------

/**
 * 针对org.apache.commons.codec.binary.Base64，
 * 需要导入架包commons-codec-1.9（或commons-codec-1.8等其他版本）
 * 官方下载地址：http://commons.apache.org/proper/commons-codec/download_codec.cgi
 */
package vip.seanxq.weibo.mp.util.crypto;

import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import org.apache.commons.codec.binary.Base64;
import vip.seanxq.weibo.common.util.crypto.WxCryptUtil;

public class WeiboMpCryptUtil extends WxCryptUtil {

  /**
   * 构造函数
   *
   * @param weiboConfigStorage
   */
  public WeiboMpCryptUtil(WeiboConfigStorage weiboConfigStorage) {
    /*
     * @param token          公众平台上，开发者设置的token
     * @param encodingAesKey 公众平台上，开发者设置的EncodingAESKey
     * @param appId          公众平台appid
     */
    String encodingAesKey = weiboConfigStorage.getAesKey();
    String token = weiboConfigStorage.getToken();
    String appId = weiboConfigStorage.getAppId();

    this.token = token;
    this.appidOrCorpid = appId;
    this.aesKey = Base64.decodeBase64(encodingAesKey + "=");
  }

}
