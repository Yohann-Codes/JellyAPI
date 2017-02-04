package dispatch;

import api.account.Login;
import api.account.Register;
import common.Client;
import api.future.LoginFutureListener;
import api.future.RegisterFutureListener;
import io.netty.util.ReferenceCountUtil;
import pojo.Account;
import serialize.Serializer;
import transport.HeartbeatHandler;
import transport.HeartbeatManager;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 内容分发.
 *
 * @author Yohann.
 */
public class Dispatcher extends Client {

    public static void dispatch(MessageHolder messageHolder) {
        byte sign = messageHolder.getSign();
        if (sign == ProtocolHeader.REQUEST) {
            return;
        }

        switch (sign) {
            case ProtocolHeader.RESPONSE:
                switch (messageHolder.getType()) {
                    case ProtocolHeader.LOGIN:
                        Account aLogin = Serializer.deserialize(messageHolder.getBody(), Account.class);
                        login(messageHolder, aLogin);
                        break;

                    case ProtocolHeader.REGISTER:
                        Account aRegister = Serializer.deserialize(messageHolder.getBody(), Account.class);
                        register(messageHolder, aRegister);
                        break;

                    default:
                        break;
                }
                break;

            case ProtocolHeader.NOTICE:
                break;

            default:
                break;
        }

        // 释放buffer
        ReferenceCountUtil.release(messageHolder);
    }

    /**
     * 登录响应
     *
     * @param messageHolder
     * @param account
     */
    private static void login(MessageHolder messageHolder, Account account) {
        LoginFutureListener listener = Login.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            // 保存用户信息
            mUsername = account.getUsername();
            mToken = account.getToken();
            // 启动心跳
            mChannel.pipeline().addAfter("IdleStateHandler",
                    "HeartbeatHandler", new HeartbeatHandler(new HeartbeatManager()));
            listener.onSuccess(mUsername);
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }

    /**
     * 注册响应
     *
     * @param messageHolder
     * @param account
     */
    private static void register(MessageHolder messageHolder, Account account) {
        RegisterFutureListener listener = Register.future.getListener();
        byte status = messageHolder.getStatus();
        if (status == ProtocolHeader.SUCCESS) {
            listener.onSuccess(account.getUsername());
        } else {
            listener.onFailure(Byte.toUnsignedInt(status));
        }
    }
}
