package test;

import api.account.Login;
import api.account.Logout;
import api.account.Register;
import api.future.RegisterFutureListener;
import com.sun.deploy.util.StringUtils;
import api.future.Future;
import api.future.LoginFutureListener;

import java.util.Scanner;

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
        System.out.println("=========== 客户端启动成功 ===========");
        System.out.print(ClientInfo.username + " > ");
        Scanner sc = new Scanner(System.in);
        while (true) {
            if (sc.hasNextLine()) {
                String line = StringUtils.trimWhitespace(sc.nextLine());
                String[] keywords = line.split(" ");
                int id;
                try {
                    id = commandLine.getCommands().get(keywords[0]);
                } catch (Exception e) {
                    System.out.println("不支持此命令");
                    System.out.print(ClientInfo.username + " > ");
                    continue;
                }
                switch (id) {

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
                }
            }
        }
    }

    private static void help() {
        System.out.println("1. help  命令帮助");
        System.out.println("2. login <username> <password>  登录");
        System.out.println("3. register <username> <password>  注册");
        System.out.print(ClientInfo.username + " > ");
    }

    private static void login(String username, String password) {
        Future future = Login.login(new MyReceiver(), username, password);
        future.addListener(new LoginFutureListener() {
            @Override
            public void onSuccess(String username) {
                // 保存信息
                ClientInfo.username = username;
                System.out.println("登录成功, username=" + username);
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("error: " + errorCode);
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
                System.out.println("注册成功, username=" + username);
                System.out.print(ClientInfo.username + " > ");
            }

            @Override
            public void onFailure(int errorCode) {
                System.out.println("error: " + errorCode);
                System.out.print(ClientInfo.username + " > ");
            }
        });
    }
}
