package api.future;

import java.util.Map;

/**
 * @author Yohann.
 */
public interface AllFriendFutureListener extends FutureListener {
    void onSuccess(Map<String, Boolean> friends);

    void onFailure(int errorCode);
}
