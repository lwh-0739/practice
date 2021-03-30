package l.w.h.jschpractice.config;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author lwh
 * @date 2021/3/4 10:35
 **/
@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "service.config")
public class WebSshData {

    private String operate = "connect";
    private String host;
    private Integer port = 22;
    private String username;
    private String password;
    private String command = "";

}
