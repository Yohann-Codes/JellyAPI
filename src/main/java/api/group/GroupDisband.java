package api.group;

import api.future.Future;
import api.future.GroupDisbandFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Group;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 解散讨论组API.
 *
 * Future future = GroupDisband.disband(groupName);
 * future.addListener(new GroupDisbandFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 讨论组解散成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 讨论组解散失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class GroupDisband extends Client {
    public static final Future<GroupDisbandFutureListener> future = new Future<>();

    /**
     * 调用此方法解散讨论组
     *
     * @param groupName 讨论组名称
     * @return future
     */
    public static Future disband(String groupName) {
        doDisband(groupName);
        return future;
    }

    private static void doDisband(String groupName) {
        Group group = init(groupName);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.DISBAND_GROUP);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(group));
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

    private static Group init(String groupName) {
        Group group = new Group();
        group.setUsername(mUsername);
        group.setGroupName(groupName);
        return group;
    }
}
