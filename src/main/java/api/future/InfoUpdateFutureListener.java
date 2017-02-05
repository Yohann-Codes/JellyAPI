package api.future;

/**
 * @author Yohann.
 */
public interface InfoUpdateFutureListener extends FutureListener {
    void onSuccess();

    void onFailure(int errorCode);
}
