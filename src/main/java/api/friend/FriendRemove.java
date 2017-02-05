package api.friend;

import api.future.FriendRemoveFutureListener;
import api.future.Future;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Friend;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 删除好友API.
 *
 * Future future = FriendRemove.remove(friend);
 * future.addListener(new FriendRemoveFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 删除好友成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 删除好友失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class FriendRemove extends Client {
    public static final Future<FriendRemoveFutureListener> future = new Future<>();

    /**
     * 调用此方法删除好友
     *
     * @param friend 要删除的好友用户名
     * @return future
     */
    public static Future remove(String friend) {
        doRemove(friend);
        return future;
    }

    private static void doRemove(String friendUsername) {
        Friend friend = init(friendUsername);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.REMOVE_FRIEND);
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
