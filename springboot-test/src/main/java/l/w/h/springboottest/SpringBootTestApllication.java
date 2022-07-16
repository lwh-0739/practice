package l.w.h.springboottest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * spring boot 2.3.x（spring-web 5.2.x） 以及一下，设置参数必须时，调用URL时，可以传“”，而从2.4（spring-web 5.3.x）开始，这种不支持，必须为有效值（非空字符串）
 * 解决由于spring boot升级导致对于参数处理方式不一样的测试
 * 内部， AbstractNamedValueMethodArgumentResolver
 * @author lwh
 * @since 2022/7/16
 */
@SpringBootApplication
public class SpringBootTestApllication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootTestApllication.class,args);
    }

}
