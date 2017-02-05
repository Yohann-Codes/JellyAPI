package api.message;

import api.future.Future;
import api.future.PersonMessageFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Message;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 发送个人消息API.
 *
 * Future future = PersonMessage.send(receiver, content);
 * future.addListener(new PersonMessageFutureListener() {
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
public class PersonMessage extends Client {
    public static final Future<PersonMessageFutureListener> future = new Future<>();

    /**
     * 调用此方法发送个人消息
     *
     * @param receiver 消息接受者用户名
     * @param content 消息内容
     * @return future
     */
    public static Future send(String receiver, String content) {
        doSend(receiver, content);
        return future;
    }

    private static void doSend(String receiver, String content) {
        Message message = init(receiver, content);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.PERSON_MESSAGE);
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

    private static Message init(String receiver, String content) {
        Message message = new Message();
        message.setSender(mUsername);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTime(System.currentTimeMillis());
        return message;
    }
}
