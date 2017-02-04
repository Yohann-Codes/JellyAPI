package api.future;

import io.netty.channel.Channel;

/**
 * @author Yohann.
 */
public interface ConnectionListener {
    /**
     * 连接成功
     * @param channel
     */
    void onConnetionComplete(Channel channel);
}
