package test;

import api.account.Login;
import api.account.Logout;
import api.account.Register;
import api.friend.AllFriend;
import api.friend.FriendAdd;
import api.friend.FriendRemove;
import api.future.*;
import api.info.FriendInfo;
import api.info.InfoUpdate;
import api.info.SelfInfo;
import api.message.PersonMessage;
import com.sun.deploy.util.StringUtils;
import pojo.Friend;

import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

/**
 * 启动命令行.
 *
 * @author Yohann.
 */
public class ClientStrap {
    private CommandLine commandLine;

    public ClientStrap() {
        commandLine = new CommandLine();
        // 初始化命令
        commandLine.init();
        //
    }

    /**
     * 启动
     */
    public void startCommandLine() {
        int commandId, optionId;
        System.out.println("=========== 客户端启动成功 ===========");
        System.out.print(ClientInfo.username + " > ");
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.hasNextLine()) {
                String line = StringUtils.trimWhitespace(sc.nextLine());
                String[] keywords = line.split(" ");
                try {
                    commandId = commandLine.getCommands().get(keywords[0]);
                } catch (Exception e) {
                    System.out.println("不支持此命令");
                    System.out.print(ClientInfo.username + " > ");
                    continue;
                }
                switch (commandId) {

                    case 0:
                        if (Checker.check(0, keywords.length)) {
                            help();
                        }
                        break;

                    case 1:
                        if (Checker.check(1, keywords.length)) {
                            login(keywords[1], keywords[2]);
                        }
                        break;

                    case 2:
                        if (Checker.check(2, keywords.length)) {
                            logout();
                        }
                        break;

                    case 3:
                        if (Checker.check(3, keywords.length)) {
                            register(keywords[1], keywords[2]);
                        }
                        break;

                    case 4:
                        if (Checker.check(4, keywords.length)) {
                            try {
                                optionId = commandLine.getMessageOptions().get(keywords[1]);
                            } catch (Exception e) {
                                System.out.println("不支持此选项");
                                System.out.print(ClientInfo.username + " > ");
                                continue;
                            }
                            switch (optionId) {
                                case 1:
                                    personMessage(keywords[2], keywords[3]);
                                    break;
                                case 2:
                                    break;
                            }
                        }
                        break;

                    case 5:
                        break;

                    case 6:
                        if (Checker.check(6, keywords.length)) {
                            try {
                                optionId = commandLine.getAddOptions().get(keywords[1]);
                            } catch (Exception e) {
                                System.out.println("不支持此选项");
                                System.out.print(ClientInfo.username + " > ");
                                continue;
                            }
                            switch (optionId) {
                                case 1:
                                    addFriend(keywords[2]);
                                    break;
                                case 2:
                                    break;
                            }
                        }
                        break;

                    case 7:
                        if (Checker.check(7, keywords.length)) {
                            try {
                                optionId = commandLine.getRemoveOptions().get(keywords[1]);
                            } catch (Exception e) {
                                System.out.println("不支持此选项");
                                System.out.print(ClientInfo.username + " > ");
                                continue;
                            }
                            switch (optionId) {
                                case 1:
                                    removeFriend(keywords[2]);
                                    break;
                                case 2:
                                    break;
                            }
                        }
                        break;

                    case 8:
                        if (Checker.check(8, keywords.length)) {
                            try {
                                optionId = commandLine.getUpadteOptions().get(keywords[1]);
                            } catch (Exception e) {
                                System.out.println("不支持此选项");
                                System.out.print(ClientInfo.username + " > ");
                                continue;
                            }
                            switch (optionId) {
                                case 1:
                                    updatePassword(keywords[2]);
                                    break;
                                case 2:
                                    updateName(keywords[2]);
                                    break;
                                case 3:
                                    updateSex(keywords[2]);
                                    break;
                                case 4:
                                    updateAge(keywords[2]);
                                    break;
                                case 5:
                                    updatePhone(keywords[2]);
                                    break;
                                case 6:
                                    updateAddress(keywords[2]);
                                    break;
                                case 7:
                                    updateIntroduction(keywords[2]);
                                    break;
                            }
                        }
                        break;

                    case 9:
                        if (Checker.check(9, keywords.length)) {
                            try {
                                optionId = commandLine.getShowOptions().get(keywords[1]);
                            } catch (Exception e) {
                                System.out.println("不支持此选项");
                                System.out.print(ClientInfo.username + " > ");
                                continue;
                            }
                            switch (optionId) {
                                case 1:
                                    showSelf();
                                    break;
                                case 2:
                                    showFriend(keywords[2]);
                                    break;
                            }
                        }
                        break;

                    case 10:
                        if (Checker.check(10, keywords.length)) {
                            try {
                                optionId = commandLine.getDisplayOptions().get(keywords[1]);
                            } catch (Exception e) {
                                System.out.println("不支持此选项");
                                System.out.print(ClientInfo.username + " > ");
                                continue;
                            }
                            switch (optionId) {
                                case 1:
                                    displayFriend();
                                    break;
                                case 2:
                                    break;
                            }
                        }
                        break;
                }
            }
        }
    }

    private static void help() {
        System.out.println("1. help  命令帮助");
        System.out.println("2. login <username> <password>  登录");
        System.out.println("3. register <username> <password>  注册");
        System.out.println("4. message");
        System.out.println("   message [-p] <receiver> <content>  发送个人消息");
        System.out.println("   message [-g] <receiver> <content>  发送个人消息");
        System.out.print(ClientInfo.username + " > ");
    }

    private static void login(String username, String password) {
        Future future = Login.login(new MyReceiver(), username, password);
        future.addListener(new LoginFutureListener() {
            @Override
            public void onSuccess(String username) {
                // 保存信息
                ClientInfo.username = username;
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("登录失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private static void logout() {
        Logout.logout();
        ClientInfo.username = "";
    }

    private static void register(String username, String password) {
        Future future = Register.register(username, password);
        future.addListener(new RegisterFutureListener() {
            @Override
            public void onSuccess(String username) {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("注册失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private static void personMessage(String receiver, String content) {
        Future future = PersonMessage.send(receiver, content);
        future.addListener(new PersonMessageFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("消息发送失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void addFriend(String friend) {
        Future future = FriendAdd.add(friend);
        future.addListener(new FriendAddFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void removeFriend(String friend) {
        Future future = FriendRemove.remove(friend);
        future.addListener(new FriendRemoveFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void displayFriend() {
        Future future = AllFriend.query();
        future.addListener(new AllFriendFutureListener() {
            @Override
            public void onSuccess(Map<String, Boolean> friends) {
                Set<Map.Entry<String, Boolean>> entries = friends.entrySet();
                Iterator<Map.Entry<String, Boolean>> ite = entries.iterator();
                while (ite.hasNext()) {
                    Map.Entry<String, Boolean> entry = ite.next();
                    String username = entry.getKey();
                    Boolean isOnline = entry.getValue();
                    if (isOnline) {
                        System.out.println(username + "  在线");
                    } else {
                        System.out.println(username + "  离线");
                    }
                }
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updatePassword(String password) {
        Future future = InfoUpdate.setPassword(password);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updateName(String name) {
        Future future = InfoUpdate.setName(name);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updateSex(String sex) {
        Future future = InfoUpdate.setSex(sex);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updateAge(String age) {
        Future future = InfoUpdate.setAge(age);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updatePhone(String phone) {
        Future future = InfoUpdate.setPhone(phone);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updateAddress(String address) {
        Future future = InfoUpdate.setAddress(address);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void updateIntroduction(String introduction) {
        Future future = InfoUpdate.setIntroduction(introduction);
        future.addListener(new InfoUpdateFutureListener() {
            @Override
            public void onSuccess() {
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("添加好友失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void showSelf() {
        Future future = SelfInfo.query();
        future.addListener(new SelfInfoFutureListener() {
            @Override
            public void onSuccess(String username, String name, String sex,
                                  String age, String phone, String address, String introduction) {
                System.out.println("用户名：" + username);
                System.out.println("姓名：" + name);
                System.out.println("性别：" + sex);
                System.out.println("年龄：" + age);
                System.out.println("联系电话：" + phone);
                System.out.println("家庭住址：" + address);
                System.out.println("个人介绍：" + introduction);
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("信息查询失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }

    private void showFriend(String friend) {
        Future future = FriendInfo.query(friend);
        future.addListener(new FriendInfoFutureListener() {
            @Override
            public void onSuccess(String username, String name, String sex,
                                  String age, String phone, String address, String introduction) {
                System.out.println("用户名：" + username);
                System.out.println("姓名：" + name);
                System.out.println("性别：" + sex);
                System.out.println("年龄：" + age);
                System.out.println("联系电话：" + phone);
                System.out.println("家庭住址：" + address);
                System.out.println("个人介绍：" + introduction);
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("信息查询失败 errorCode: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }
}
