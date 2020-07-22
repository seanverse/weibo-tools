package vip.seanxq.weibo.common.error;

/**
 * @author Daniel Qian
 */
public class WeiboErrorException extends Exception {
  private static final long serialVersionUID = -6357149550353160810L;

  private WeiboError error;

  public WeiboErrorException(WeiboError error) {
    super(error.toString());
    this.error = error;
  }

  public WeiboErrorException(WeiboError error, Throwable cause) {
    super(error.toString(), cause);
    this.error = error;
  }

  public WeiboError getError() {
    return this.error;
  }


}
