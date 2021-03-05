package l.w.h.jschpractice.service;

import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

/**
 * @author lwh
 * @date 2021/2/19 11:12
 **/
public interface SshService {

    /**
     * 初始化连接
     * @param webSocketSession  webSocketSession
     */
    void initSshConnection(WebSocketSession webSocketSession);

    /**
     * 处理客户端发送的数据
     * @param buffer                数据
     * @param webSocketSession      webSocketSession
     */
    void recvHandle(String buffer, WebSocketSession webSocketSession);

    /**
     * 发送消息
     * @param webSocketSession
     * @param buffer
     * @throws IOException
     */
    void sendMessage(WebSocketSession webSocketSession, byte[] buffer) throws IOException;

    /**
     * 关闭连接
     * @param webSocketSession
     */
    void close(WebSocketSession webSocketSession);

}
