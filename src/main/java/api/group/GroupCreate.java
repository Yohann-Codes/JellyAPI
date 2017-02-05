package api.group;

import api.future.Future;
import api.future.GroupCreateFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Group;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 创建讨论组API.
 *
 * Future future = GroupCreate.create(groupName);
 * future.addListener(new GroupCreateFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 讨论组创建成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 讨论组创建失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class GroupCreate extends Client {
    public static final Future<GroupCreateFutureListener> future = new Future<>();

    /**
     * 调用此方法创建讨论组
     *
     * @param groupName 讨论组名称
     * @return future
     */
    public static Future create(String groupName) {
        doCreate(groupName);
        return future;
    }

    private static void doCreate(String groupName) {
        Group group = init(groupName);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.CREATE_GROUP);
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
