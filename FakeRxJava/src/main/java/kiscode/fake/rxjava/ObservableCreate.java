package kiscode.fake.rxjava;

/**
 * Description:
 * Author: KENO
 * Date : 2020/12/28 17:16
 **/
public class ObservableCreate<T> extends Observable<T> {
    private ObservableOnSubscribe<T> source;

    public ObservableCreate(ObservableOnSubscribe<T> source) {
        this.source = source;
    }

    @Override
    public void subscribe(Observer<? super T> observer) {
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        CreateEmitter<T> createEmitter = new CreateEmitter<>(observer);
//        observer.onSubscribe(createEmitter);
        try {
            source.subscribe(createEmitter);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    static final class CreateEmitter<T> implements ObservableEmitter<T> {
        Observer<? super T> observer;

        public CreateEmitter(Observer<? super T> observer) {
            this.observer = observer;
        }

        @Override
        public void onNext(T value) {
            observer.onNext(value);
        }

        @Override
        public void onError(Throwable error) {
            observer.onError(error);
        }

        @Override
        public void onComplete() {
            observer.onComplete();

        }
    }
}