package l.w.h.jschpractice.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import l.w.h.jschpractice.constant.ConstantPool;
import l.w.h.jschpractice.factory.CustomThreadFactory;
import l.w.h.jschpractice.service.SshService;
import l.w.h.jschpractice.support.SshConnectionInfo;
import l.w.h.jschpractice.config.WebSshData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author lwh
 * @date 2021/2/19 11:13
 **/
@Service
public class SshServiceImpl implements SshService {

    private Logger logger = LoggerFactory.getLogger(SshServiceImpl.class);

    private ExecutorService executorService = new ThreadPoolExecutor(1,5,60, TimeUnit.SECONDS, new SynchronousQueue<>(),new CustomThreadFactory("web_ssh"));

    @Override
    public void initSshConnection(WebSocketSession webSocketSession) {
        ConstantPool.TOKEN_MAP.put(findUserId(webSocketSession), SshConnectionInfo
                .builder()
                .jSch(new JSch())
                .webSocketSession(webSocketSession)
                .build());
    }

    /**
     * 获取用户标识
     */
    private String findUserId(WebSocketSession webSocketSession){
        return String.valueOf(webSocketSession.getAttributes().get(ConstantPool.USER_UUID_KEY));
    }

    @Override
    public void recvHandle(String buffer, WebSocketSession webSocketSession) {
        WebSshData webSshData;
        try {
            webSshData = new ObjectMapper().readValue(buffer, WebSshData.class);
        } catch (IOException e) {
            logger.error("JSON转换异常：" + e.getMessage());
            return;
        }
        String userId = findUserId(webSocketSession),operateType;
        if (ConstantPool.WEB_SSH_OPERATE_CONNECT.equals(operateType = webSshData.getOperate())) {
            //启动线程异步处理
            executorService.execute(() -> {
                try {
                    connectToSsh(ConstantPool.TOKEN_MAP.get(userId), webSshData, webSocketSession);
                } catch (JSchException | IOException e) {
                    logger.error("webSsh连接异常：" + e.getMessage());
                    close(webSocketSession);
                }
            });
        } else if (ConstantPool.WEB_SSH_OPERATE_COMMAND.equals(operateType)) {
            SshConnectionInfo sshConnectInfo = ConstantPool.TOKEN_MAP.get(userId);
            if (sshConnectInfo != null) {
                try {
                    transToSsh(sshConnectInfo.getChannelShell(), webSshData.getCommand());
                } catch (IOException e) {
                    logger.error("webSsh连接异常：" + e.getMessage());
                    close(webSocketSession);
                }
            }
        } else {
            logger.warn("操作类型异常：" + operateType);
            close(webSocketSession);
        }
    }

    /**
     * 连接服务器
     */
    private void connectToSsh(SshConnectionInfo sshConnectInfo, WebSshData webSshData, WebSocketSession webSocketSession) throws JSchException, IOException {
        Session session = sshConnectInfo.getJSch().getSession(webSshData.getUsername(), webSshData.getHost(), webSshData.getPort());
        session.setConfig("StrictHostKeyChecking", "no");
        session.setPassword(webSshData.getPassword());
        //连接  超时时间30s
        session.connect(30000);
        //开启shell通道
        ChannelShell channelShell = (ChannelShell)session.openChannel("shell");
        //通道连接 超时时间3s
        channelShell.connect(3000);
        //设置channel
        sshConnectInfo.setChannelShell(channelShell);
        //读取终端返回的信息流
        try (InputStream inputStream = channelShell.getInputStream()) {
            //循环读取
            byte[] buffer = new byte[1024];
            int i;
            //如果没有数据来，线程会一直阻塞在这个地方等待数据。
            while ((i = inputStream.read(buffer)) != -1) {
                sendMessage(webSocketSession, Arrays.copyOfRange(buffer, 0, i));
            }
        } finally {
            //断开连接后关闭会话
            session.disconnect();
            channelShell.disconnect();
        }
    }

    /**
     * 发送指令至服务器
     */
    private void transToSsh(ChannelShell channelShell, String command) throws IOException {
        if (channelShell != null) {
            OutputStream outputStream = channelShell.getOutputStream();
            outputStream.write(command.getBytes());
            outputStream.flush();
        }
    }

    @Override
    public void sendMessage(WebSocketSession webSocketSession, byte[] buffer) throws IOException {
        webSocketSession.sendMessage(new TextMessage(buffer));
    }

    @Override
    public void close(WebSocketSession webSocketSession) {
        String userId;
        SshConnectionInfo sshConnectInfo;
        if (null != (sshConnectInfo = ConstantPool.TOKEN_MAP.get(userId = findUserId(webSocketSession)))) {
            //断开连接
            if (sshConnectInfo.getChannelShell() != null){
                sshConnectInfo.getChannelShell().disconnect();
            }
            //map中移除
            ConstantPool.TOKEN_MAP.remove(userId);
        }
    }

}
