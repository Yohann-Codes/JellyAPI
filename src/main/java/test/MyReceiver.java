package test;

import api.future.Receiver;

/**
 * @author Yohann.
 */
public class MyReceiver implements Receiver {
    @Override
    public void systemNotice(String notice) {
        System.out.println(notice);
        System.out.print(ClientInfo.username + " > ");
    }
}
