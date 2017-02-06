package api.future;

import java.util.List;
import java.util.Map;

/**
 * @author Yohann.
 */
public interface InfoGroupFutureListener extends FutureListener {
    void onSuccess(Map<String, List<String>> groups);

    void onFailure(int errorCode);
}
