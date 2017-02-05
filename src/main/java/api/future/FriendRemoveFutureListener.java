package api.future;

/**
 * @author Yohann.
 */
public interface FriendRemoveFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
