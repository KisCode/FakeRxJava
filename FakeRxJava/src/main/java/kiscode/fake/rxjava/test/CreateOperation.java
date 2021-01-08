package kiscode.fake.rxjava.test;

import kiscode.fake.rxjava.Observable;
import kiscode.fake.rxjava.ObservableOnSubscribe;
import kiscode.fake.rxjava.Observer;

/**
 * Description:
 * Author: Keno
 * Date : 2020/12/28 17:26
 **/
public class CreateOperation {
    private static final String TAG = CreateOperation.class.getSimpleName();

    public static void main(String[] args) {
//        testCreateOperation();
//        testFromArrayOperation();
        testJustOperation();
    }

    private static void testCreateOperation() {

        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(Observer<? super Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new Exception("Exception 。。。。"));
                emitter.onComplete();
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe() {
                System.out.println("onSubscribe");
            }

            @Override
            public void onNext(Integer integer) {
                System.out.println("onNext:" + integer);
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("onError:" + e);
            }

            @Override
            public void onComplete() {
                System.out.println("onComplete");
            }
        });
    }

    private static void testFromArrayOperation() {
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe() {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(Integer integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError:" + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }

    private static void testJustOperation() {
        Observable.just("Java", "Swift", "Kotlin")
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe() {
                        System.out.println("onSubscribe");
                    }

                    @Override
                    public void onNext(String integer) {
                        System.out.println("onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("onError:" + e);
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("onComplete");
                    }
                });
    }
} 