package api.info;

import api.future.Future;
import api.future.InfoUpdateFutureListener;
import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Info;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 修改个人信息API.
 *
 * e.g.修改密码
 *
 * Future future = InfoUpdate.setPassword(password);
 * future.addListener(new InfoUpdateFutureListener() {
 *     @Override
 *     public void onSuccess() {
 *         // 密码修改成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 密码修改失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class InfoUpdate extends Client {
    public static final Future<InfoUpdateFutureListener> future = new Future<>();

    /**
     * 修改密码
     *
     * @param password
     */
    public static Future setPassword(String password) {
        Info info = new Info();
        info.setPassword(password);
        doUpdate(info);
        return future;
    }

    /**
     * 修改姓名
     *
     * @param name
     * @return
     */
    public static Future setName(String name) {
        Info info = new Info();
        info.setName(name);
        doUpdate(info);
        return future;
    }

    /**
     * 修改性别
     *
     * @param sex
     * @return
     */
    public static Future setSex(String sex) {
        Info info = new Info();
        info.setSex(sex);
        doUpdate(info);
        return future;
    }

    /**
     * 修改年龄
     *
     * @param age
     * @return
     */
    public static Future setAge(String age) {
        Info info = new Info();
        info.setAge(age);
        doUpdate(info);
        return future;
    }

    /**
     * 修改联系方式
     *
     * @param phone
     * @return
     */
    public static Future setPhone(String phone) {
        Info info = new Info();
        info.setPhone(phone);
        doUpdate(info);
        return future;
    }

    /**
     * 修改地址
     *
     * @param address
     * @return
     */
    public static Future setAddress(String address) {
        Info info = new Info();
        info.setAddress(address);
        doUpdate(info);
        return future;
    }

    /**
     * 修改个人简介
     *
     * @param introduction
     * @return
     */
    public static Future setIntroduction(String introduction) {
        Info info = new Info();
        info.setIntroduction(introduction);
        doUpdate(info);
        return future;
    }

    private static void doUpdate(Info info) {
        info.setUsername(mUsername);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.UPDATE_SELF_INFO);
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
}
