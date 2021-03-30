package l.w.h.jschpractice.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lwh
 * @date 2021/3/30 11:04
 **/
@Component
@ConfigurationProperties(prefix = "swagger.log")
@Getter
@Setter
@NoArgsConstructor
public class SwaggerInfo {

    private String groupName = "linux-operate";

    private String basePackage = "l.w.h.jschpractice.controller";

    private String title = "linux 文件管理";

    private String description = "文件管理";

    private String version = "1.0.0";

    private Boolean enable = false;

    private String contactName = "文件管理服务模块";

    private String contactEmail = "";

    private String contactUrl = "";

    private String license = "";

    private String licenseUrl = "";

}
