package l.w.h.jschpractice.support;

import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.socket.WebSocketSession;

/**
 * @author lwh
 * @date 2021/2/19 14:28
 **/
@Getter
@Setter
@Builder
public class SshConnectionInfo {

    private JSch jSch;

    private WebSocketSession webSocketSession;

    private ChannelShell channelShell;

}
