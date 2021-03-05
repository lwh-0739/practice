package l.w.h.jschpractice.config;

import l.w.h.jschpractice.handler.SshWebSocketHandler;
import l.w.h.jschpractice.interceptor.WebSocketInterceptor;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import javax.annotation.Resource;

/**
 * @author lwh
 * @date 2021/2/18 11:35
 **/
@SpringBootConfiguration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Resource
    private SshWebSocketHandler sshWebSocketHandler;

    @Resource
    private WebSocketInterceptor webSocketInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry webSocketHandlerRegistry) {
        webSocketHandlerRegistry.addHandler(sshWebSocketHandler,"/web_ssh")
                                .addInterceptors(webSocketInterceptor)
                                .setAllowedOrigins("*");
    }

}
