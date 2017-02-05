package api.future;

/**
 * @author Yohann.
 */
public interface FriendAddFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
