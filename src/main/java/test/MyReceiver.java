package test;

import api.future.Receiver;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Yohann.
 */
public class MyReceiver implements Receiver {
    @Override
    public void systemNotice(String notice) {
        System.out.println(System.getProperty("line.separator") + notice);
        System.out.print(ClientInfo.username + " > ");
    }

    @Override
    public void reconnection(boolean isSuccess) {
        if (isSuccess) {
            System.out.println(System.getProperty("line.separator") + "重新连接成功");
            System.out.print(ClientInfo.username + " > ");
        } else {
            System.out.println(System.getProperty("line.separator") + "重新连接失败, 请重新登录");
            System.out.print(ClientInfo.username + " > ");
        }
    }

    @Override
    public void personMessage(String sender, String content, Long time) {
        String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        System.out.println(System.getProperty("line.separator") + sender + ": " + content + "   [" + t + "]");
        System.out.print(ClientInfo.username + " > ");
    }

    @Override
    public void groupMessage(String groupName, String sender, String content, Long time) {
        String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        System.out.println(System.getProperty("line.separator") + "<" + groupName + "> " + sender + ": "
                + content + "   [" + t + "]");
        System.out.print(ClientInfo.username + " > ");
    }
}
