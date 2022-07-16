package l.w.h.springboottest;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lwh
 * @since 2022/7/16
 */
@Validated
@RestController
public class TestController {

    @GetMapping("/test")
    public void test(
            @RequestHeader(value = "test") Long test,
            @RequestParam(value = "a") Long a
//            @RequestHeader(value = "test",defaultValue = "") Long test
    ){
        System.out.println("test-" + test + "-" + a);
    }

}
