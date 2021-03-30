package l.w.h.jschpractice.config;

import l.w.h.jschpractice.config.property.SwaggerInfo;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * @author lwh
 * @date 2021/3/4 14:33
 **/
@SpringBootConfiguration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

    @Resource
    private SwaggerInfo swaggerInfo;

    /**
     * 路径前缀
     */
    public static final String PATH_PRE = "/linux_operate/v_1_0";

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
        if (swaggerInfo.getEnable()){
            registry.addResourceHandler("swagger-ui.html")
                    .addResourceLocations("classpath:/META-INF/resources/");
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
    }

}
