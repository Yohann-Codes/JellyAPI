package api.group;

import api.future.Future;
import api.future.MemberAddFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Member;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 讨论组添加成员API.
 *
 * Future future = MemberAdd.add(groupName, username);
 * future.addListener(new MemberAddFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 讨论组添加成员成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 讨论组添加成员失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class MemberAdd extends Client {
    public static final Future<MemberAddFutureListener> future = new Future<>();

    /**
     * 调用此方法添加讨论组成员
     *
     * @param groupName 讨论组名称
     * @param username 要添加的成员用户名
     * @return future
     */
    public static Future add(String groupName, String username) {
        doAdd(groupName, username);
        return future;
    }

    private static void doAdd(String groupName, String username) {
        Member member = init(groupName, username);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.ADD_MEMBER);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(member));
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

    private static Member init(String groupName, String username) {
        Member member = new Member();
        member.setUsername(mUsername);
        member.setMember(username);
        member.setGroupName(groupName);
        return member;
    }
}
