package api.future;

/**
 * @author Yohann.
 */
public interface GroupMessageFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
