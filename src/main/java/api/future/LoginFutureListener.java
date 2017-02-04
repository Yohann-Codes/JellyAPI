package api.future;

/**
 * @author Yohann.
 */
public interface LoginFutureListener extends FutureListener {
    void onSuccess(String username);

    void onFailure(int errorCode);
}
