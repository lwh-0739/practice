package l.w.h.aoppractice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lwh
 * @since 2022/2/16 11:37
 **/
@RestController
public class TestController {

    private Logger logger = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/test")
    public String test(@RequestParam String id) throws Exception {
        logger.error("处理请求。。。");
        // throw new Exception("测试异常");
        return "完成";
    }

}
