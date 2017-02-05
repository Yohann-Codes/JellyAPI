package api.friend;

import api.future.FriendAddFutureListener;
import api.future.Future;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Friend;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 添加好友API.
 *
 * Future future = FriendAdd.add(friend);
 * future.addListener(new FriendAddFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 好友添加成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 好友添加失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class FriendAdd extends Client {
    public static final Future<FriendAddFutureListener> future = new Future<>();

    /**
     * 调用此方法添加好友
     *
     * @param friend 要添加的好友用户名
     * @return future
     */
    public static Future add(String friend) {
        doAdd(friend);
        return future;
    }

    private static void doAdd(String friendUsername) {
        Friend friend = init(friendUsername);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.ADD_FRIEND);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(friend));
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

    private static Friend init(String friendUsername) {
        Friend friend = new Friend();
        friend.setUsername(mUsername);
        friend.setFriend(friendUsername);
        return friend;
    }
}
