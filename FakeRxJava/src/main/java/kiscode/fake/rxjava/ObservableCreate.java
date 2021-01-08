package kiscode.fake.rxjava;

/**
 * Description:
 * Author: KENO
 * Date : 2020/12/28 17:16
 **/
public class ObservableCreate<T> extends Observable<T> {
    private final ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        observer.onSubscribe();
        try {
            //回调绑定
            source.subscribe(observer);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }
}