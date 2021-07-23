package kiscode.fake.rxjava;

/**
 * Description:
 * Author: Keno
 * Date : 2020/12/28 17:13
 **/
public abstract class Observable<T> implements ObservableSourece<T> {

    public static <T> Observable<T> create(ObservableOnSubscribe<T> source) {
//        ObjectHelper.requireNonNull(source, "source is null");
//        return RxJavaPlugins.onAssembly(new ObservableCreate<T>(source));

        return new ObservableCreate<T>(source);
    }

    public static <T> Observable<T> fromArray(T... items) {
        return new ObservableFromArray<>(items);
    }

    public static <T> Observable<T> just(T item) {
        return fromArray(item);
    }
    public static <T> Observable<T> just(T item, T item2) {
        return fromArray(item, item2);
    }
    public static <T> Observable<T> just(T item, T item2, T item3) {
        return fromArray(item, item2, item3);
    }

    public final <R> Observable<R> map(Function<T,R> function){

//        return new ObservableMap<T,R>(this,function);
        return null;
    }

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }


    protected abstract void subscribeActual(Observer<? super T> observer);
}