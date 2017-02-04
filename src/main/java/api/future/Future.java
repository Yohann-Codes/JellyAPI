package api.future;

/**
 * 异步调用后返回Future，用于注册监听.
 *
 * @author Yohann.
 */
public class Future<T extends FutureListener> {
    private T listener;

    public void addListener(T listener) {
        this.listener = listener;
    }

    public T getListener() {
        return listener;
    }
}
