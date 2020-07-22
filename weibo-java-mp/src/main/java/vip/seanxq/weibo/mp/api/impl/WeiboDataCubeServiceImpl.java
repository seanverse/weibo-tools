package vip.seanxq.weibo.mp.api.impl;

import com.google.gson.JsonObject;
import lombok.RequiredArgsConstructor;
import vip.seanxq.weibo.common.error.WeiboErrorException;
import vip.seanxq.weibo.mp.api.WeiboDataCubeService;
import vip.seanxq.weibo.mp.api.WeiboMpService;

import vip.seanxq.weibo.mp.enums.WeiboMpApiUrl;
import org.apache.commons.lang3.time.FastDateFormat;
import vip.seanxq.weibo.mp.bean.datacube.*;

import java.text.Format;
import java.util.Date;
import java.util.List;

/**
 * Created by Binary Wang on 2016/8/23.
 *
 * @author binarywang (https://github.com/binarywang)
 */
@RequiredArgsConstructor
public class WeiboDataCubeServiceImpl implements WeiboDataCubeService {
  private final Format dateFormat = FastDateFormat.getInstance("yyyy-MM-dd");

  private final WeiboMpService wxMpService;

  @Override
  public List<WbDataCubeUserSummary> getUserSummary(Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.DataCube.GET_USER_SUMMARY, buildParams(beginDate, endDate));
    return WbDataCubeUserSummary.fromJson(responseContent);
  }

  @Override
  public List<WbDataCubeUserCumulate> getUserCumulate(Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.DataCube.GET_USER_CUMULATE, buildParams(beginDate, endDate));
    return WbDataCubeUserCumulate.fromJson(responseContent);
  }

  @Override
  public List<WbDataCubeArticleResult> getArticleSummary(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getArticleResults(WeiboMpApiUrl.DataCube.GET_ARTICLE_SUMMARY, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeArticleTotal> getArticleTotal(Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.DataCube.GET_ARTICLE_TOTAL, buildParams(beginDate, endDate));
    return WbDataCubeArticleTotal.fromJson(responseContent);
  }

  @Override
  public List<WbDataCubeArticleResult> getUserRead(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getArticleResults(WeiboMpApiUrl.DataCube.GET_USER_READ, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeArticleResult> getUserReadHour(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getArticleResults(WeiboMpApiUrl.DataCube.GET_USER_READ_HOUR, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeArticleResult> getUserShare(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getArticleResults(WeiboMpApiUrl.DataCube.GET_USER_SHARE, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeArticleResult> getUserShareHour(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getArticleResults(WeiboMpApiUrl.DataCube.GET_USER_SHARE_HOUR, beginDate, endDate);
  }

  private List<WbDataCubeArticleResult> getArticleResults(WeiboMpApiUrl url, Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(url, buildParams(beginDate, endDate));
    return WbDataCubeArticleResult.fromJson(responseContent);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsg(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsgHour(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG_HOUR, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsgWeek(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG_WEEK, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsgMonth(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG_MONTH, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsgDist(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG_DIST, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsgDistWeek(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG_DIST_WEEK, beginDate, endDate);
  }

  @Override
  public List<WbDataCubeMsgResult> getUpstreamMsgDistMonth(Date beginDate, Date endDate) throws WeiboErrorException {
    return this.getUpstreamMsg(WeiboMpApiUrl.DataCube.GET_UPSTREAM_MSG_DIST_MONTH, beginDate, endDate);
  }

  private List<WbDataCubeMsgResult> getUpstreamMsg(WeiboMpApiUrl url, Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(url, buildParams(beginDate, endDate));
    return WbDataCubeMsgResult.fromJson(responseContent);
  }

  @Override
  public List<WbDataCubeInterfaceResult> getInterfaceSummary(Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.DataCube.GET_INTERFACE_SUMMARY, buildParams(beginDate, endDate));
    return WbDataCubeInterfaceResult.fromJson(responseContent);
  }

  private String buildParams(Date beginDate, Date endDate) {
    JsonObject param = new JsonObject();
    param.addProperty("begin_date", this.dateFormat.format(beginDate));
    param.addProperty("end_date", this.dateFormat.format(endDate));
    return param.toString();
  }

  @Override
  public List<WbDataCubeInterfaceResult> getInterfaceSummaryHour(Date beginDate, Date endDate) throws WeiboErrorException {
    String responseContent = this.wxMpService.post(WeiboMpApiUrl.DataCube.GET_INTERFACE_SUMMARY_HOUR, buildParams(beginDate, endDate));
    return WbDataCubeInterfaceResult.fromJson(responseContent);
  }
}
