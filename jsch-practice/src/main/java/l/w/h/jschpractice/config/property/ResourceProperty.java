package l.w.h.jschpractice.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lwh
 * @date 2021/3/30 11:57
 **/
@Component
@ConfigurationProperties(prefix = "resource")
@Getter
@Setter
@NoArgsConstructor
public class ResourceProperty {

    /**
     * 资源路径：默认使用内部路径
     * 内部路径：classpath:/static/
     * 外部路径：file:./static/
     */
    private String path = "classpath:/static/";

}
