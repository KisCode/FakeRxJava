package kiscode.fake.rxjava;

/**
 * Description:
 * Author: keno
 * Date : 2021/1/8 8:37
 **/
public class ObservableFromArray<T> extends Observable<T> {
    final T[] array;

    public ObservableFromArray(T[] array) {
        this.array = array;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {
        ObservableOnSubscribe sourece = new ObservableOnSubscribeFromArray(array);
        observer.onSubscribe();

        try {
            sourece.subscribe(observer);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private class ObservableOnSubscribeFromArray<T> implements ObservableOnSubscribe<T> {
        final T[] array;

        private ObservableOnSubscribeFromArray(T[] array) {
            this.array = array;
        }

        @Override
        public void subscribe(Observer<? super T> emitter) throws Exception {
            run(emitter);
        }

        void run(Observer<? super T> emitter) {
            T[] a = array;
            int n = a.length;

            for (int i = 0; i < n; i++) {
                T value = a[i];
                if (value == null) {
                    emitter.onError(new NullPointerException("The " + i + "th element is null"));
                    return;
                }
                emitter.onNext(value);
            }
            emitter.onComplete();
        }
    }
}