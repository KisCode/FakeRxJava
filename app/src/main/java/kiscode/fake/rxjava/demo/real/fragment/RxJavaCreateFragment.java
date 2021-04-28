package kiscode.fake.rxjava.demo.real.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava Create操作符使用示例
 * Author: keno
 * CreateDate: 2021/1/2 9:41
 */

public class RxJavaCreateFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RxJavaCreateFragment";

    private CompositeDisposable compositeDisposable;

    public RxJavaCreateFragment() {
    }

    public static RxJavaCreateFragment newInstance() {
        RxJavaCreateFragment fragment = new RxJavaCreateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate");
        compositeDisposable = new CompositeDisposable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rx_java_create, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy");
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
            compositeDisposable = null;
        }
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_rxjava_operate_create).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_just).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_fromArray).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_empty).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_range).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_interval).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_intervalRange).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava_operate_create:
                useObservable();
                break;
            case R.id.btn_rxjava_operate_just:
                useJust();
                break;
            case R.id.btn_rxjava_operate_fromArray:
                useFromArray();
                break;
            case R.id.btn_rxjava_operate_empty:
                useEmpty();
                break;
            case R.id.btn_rxjava_operate_range:
                useRange();
                break;
            case R.id.btn_rxjava_operate_interval:
                useInterval();
                break;
            case R.id.btn_rxjava_operate_intervalRange:
                useIntervalRange();
                break;
            default:
                break;
        }
    }


    private void useObservable() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
               /* emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onError(new Exception("New Custom Exception 。。。。"));
                emitter.onComplete();*/
                for (int i = 100; i < 1000; i++) {
                    emitter.onNext(i);
                    try {
                        Thread.sleep(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe ");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }


    private void useJust() {
        //最大支持10个
        Observable.just(1, 2, 3, 4, 5, 6, 7, 8, 9, 10)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe ");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    private void useFromArray() {
        //最大支持10个
        Observable.fromArray(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe ");
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError: " + e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    private void useEmpty() {
        //empty 传递数据类型只能为Object ,下游onNext不会被执行
        Observable.empty()
                .subscribe(new Observer<Object>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Object o) {
                        //empty 下游onNext()方法不会执行
                        Log.i(TAG, "onNext:" + o);
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

    private void useRange() {
        //从1开始，每次增1，累计20次 以此发射onNext
        Observable.range(1, 20)
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "onNext:" + integer);
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

    private void useInterval() {
        //interval间隔1秒发射一次
        Observable.interval(1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long value) {
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
     * IntervalRange 操作符
     */
    private void useIntervalRange() {
        //IntervalRange: 间隔发射器，可视为Interval的加强版
        //start 开始数值，count累计次数，initDelay 初始等待时间，period 间隔时间点
        Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                        compositeDisposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long value) {
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

}