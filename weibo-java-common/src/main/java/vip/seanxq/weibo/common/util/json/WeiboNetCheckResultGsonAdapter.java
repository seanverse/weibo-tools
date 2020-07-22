package vip.seanxq.weibo.common.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.bean.WeiboNetCheckResult;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


/**
 * @author billytomato
 */
public class WeiboNetCheckResultGsonAdapter implements JsonDeserializer<WeiboNetCheckResult> {


  @Override
  public WeiboNetCheckResult deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    WeiboNetCheckResult result = new WeiboNetCheckResult();

    JsonArray dnssJson = json.getAsJsonObject().get("dns").getAsJsonArray();
    List<WeiboNetCheckResult.WxNetCheckDnsInfo> dnsInfoList = new ArrayList<>();
    if (dnssJson != null && dnssJson.size() > 0) {
      for (int i = 0; i < dnssJson.size(); i++) {
        JsonObject buttonJson = dnssJson.get(i).getAsJsonObject();
        WeiboNetCheckResult.WxNetCheckDnsInfo dnsInfo = new WeiboNetCheckResult.WxNetCheckDnsInfo();
        dnsInfo.setIp(GsonHelper.getString(buttonJson, "ip"));
        dnsInfo.setRealOperator(GsonHelper.getString(buttonJson, "real_operator"));
        dnsInfoList.add(dnsInfo);
      }
    }

    JsonArray pingsJson = json.getAsJsonObject().get("ping").getAsJsonArray();
    List<WeiboNetCheckResult.WxNetCheckPingInfo> pingInfoList = new ArrayList<>();
    if (pingsJson != null && pingsJson.size() > 0) {
      for (int i = 0; i < pingsJson.size(); i++) {
        JsonObject pingJson = pingsJson.get(i).getAsJsonObject();
        WeiboNetCheckResult.WxNetCheckPingInfo pingInfo = new WeiboNetCheckResult.WxNetCheckPingInfo();
        pingInfo.setIp(GsonHelper.getString(pingJson, "ip"));
        pingInfo.setFromOperator(GsonHelper.getString(pingJson, "from_operator"));
        pingInfo.setPackageLoss(GsonHelper.getString(pingJson, "package_loss"));
        pingInfo.setTime(GsonHelper.getString(pingJson, "time"));
        pingInfoList.add(pingInfo);
      }
    }
    result.setDnsInfos(dnsInfoList);
    result.setPingInfos(pingInfoList);
    return result;
  }

}
