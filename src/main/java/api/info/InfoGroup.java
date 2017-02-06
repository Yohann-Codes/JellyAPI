package api.info;

import api.future.Future;
import api.future.InfoGroupFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.MyGroup;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 查询讨论组信息API.
 *
 * Future future = InfoGroup.query();
 * future.addListener(new InfoGroupFutureListener() {
 *     @Override
 *     public void onSuccess(Map<String, List<String>> groups) {
 *         // 查询成功
 *         // key -> 讨论组名称
 *         // value -> 讨论组成员
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
public class InfoGroup extends Client {
    public static final Future<InfoGroupFutureListener> future = new Future<>();

    /**
     * 调用此方法查询讨论组的信息
     *
     * @return future
     */
    public static Future query() {
        doQuery();
        return future;
    }

    private static void doQuery() {
        MyGroup myGroup = init();
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.LOOK_GROUP_INFO);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(myGroup));
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

    private static MyGroup init() {
        MyGroup myGroup = new MyGroup();
        myGroup.setUsername(mUsername);
        return myGroup;
    }
}
