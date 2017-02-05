package api.future;

/**
 * @author Yohann.
 */
public interface PersonMessageFutureListener extends FutureListener{
    void onSuccess();

    void onFailure(int errorCode);
}
