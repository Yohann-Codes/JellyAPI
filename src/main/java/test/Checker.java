package test;

/**
 * 检查命令格式.
 *
 * @author Yohann.
 */
public class Checker {

    public static boolean check(int id, int length) {

        switch (id) {
            case 0:
                if (length == 1) {
                    return true;
                }
                break;
            case 1:
                if (length == 3) {
                    return true;
                }
                break;
            case 2:
                if (length == 1) {
                    return true;
                }
                break;
            case 3:
                if (length == 3) {
                    return true;
                }
                break;
        }
        System.out.println("命令格式错误");
        System.out.print(ClientInfo.username + " > ");
        return false;
    }
}
