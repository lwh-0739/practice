package l.w.h.jschpractice.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lwh
 * @date 2021/3/4 14:33
 **/
@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    /**
     * 静态资源配置
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**")
                // 外部静态资源
                .addResourceLocations("file:./static/");
                // .addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/ssh/**")
                .addResourceLocations("file:./static/");
                // .addResourceLocations("classpath:/static/");
    }

}
