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

    @Override
    public void subscribe(Observer<? super T> observer) {
        subscribeActual(observer);
    }


    protected abstract void subscribeActual(Observer<? super T> observer);
}