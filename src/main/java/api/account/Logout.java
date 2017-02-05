package api.account;

import common.Client;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import pojo.Account;
import serialize.Serializer;
import transport.MessageHolder;
import transport.ProtocolHeader;

/**
 * 登出API.
 *
 * Logout.logout();
 *
 * @author Yohann.
 */
public class Logout extends Client {

    public static void logout() {
        isLogout = true;
        doLogout();
    }

    /**
     * 调用此方法登出账户
     */
    private static void doLogout() {
        Account account = new Account();
        account.setUsername(mUsername);
        account.setToken(mToken);
        MessageHolder messageHolder = new MessageHolder();
        messageHolder.setSign(ProtocolHeader.REQUEST);
        messageHolder.setType(ProtocolHeader.LOGOUT);
        messageHolder.setStatus((byte) 0);
        messageHolder.setBody(Serializer.serialize(account));
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
