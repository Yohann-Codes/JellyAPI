package common;

import api.future.Receiver;
import io.netty.channel.Channel;

/**
 * 保存用户登录的信息
 * <p>
 * @author Yohann.
 */
public class Client {
    /**
     * 用户名
     */
    protected static String mUsername;

    /**
     * 消息传输
     */
    protected static Channel mChannel;

    /**
     * 断线重连的验证信息
     */
    protected static Long mToken;

    /**
     * 登出标志
     */
    protected static boolean isLogout;

    /**
     * 回调MessageReceiver中的方法
     */
    protected static Receiver mReceiver;
}
