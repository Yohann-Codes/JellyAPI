package api.future;

/**
 * @author Yohann.
 */
public interface SelfInfoFutureListener extends FutureListener {
    void onSuccess(String username, String name, String sex, String age,
                   String phone, String address, String introduction);

    void onFailure(int errorCode);
}
