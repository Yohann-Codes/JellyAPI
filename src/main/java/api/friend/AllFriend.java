package api.friend;

import api.future.AllFriendFutureListener;
import api.future.Future;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.MyFriend;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 查询已添加好友API.
 *
 * Future future = AllFriend.query();
 * future.addListener(new AllFriendFutureListener() {
 *     @Override
 *     public void onSuccess(Map<String, Boolean> friends) {
 *         // 查询好友成功
 *         // key -> 好友用户名
 *         // value -> 好友是否在线
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 查询好友失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class AllFriend extends Client {
    public static final Future<AllFriendFutureListener> future = new Future<>();

    /**
     * 调用此方法查询好友
     *
     * @return future
     */
    public static Future query() {
        doQuery();
        return future;
    }

    private static void doQuery() {
        MyFriend myFriend = init();
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.ALL_FRIEND);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(myFriend));
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

    private static MyFriend init() {
        MyFriend myFriend = new MyFriend();
        myFriend.setUsername(mUsername);
        return myFriend;
    }
}
