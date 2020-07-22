package vip.seanxq.spring.starter.weibo.mp.config;

import vip.seanxq.spring.starter.weibo.mp.properties.WeiboMpProperties;
import vip.seanxq.weibo.mp.api.*;
import vip.seanxq.weibo.mp.api.impl.WeiboMpServiceHttpClientImpl;
import vip.seanxq.weibo.mp.api.impl.WeiboMpServiceImpl;
import vip.seanxq.weibo.mp.config.WeiboConfigStorage;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微博公众号相关服务自动注册.
 *
 * @author someone
 */
@Configuration
public class WeiboServiceAutoConfiguration {

  /**
   * {@link @ConditionalOnMissingBean}注解作用在@bean定义上，它的作用就是在容器加载它作用的bean时，
   * 检查容器中是否存在目标类型（ConditionalOnMissingBean注解的value值）的bean了，如果存在这跳过原始bean的BeanDefinition加载动作。
   * @param configStorage
   * @param weiboMpProperties
   * @return
   */
  @Bean
  @ConditionalOnMissingBean
  //ConditionalOnMissingBean是作为斥条件，为空时表示当MPService有进行注册时，这里不再进行注册，避免重复注册
  public WeiboMpService wxMpService(WeiboConfigStorage configStorage, WeiboMpProperties weiboMpProperties) {
    WeiboMpProperties.HttpClientType httpClientType = weiboMpProperties.getConfigStorage().getHttpClientType();
    WeiboMpService wxMpService;
    /*if (httpClientType == WeiboMpProperties.HttpClientType.okhttp) {
      wxMpService = newWxMpServiceJoddHttpImpl();
    } else if (httpClientType == WeiboMpProperties.HttpClientType.joddhttp) {
      wxMpService = newWxMpServiceOkHttpImpl();
    } else */
    if (httpClientType == WeiboMpProperties.HttpClientType.httpclient) {
      wxMpService = newWxMpServiceHttpClientImpl();
    } else {
      wxMpService = newWxMpServiceImpl();
    }

    wxMpService.setWxMpConfigStorage(configStorage);
    return wxMpService;
  }

  private WeiboMpService newWxMpServiceImpl() {
    return new WeiboMpServiceImpl();
  }

  private WeiboMpService newWxMpServiceHttpClientImpl() {
    return new WeiboMpServiceHttpClientImpl();
  }


}
