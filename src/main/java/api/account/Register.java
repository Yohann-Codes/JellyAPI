package api.account;

import common.Constants;
import api.future.Future;
import api.future.ConnectionListener;
import api.future.RegisterFutureListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Account;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;
import transport.Service;

/**
 * 注册API.
 *
 * Future future = Register.register(username, password);
 * future.addListener(new RegisterFutureListener() {
 *     @Override
 *     public void onSuccess(String username) {
 *         // 注册成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 注册失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class Register implements ConnectionListener {
    public static final Future<RegisterFutureListener> future = new Future<>();

    private static String username;
    private static String password;
    private static Channel channel;

    /**
     * 调用此方法注册账户
     *
     * @param username 用户名
     * @param password 密码
     * @return future
     */
    public static Future register(String username, String password) {
        Register.username = username;
        Register.password = password;
        // 异步连接
        new Thread() {
            @Override
            public void run() {
                connect();
            }
        }.start();
        return future;
    }

    private static void connect() {
        ConnectionListener connListener = new Register();
        Service service = new Service(connListener);
        service.connect(Constants.HOST, Constants.PORT);
    }

    @Override
    public void onConnetionComplete(Channel channel) {
        Register.channel = channel;
        doRegister();
    }

    private static void doRegister() {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        sendRequest(Serializer.serialize(account));
    }

    private static void sendRequest(String body) {
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.REGISTER);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(body);
        ChannelFuture future = channel.writeAndFlush(messageHolder);
        future.addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception {
                if (!future.isSuccess()) {
                    channel.writeAndFlush(messageHolder);
                }
            }
        });
    }
}
