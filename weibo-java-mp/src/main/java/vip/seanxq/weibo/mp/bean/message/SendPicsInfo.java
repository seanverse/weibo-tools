package vip.seanxq.weibo.mp.bean.message;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamConverter;
import lombok.Data;
import vip.seanxq.weibo.common.util.xml.XStreamCDataConverter;
import vip.seanxq.weibo.mp.util.json.WbMpGsonBuilder;

/**
 * <pre>
 *  Created by BinaryWang on 2017/5/4.
 * </pre>
 *
 * @author Binary Wang
 */
@XStreamAlias("SendPicsInfo")
@Data
public class SendPicsInfo implements Serializable {
  private static final long serialVersionUID = -4572837013294199227L;

  @XStreamAlias("PicList")
  protected final List<Item> picList = new ArrayList<>();

  @XStreamAlias("Count")
  private Long count;

  @Override
  public String toString() {
    return WbMpGsonBuilder.create().toJson(this);
  }

  @XStreamAlias("item")
  @Data
  public static class Item implements Serializable {
    private static final long serialVersionUID = 7706235740094081194L;

    @XStreamAlias("PicMd5Sum")
    @XStreamConverter(value = XStreamCDataConverter.class)
    private String picMd5Sum;

    @Override
    public String toString() {
      return WbMpGsonBuilder.create().toJson(this);
    }

  }
}
