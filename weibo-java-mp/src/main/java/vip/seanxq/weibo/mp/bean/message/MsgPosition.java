package vip.seanxq.weibo.mp.bean.message;

import lombok.Data;

import java.io.Serializable;

/**
 * 位置信息
 */
@Data
public class MsgPosition implements Serializable {
  private static final long serialVersionUID = 2145235440502365L;

  private String longitude;
  private String latitude;
}
