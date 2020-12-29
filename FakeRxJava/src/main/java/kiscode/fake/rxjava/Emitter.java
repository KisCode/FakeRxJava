package kiscode.fake.rxjava;

/**
 * Description:
 * Author: keno
 * Date : 2020/12/28 17:32
 **/
public interface Emitter<T> {
    /**
     * Signal a normal value.
     * @param value the value to signal, not null
     */
    void onNext(T value);

    /**
     * Signal a Throwable exception.
     * @param error the Throwable to signal, not null
     */
    void onError(Throwable error);

    /**
     * Signal a completion.
     */
    void onComplete();

} 