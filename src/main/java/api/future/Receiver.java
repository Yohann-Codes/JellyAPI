package api.future;

/**
 * @author Yohann.
 */
public interface Receiver {

    /**
     * 重写此方法接收系统消息 e.g.退出登录/断线...
     *
     * @param notice 通知内容
     */
    void systemNotice(String notice);

    /**
     * 重写此方法接收个人消息
     *
     * @param sender 发送者用户名
     * @param content 消息内容
     * @param time 发送时间
     */
    void personMessage(String sender, String content, Long time);
}
