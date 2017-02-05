package api.future;

/**
 * @author Yohann.
 */
public interface MemberRemoveFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
