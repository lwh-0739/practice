package l.w.h.jschpractice.constant;

import l.w.h.jschpractice.support.SshConnectionInfo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author lwh
 * @date 2021/2/19 10:55
 **/
public class ConstantPool {

    public static final String USER_UUID_KEY = "uuid";

    /**
     * 发送指令：连接
     */
    public static final String WEB_SSH_OPERATE_CONNECT = "connect";
    /**
     * 发送指令：命令
     */
    public static final String WEB_SSH_OPERATE_COMMAND = "command";

    public static final Map<String, SshConnectionInfo> TOKEN_MAP = new HashMap<>(16);

}
