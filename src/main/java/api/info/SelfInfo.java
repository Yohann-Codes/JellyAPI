package api.info;

import api.future.Future;
import api.future.SelfInfoFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Info;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 查看我的信息API.
 *
 * Future future = SelfInfo.query();
 * future.addListener(new SelfInfoFutureListener() {
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
public class SelfInfo extends Client {
    public static final Future<SelfInfoFutureListener> future = new Future<>();

    /**
     * 调用此方法查询我的信息
     *
     * @return future
     */
    public static Future query() {
        doQuery();
        return future;
    }

    private static void doQuery() {
        Info info = init();
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.LOOK_SELF_INFO);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(info));
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

    private static Info init() {
        Info info = new Info();
        info.setUsername(mUsername);
        return info;
    }
}
