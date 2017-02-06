# JellyAPI
**为了测试API，于是写了一个简单的基于命令行操作的客户端（test包）。**
#### 除了登出接口，其它接口都是异步调用，仿照Netty的Future使用模式。
**比如登录API**

```
public class MyReceiver implements Receiver {                      
    //...                                                          
}                                                                  
                                                                   
Future future = Login.login(new MyReceiver(), username, password); 
future.addListener(new LoginFutureListener() {                     
    @Override                                                      
    public void onSuccess(String username, Long token) {           
        // 登录成功                                                    
    }                                                              
                                                                   
    @Override                                                      
    public void onFailure(int errorCode) {                         
        // 登录失败                                                    
    }                                                              
});                                                                
```

**客户端接收到响应包后，根据Status来决定调用onSuccess()或onFailure()， 除此之外客户端还需要实现Receiver接口来接收消息等。**

```
public interface Receiver {

    /**
     * 重写此方法接收系统消息 e.g.退出登录...
     *
     * @param notice 通知内容
     */
    void systemNotice(String notice);

    /**
     * 重写此方法接收断线重连结果
     *
     * @param isSuccess 重连成功/失败
     */
    void reconnection(boolean isSuccess);

    /**
     * 重写此方法接收个人消息
     *
     * @param sender 发送者用户名
     * @param content 消息内容
     * @param time 发送时间
     */
    void personMessage(String sender, String content, Long time);

    /**
     * 重写此方法接收讨论组消息
     *
     * @param groupName 讨论组名称
     * @param sender 发送者用户名
     * @param content 消息内容
     * @param time 发送时间
     */
    void groupMessage(String groupName, String sender, String content, Long time);
}
```