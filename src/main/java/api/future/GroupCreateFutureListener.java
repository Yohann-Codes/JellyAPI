package api.future;

/**
 * @author Yohann.
 */
public interface GroupCreateFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
