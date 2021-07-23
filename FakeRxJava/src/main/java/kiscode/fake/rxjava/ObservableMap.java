package kiscode.fake.rxjava;

/**
 * Description:
 * Author: KrisKeno
 * Date : 2021/1/9 9:19 PM
 **/
public class ObservableMap<T,R> extends Observable<T>{
    private final Function<T,R> mapper;


    public ObservableMap(ObservableSourece<T> source,Function<T, R> mapper) {
        this.mapper = mapper;
//        this.source = source;
    }

    @Override
    protected void subscribeActual(Observer<? super T> observer) {

    }
}
