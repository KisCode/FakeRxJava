package kiscode.fake.rxjava.demo.real.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: 功能函数
 * 1. delay 延迟x秒发射
 * 2. doOnNext/doOnComplete/doOnError/doOnSubscribe 在下游执行之前回调该方法
 * 3. doAfterNext在 下游onNext执行之后回调该方法
 * 4. timeout 上游超过指定时长没有发射，会发送一个错误通知；timeout变体可以向下发射定制内容或切换线程
 * 5. timestamp 它将一个发射T类型数据的Observable转换为一个发射类型为Timestamped<T>的数据的Observable，每一项都包含数据的原始发射时间
 * Author: keno
 * CreateDate: 2021/1/6 22:35
 */

public class RxJavaFunctionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RxJavaFunctionFragment.class.getSimpleName();


    public RxJavaFunctionFragment() {
    }

    public static RxJavaFunctionFragment newInstance() {
        RxJavaFunctionFragment fragment = new RxJavaFunctionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rx_java_function, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_rxjava_delay).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_doOnNext).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_doAfterNext).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_timeout).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_timestamp).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) view).getText() + "-------------------------\n");
        }

        switch (view.getId()) {
            case R.id.btn_rxjava_delay:
                useDelay();
                break;
            case R.id.btn_rxjava_doOnNext:
                useDoOnNext();
                break;
            case R.id.btn_rxjava_doAfterNext:
                useDoAfterNext();
                break;
            case R.id.btn_rxjava_timeout:
                useTimeout();
                break;
            case R.id.btn_rxjava_timestamp:
                useTimeStamp();
                break;
        }
    }

    /***
     * delay 延迟发射
     */
    private void useDelay() {
        Observable.just(1, 2, 3, 4)
                .delay(2, TimeUnit.SECONDS)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept:" + integer);
                    }
                });
    }


    /***
     * doAfterNext在 下游onNext执行之后回调该方法
     */
    private void useDoOnNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Exception {
                //在onSubscribe 之前执行
                Log.i(TAG, "accept: doOnSubscribe");

            }
        }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "doOnNext:" + integer);
            }
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                Log.i(TAG, "doOnComplete");
            }
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                Log.i(TAG, "doOnError:" + throwable);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull Integer value) {
                Log.i(TAG, "onNext:" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError:" + e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });


    }

    private void useDoAfterNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        }).doAfterNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "doAfterNext accept :" + integer);
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull Integer value) {
                Log.i(TAG, "onNext:" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError:" + e);
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }


    /***
     * timeout 上游超过指定时长没有发射，会发送一个错误通知；timeout变体可以向下发射定制内容或切换线程
     */
    private void useTimeout() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 1; i < 10; i++) {
                    int sleepTime = i == 5 ? 5000 : 500;
                    Thread.sleep(sleepTime);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        })/*.timeout(3000, TimeUnit.MILLISECONDS)  //上游如果指定时长没有发射则 抛出异常*/
                .timeout(3000, TimeUnit.MILLISECONDS, new ObservableSource<Integer>() {
                    @Override
                    public void subscribe(@NonNull Observer<? super Integer> observer) {
                        Log.i(TAG, "timeout subscribe");
                        observer.onNext(909);  //
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.i(TAG, "onNext:" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError:" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }


    /***
     * timestamp 它将一个发射T类型数据的Observable转换为一个发射类型为Timestamped<T>的数据的Observable，每一项都包含数据的原始发射时间
     */
    private void useTimeStamp() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 1; i < 100; i++) {
                    Thread.sleep(i);
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }).timestamp()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Timed<Integer>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Timed<Integer> integerTimed) {
                        Log.i(TAG, "onNext:" + "time:" + integerTimed.time() + "\tvalue：" + integerTimed.value());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError:" + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }


}