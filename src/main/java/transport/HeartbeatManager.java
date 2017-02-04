package transport;

import api.account.Login;
import common.Client;

/**
 * 心跳管理.
 *
 * @author Yohann.
 */
public class HeartbeatManager extends Client {
    /**
     * 发送心跳
     */
    public void heartbeat() {
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.HEARTBEAT);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody("");
        mChannel.writeAndFlush(messageHolder);
    }

    /**
     * 断线处理
     */
    public void inActive() {
        if (Client.isLogout) {
            mReceiver.systemNotice("退出登录");
        } else {
            // 意外断线，执行重连
            mReceiver.systemNotice("与服务器断开连接，正在尝试重新连接......");
            if (mToken != null) {
                Login.login(mToken);
            } else {
                mReceiver.systemNotice("登录信息已失效，请重新登录");
            }
        }
    }
}
