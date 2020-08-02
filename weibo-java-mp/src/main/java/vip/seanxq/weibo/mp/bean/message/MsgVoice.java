package vip.seanxq.weibo.mp.bean.message;


import lombok.Data;

import java.io.Serializable;

/**
 * 位置信息
 */
@Data
public class MsgVoice implements Serializable {
  private static final long serialVersionUID = 9625235440502365L;
  /**
   * 发送者用此ID查看语音
   */
  private String vfid;
  /**
   * 接收者用此ID查看语音
   */
  private String tovfid;


  //语音文件下载需要另外再测试

  /**
   * 查看（下载）认证用户接收到的语音方法一
   * curl "https://upload.api.weibo.com/2/mss/msget?access_token=RECIPIENT_ACCESS_TOKEN&fid=TOVFID"
   *
   * 1."RECIPIENT_ACCESS_TOKEN"：返回结果中接收者（recipient_id）通过OAuth2授权返回的access_token；
   * 2."TOVFID"：返回结果data字段中的tovfid。
   *
   * 查看（下载）认证用户接收到的语音方法二
   * curl -u "USERNAME:PASSWORD" "https://upload.api.weibo.com/2/mss/msget?source=APPKEY&fid=TOVFID"
   *
   * 1."USERNAME:PASSWORD"为"recipient_id"（认证用户）的微博登录用户名和密码，此时方法二中的APPKEY应用所有者为"recipient_id"；
   * 2."TOVFID"：返回结果data字段中的tovfid。
   */
}
