package l.w.h.redissonpractice.config;

import org.redisson.config.Config;
import org.redisson.spring.starter.RedissonAutoConfigurationCustomizer;
import org.springframework.boot.SpringBootConfiguration;

/**
 * @author lwh
 * @since 2022/2/18 13:43
 **/
@SpringBootConfiguration
public class CustomRedissonConfigurationCustomizer implements RedissonAutoConfigurationCustomizer {
    @Override
    public void customize(Config configuration) {
        // 自定义redisson配置
        configuration.useSingleServer()
                .setConnectionPoolSize(10)
                .setConnectionMinimumIdleSize(4);
    }
}
