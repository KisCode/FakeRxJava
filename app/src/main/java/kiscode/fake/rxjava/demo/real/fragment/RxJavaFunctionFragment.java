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
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: 功能函数
 *  1. delay 延迟x秒发射
 *  2. doOnNext/doOnComplete/doOnError 在下游执行之前回调该方法
 *  3. doAfterNext在 下游onNext执行之后回调该方法
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
                Log.i(TAG,"doAfterNext accept :"+integer);
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

}