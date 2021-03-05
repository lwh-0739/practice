package l.w.h.jschpractice.support;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author lwh
 * @date 2021/3/4 10:35
 **/
@Getter
@Setter
@NoArgsConstructor
public class WebSshData {

    private String operate;
    private String host;
    private Integer port = 22;
    private String username;
    private String password;
    private String command = "";

}
