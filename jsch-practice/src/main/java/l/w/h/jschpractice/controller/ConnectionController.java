package l.w.h.jschpractice.controller;

import cn.zhgtv.common.result.BaseResponse;
import l.w.h.jschpractice.config.WebSshData;
import l.w.h.jschpractice.util.Util;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author lwh
 * @date 2021/3/26 11:21
 **/
@RestController
public class ConnectionController {

    @Resource
    private WebSshData webSshData;

    @GetMapping("connection_info")
    public BaseResponse<WebSshData> connectionInfo(){
        return Util.sendSuccessResponse(webSshData);
    }

}
