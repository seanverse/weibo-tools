/*
 * KINGSTAR MEDIA SOLUTIONS Co.,LTD. Copyright c 2005-2013. All rights reserved.
 *
 * This source code is the property of KINGSTAR MEDIA SOLUTIONS LTD. It is intended
 * only for the use of KINGSTAR MEDIA application development. Reengineering, reproduction
 * arose from modification of the original source, or other redistribution of this source
 * is not permitted without written permission of the KINGSTAR MEDIA SOLUTIONS LTD.
 */
package vip.seanxq.weibo.common.util.json;

import com.google.gson.*;
import vip.seanxq.weibo.common.bean.menu.WeiboMenu;
import vip.seanxq.weibo.common.bean.menu.WeiboMenuButton;

import java.lang.reflect.Type;


/**
 * @author Daniel Qian
 */
public class WeiboMenuGsonAdapter implements JsonSerializer<WeiboMenu>, JsonDeserializer<WeiboMenu> {

  @Override
  public JsonElement serialize(WeiboMenu menu, Type typeOfSrc, JsonSerializationContext context) {
    JsonObject json = new JsonObject();

    JsonArray buttonArray = new JsonArray();
    for (WeiboMenuButton button : menu.getButtons()) {
      JsonObject buttonJson = convertToJson(button);
      buttonArray.add(buttonJson);
    }
    json.add("button", buttonArray);

    return json;
  }

  protected JsonObject convertToJson(WeiboMenuButton button) {
    JsonObject buttonJson = new JsonObject();
    buttonJson.addProperty("type", button.getType());
    buttonJson.addProperty("name", button.getName());
    buttonJson.addProperty("key", button.getKey());
    if (button.getUrl() != null)
      buttonJson.addProperty("url", button.getUrl());

    if (button.getSubButtons() != null && button.getSubButtons().size() > 0) {
      JsonArray buttonArray = new JsonArray();
      for (WeiboMenuButton sub_button : button.getSubButtons()) {
        buttonArray.add(convertToJson(sub_button));
      }
      buttonJson.add("sub_button", buttonArray);
    }
    return buttonJson;
  }

  @Override
  public WeiboMenu deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
    /*
     * 操蛋的微博
     * 创建菜单时是 { button : ... }
     * 查询菜单时是 { menu : { button : ... } }
     * 现在企业号升级为企业微博后，没有此问题，因此需要单独处理
     */
    JsonArray buttonsJson = json.getAsJsonObject().get("menu").getAsJsonObject().get("button").getAsJsonArray();
    return this.buildMenuFromJson(buttonsJson);
  }

  protected WeiboMenu buildMenuFromJson(JsonArray buttonsJson) {
    WeiboMenu menu = new WeiboMenu();
    for (int i = 0; i < buttonsJson.size(); i++) {
      JsonObject buttonJson = buttonsJson.get(i).getAsJsonObject();
      WeiboMenuButton button = convertFromJson(buttonJson);
      menu.getButtons().add(button);
      if (buttonJson.get("sub_button") == null || buttonJson.get("sub_button").isJsonNull()) {
        continue;
      }
      JsonArray sub_buttonsJson = buttonJson.get("sub_button").getAsJsonArray();
      for (int j = 0; j < sub_buttonsJson.size(); j++) {
        JsonObject sub_buttonJson = sub_buttonsJson.get(j).getAsJsonObject();
        button.getSubButtons().add(convertFromJson(sub_buttonJson));
      }
    }
    return menu;
  }

  protected WeiboMenuButton convertFromJson(JsonObject json) {
    WeiboMenuButton button = new WeiboMenuButton();
    button.setName(GsonHelper.getString(json, "name"));
    button.setKey(GsonHelper.getString(json, "key"));
    if (json.has("url"))
      button.setUrl(GsonHelper.getString(json, "url"));

    button.setType(GsonHelper.getString(json, "type"));
    return button;
  }

}
