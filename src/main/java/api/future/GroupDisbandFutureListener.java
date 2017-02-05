package api.future;

/**
 * @author Yohann.
 */
public interface GroupDisbandFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
