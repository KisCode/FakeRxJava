package kiscode.fake.rxjava;

/**
 * Description:
 * Author: KENO
 * Date : 2020/12/28 17:12
 **/
public interface ObservableSourece<T> {
    void subscribe(Observer<? super T> observer);
}

