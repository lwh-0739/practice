package l.w.h.jschpractice.init;

import l.w.h.jschpractice.util.Util;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @author lwh
 * @date 2021/3/30 10:58
 **/
@Component
public class Init implements CommandLineRunner {

    @Override
    public void run(String... args) {
        String osName = System.getProperty("os.name");
        if (Util.stringContainAnotherString(osName,Util.WIN,true)){
            Util.setOsIsLinux(false);
            Util.setEncoding("GBK");
        }else {
            Util.setOsIsLinux(true);
            Util.setEncoding("UTF-8");
        }
    }

}
