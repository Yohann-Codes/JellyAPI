package test;

import java.util.HashMap;
import java.util.Map;

/**
 * 命令行.
 *
 * @author Yohann.
 */
public class CommandLine {

    // 命令容器
    private Map<String, Integer> commands;

    // 选项容器
    private Map<String, Integer> messageOptions;
    private Map<String, Integer> groupOptions;
    private Map<String, Integer> addOptions;
    private Map<String, Integer> removeOptions;
    private Map<String, Integer> upadteOptions;
    private Map<String, Integer> showOptions;
    private Map<String, Integer> displayOptions;

    public CommandLine() {
        commands = new HashMap<>();
        messageOptions = new HashMap<>();
        groupOptions = new HashMap<>();
        addOptions = new HashMap<>();
        removeOptions = new HashMap<>();
        upadteOptions = new HashMap<>();
        showOptions = new HashMap<>();
        displayOptions = new HashMap<>();
    }

    public void init() {
        initCommand();
        initOptions();
    }

    /**
     * 初始化命令
     */
    private void initCommand() {
        commands.put("help", 0);           // 帮助        help
        commands.put("login", 1);          // 登录        login <username> <password>
        commands.put("logout", 2);         // 登出        logout
        commands.put("register", 3);       // 注册        register <username> <password>
        commands.put("message", 4);        // 发送消息     message [-p/-g] <receiver> <content>
        commands.put("group", 5);          // 讨论组       group [-c/-d] <groupName>
        commands.put("add", 6);            // 添加        add [-f/-m] <groupName> <username>
        commands.put("remove", 7);         // 删除        remove [-f/-m] <groupName> <username>
        commands.put("update", 8);         // 修改信息     update [-p/-n/-s/-a/-P/-A/-i] <content>
        commands.put("show", 9);           // 查看信息     show [-m/-f] <friend>
        commands.put("display", 10);       // 列出        display [-f/-g]
    }

    /**
     * 初始化选项
     */
    private void initOptions() {
        // messsage
        messageOptions.put("-p", 1); // 个人消息
        messageOptions.put("-g", 2); // 讨论组消息
        // group
        groupOptions.put("-c", 1); // 创建
        groupOptions.put("-d", 2); // 解散
        // add
        addOptions.put("-f", 1); // 添加好友
        addOptions.put("-m", 2); // 添加讨论组成员
        // remove
        removeOptions.put("-f", 1); // 删除好友
        removeOptions.put("-m", 2); // 删除讨论组成员
        // update
        upadteOptions.put("-p", 1); // 修改密码
        upadteOptions.put("-n", 2); // 修改姓名
        upadteOptions.put("-s", 3); // 修改性别
        upadteOptions.put("-a", 4); // 修改年龄
        upadteOptions.put("-P", 5); // 修改联系方式
        upadteOptions.put("-A", 6); // 修改地址
        upadteOptions.put("-i", 7); // 修改自我介绍
        // show
        showOptions.put("-m", 1); // 查看自己的信息
        showOptions.put("-f", 2); // 查看好友的信息
        // display
        displayOptions.put("-f", 1); // 已添加的好友
        displayOptions.put("-g", 2); // 已加入的讨论组
    }

    public Map<String, Integer> getCommands() {
        return commands;
    }

    public Map<String, Integer> getMessageOptions() {
        return messageOptions;
    }

    public Map<String, Integer> getGroupOptions() {
        return groupOptions;
    }

    public Map<String, Integer> getAddOptions() {
        return addOptions;
    }

    public Map<String, Integer> getRemoveOptions() {
        return removeOptions;
    }

    public Map<String, Integer> getUpadteOptions() {
        return upadteOptions;
    }

    public Map<String, Integer> getShowOptions() {
        return showOptions;
    }

    public Map<String, Integer> getDisplayOptions() {
        return displayOptions;
    }
}
