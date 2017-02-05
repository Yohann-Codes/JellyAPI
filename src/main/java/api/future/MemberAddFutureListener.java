package api.future;

/**
 * @author Yohann.
 */
public interface MemberAddFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
