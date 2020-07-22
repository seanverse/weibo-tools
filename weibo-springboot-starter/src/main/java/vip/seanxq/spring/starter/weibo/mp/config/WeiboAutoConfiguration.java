package vip.seanxq.spring.starter.weibo.mp.config;

import vip.seanxq.spring.starter.weibo.mp.properties.WeiboMpProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * .
 *
 * @author someone
 */
@Configuration
@EnableConfigurationProperties(WeiboMpProperties.class)
@Import({WeiboStorageAutoConfiguration.class, WeiboServiceAutoConfiguration.class})
public class WeiboAutoConfiguration {
}
