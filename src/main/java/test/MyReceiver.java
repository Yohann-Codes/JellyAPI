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
    public void personMessage(String sender, String content, Long time) {
        String t = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(time));
        System.out.println(System.getProperty("line.separator") + sender + ": " + content + "   [" + t + "]");
        System.out.print(ClientInfo.username + " > ");
    }
}
