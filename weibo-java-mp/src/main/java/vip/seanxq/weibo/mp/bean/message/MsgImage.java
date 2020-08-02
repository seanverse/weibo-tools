package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 位置信息
 */
@Data
public class MsgImage implements Serializable {
  private static final long serialVersionUID = 8547235440502365L;
  /**
   * 发送者用此ID查看图片
   */
  private String vfid;
  /**
   * 接收者用此ID查看图片
   */
  private String tovfid;


  //语音文件下载需要另外再测试

  /**
   * 查看（下载）图片
   * curl "https://upload.api.weibo.com/2/mss/msget?access_token=RECIPIENT_ACCESS_TOKEN&fid=TOVFID"
   *
   * 1，"RECIPIENT_ACCESS_TOKEN"：返回结果中接收者（receiver_id）通过OAuth2授权返回的access_token；
   * 2，"TOVFID"：返回结果data字段中的tovfid。
   */
}

