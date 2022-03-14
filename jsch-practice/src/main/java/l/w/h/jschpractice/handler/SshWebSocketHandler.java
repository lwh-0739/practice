package l.w.h.jschpractice.handler;

import l.w.h.jschpractice.constant.ConstantPool;
import l.w.h.jschpractice.service.SshService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.NonNullApi;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;

import javax.annotation.Resource;

/**
 * @author lwh
 * @date 2021/2/18 11:42
 **/
@Component
public class SshWebSocketHandler implements WebSocketHandler {

    private final Logger logger = LoggerFactory.getLogger(SshWebSocketHandler.class);

    @Resource
    private SshService sshService;

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) {
        logger.info("用户标识：{}，连接webSsh",webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY));
        sshService.initSshConnection(webSocketSession);
    }

    @Override
    public void handleMessage(@NonNull WebSocketSession webSocketSession, @NonNull WebSocketMessage<?> webSocketMessage) {
        if (webSocketMessage instanceof TextMessage) {
            logger.info("用户:{},发送命令:{}", webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY), webSocketMessage);
            sshService.recvHandle(((TextMessage) webSocketMessage).getPayload(), webSocketSession);
        }
//        else if (webSocketMessage instanceof BinaryMessage) {
//
//        } else if (webSocketMessage instanceof PongMessage) {
//
//        }
        else {
            System.out.println("Unexpected WebSocket message type: " + webSocketMessage);
        }
    }

    @Override
    public void handleTransportError(@NonNull WebSocketSession session, @NonNull Throwable exception) {
        logger.error("数据传输错误");
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession webSocketSession, @NonNull CloseStatus closeStatus) {
        logger.info("用户:{}断开webSsh连接", webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY));
        sshService.close(webSocketSession);
    }

    @Override
    public boolean supportsPartialMessages() {
        return false;
    }

}
