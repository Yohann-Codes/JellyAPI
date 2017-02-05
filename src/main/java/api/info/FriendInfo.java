package api.info;

import api.future.FriendInfoFutureListener;
import api.future.Future;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Friend;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 查询好友个人信息API.
 *
 * Future future = FriendInfo.query(friend);
 * future.addListener(new FriendInfoFutureListener() {
 *     @Override
 *     public void onSuccess(String username, String name, String sex,
 *                           String age, String phone, String address, String introduction) {
 *         // 查询成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 查询失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class FriendInfo extends Client {
    public static final Future<FriendInfoFutureListener> future = new Future<>();

    /**
     * 调用此方法查询好友的信息
     *
     * @param friend 好友用户名
     * @return future
     */
    public static Future query(String friend) {
        doQuery(friend);
        return future;
    }

    private static void doQuery(String friend) {
        Friend fri = init(friend);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.LOOK_FRIEND_INFO);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(fri));
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

    private static Friend init(String friend) {
        Friend fri = new Friend();
        fri.setUsername(mUsername);
        fri.setFriend(friend);
        return fri;
    }
}
