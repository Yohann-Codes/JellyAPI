package api.account;

import api.future.Receiver;
import common.Constants;
import common.Client;
import api.future.Future;
import api.future.LoginFutureListener;
import api.future.ConnectionListener;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Account;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;
import transport.Service;

/**
 * 登录API.
 *
 * public class MyReceiver implements Receiver {
 *     //...
 * }
 *
 * Future future = Login.login(new MyReceiver(), username, password);
 * future.addListener(new LoginFutureListener() {
 *     @Override
 *     public void onSuccess(String username, Long token) {
 *         // 登录成功
 *     }
 *
 *     @Override
 *     public void onFailure(int errorCode) {
 *         // 登录失败
 *     }
 * });
 *
 * @author Yohann.
 */
public class Login extends Client implements ConnectionListener {
    public static final Future<LoginFutureListener> future = new Future<>();

    private static String username;
    private static String password;
    private static Long token;

    /**
     * 调用此方法登录账户
     *
     * @param username 用户名
     * @param password 密码
     * @return future
     */
    public static Future login(Receiver receiver, String username, String password) {
        isLogout = false;
        Login.username = username;
        Login.password = password;
        mReceiver = receiver;

        // 异步连接
        new Thread() {
            @Override
            public void run() {
                connect();
            }
        }.start();
        return future;
    }

    /**
     * 重连
     *
     * @param token
     * @return
     */
    public static Future login(Long token) {
        isLogout = false;
        Login.token = token;
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
        ConnectionListener connListener = new Login();
        Service service = new Service(connListener);
        service.connect(Constants.HOST, Constants.PORT);
    }

    @Override
    public void onConnetionComplete(Channel channel) {
        // 保存channel
        mChannel = channel;

        if (token == null) {
            // 登录
            doLogin();
        } else {
            // 重连
            reconnect();
        }
    }

    private static void doLogin() {
        Account account = new Account();
        account.setUsername(username);
        account.setPassword(password);
        sendRequest(Serializer.serialize(account), ProtocolHeader.LOGIN);
    }

    private static void reconnect() {
        Account account = new Account();
        account.setUsername(username);
        account.setToken(token);
        sendRequest(Serializer.serialize(account), ProtocolHeader.RECONN);
    }

    private static void sendRequest(String body, byte type) {
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(type);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(body);
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
