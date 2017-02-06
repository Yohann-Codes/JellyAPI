package api.future;

/**
 *
 * 消息接收接口.
 * 实现此接口来接收系统通知及用户消息.
 *
 * public class MyReceiver implements Receiver {
 *     @Override
 *     public void systemNotice(String notice) {
 *         // ...
 *     }
 *
 *     @Override
 *     public void reconnection(boolean isSuccess) {
 *         // ...
 *     }
 *
 *     @Override
 *     public void personMessage(String sender, String content, Long time) {
 *         // ...
 *     }
 *
 *     @Override
 *     public void groupMessage(String groupName, String sender, String content, Long time) {
 *         // ...
 *     }
 * }
 *
 *
 * @author Yohann.
 */
public interface Receiver {

    /**
     * 重写此方法接收系统消息 e.g.退出登录...
     *
     * @param notice 通知内容
     */
    void systemNotice(String notice);

    /**
     * 重写此方法接收断线重连结果
     *
     * @param isSuccess 重连成功/失败
     */
    void reconnection(boolean isSuccess);

    /**
     * 重写此方法接收个人消息
     *
     * @param sender 发送者用户名
     * @param content 消息内容
     * @param time 发送时间
     */
    void personMessage(String sender, String content, Long time);

    /**
     * 重写此方法接收讨论组消息
     *
     * @param groupName 讨论组名称
     * @param sender 发送者用户名
     * @param content 消息内容
     * @param time 发送时间
     */
    void groupMessage(String groupName, String sender, String content, Long time);
}
