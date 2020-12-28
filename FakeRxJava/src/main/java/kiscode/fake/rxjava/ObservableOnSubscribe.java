package kiscode.fake.rxjava;

public interface ObservableOnSubscribe<T> {

    /**
     * Called for each Observer that subscribes.
     * @param emitter the safe emitter instance, never null
     * @throws Exception on error
     */
    void subscribe(ObservableEmitter<T> emitter) throws Exception;
}