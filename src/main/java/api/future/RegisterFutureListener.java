package api.future;

/**
 * @author Yohann.
 */
public interface RegisterFutureListener extends FutureListener {
    void onSuccess(String username);

    void onFailure(int errorCode);
}
