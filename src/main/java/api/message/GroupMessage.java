package api.message;

import api.future.Future;
import api.future.GroupMessageFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Message;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 发送讨论组消息API.
 *
 * Future future = GroupMessage.send(groupName, content);
 * future.addListener(new GroupMessageFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 发送成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 发送失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class GroupMessage extends Client {
    public static final Future<GroupMessageFutureListener> future = new Future<>();

    /**
     * 调用此方法发送讨论组消息
     *
     * @param groupName 讨论组名称
     * @param content 消息内容
     * @return future
     */
    public static Future send(String groupName, String content) {
        doSend(groupName, content);
        return future;
    }

    private static void doSend(String groupName, String content) {
        Message message = init(groupName, content);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.GROUP_MESSAGE);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(message));
        ChannelFuture future = mChannel.writeAndFlush(messageHolder);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    mChannel.writeAndFlush(messageHolder);
                }
            }
        });
    }

    private static Message init(String groupName, String content) {
        Message message = new Message();
        message.setSender(mUsername);
        message.setReceiver(groupName);
        message.setContent(content);
        message.setTime(System.currentTimeMillis());
        return message;
    }
}
