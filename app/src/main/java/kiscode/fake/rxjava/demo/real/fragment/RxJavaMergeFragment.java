package kiscode.fake.rxjava.demo.real.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava 合并操作符使用示例
 * 上游(两个或多个被观察者Observable合并为一个Observable) --> 条件操作符 -->下游
 * 1. startWith/concatWith/mergeWith :先创建被观察者，然后再组合其他的被观察者，最后再订阅
 * 2. concat/merge/zip: 直接合并多个被观察者，然后订阅
 * a. startWith 先执行startWith括号内的观察者
 * b. concatWith 后执行concatWith括号内的观察者
 * c. concat 按照顺序执行
 * d. merge并列执行
 * e. zip zip 将多个Observable的发射数据依次结合到一起（成为新的数据类型）再发射，只发射和数据量最少的那个Observable一样多的数据
 * Author: keno
 * CreateDate: 2021/1/3 08:30
 */
public class RxJavaMergeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = RxJavaMergeFragment.class.getSimpleName();

    public RxJavaMergeFragment() {
    }

    public static RxJavaMergeFragment newInstance() {
        RxJavaMergeFragment fragment = new RxJavaMergeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rx_java_merge, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
//        filter、take、distinct、skip、elementAt、ignoreElement、first、last、sample
        view.findViewById(R.id.btn_rxjava_operate_startWith).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_concatWith).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_concat).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_merge).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_mergeWith).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_zip).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_switch).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) v).getText() + "-------------------------\n");
        }

        switch (v.getId()) {
            case R.id.btn_rxjava_operate_startWith:
                useStartWith();
                break;
            case R.id.btn_rxjava_operate_concatWith:
                useContactWith();
                break;
            case R.id.btn_rxjava_operate_concat:
                useContact();
                break;
            case R.id.btn_rxjava_operate_merge:
                useMerge();
                break;
            case R.id.btn_rxjava_operate_mergeWith:
                useMergeWith();
                break;
            case R.id.btn_rxjava_operate_zip:
                useZip();
                break;
            case R.id.btn_rxjava_operate_switch:
                useSwitch();
                break;

        }
    }

    /***
     * startWith 操作符实例：
     * 合并多个Observable，将startWith的Observable插在原始Observable发射的数据序列之前
     * 如：observable1.startWith(observable2) 会线执行observable2，再执行observable1的事件
     */
    private void useStartWith() {
        Observable.just(1, 2, 3, 4, 5)
                .startWith(Observable.just(100, 200, 300)) //合并操作符，会优先执行
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept:" + integer);
                    }
                });
    }


    /***
     * concatWith 操作符实例：与startWith执行顺序相反
     * 合并多个Observable，将concatWith的Observable插在原始Observable发射的数据序列之后
     * 如：observable1.concatWith(observable2) 会线执行observable1，再执行observable2的事件
     */
    private void useContactWith() {
        Observable.just(1, 2, 3, 4, 5)
                .concatWith(Observable.just(100, 200, 300))
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "accept:" + integer);
                    }
                });
    }

    /***
     * concat 连接操作符，最多可支持连接4个Observable,各个Observable按顺序执行
     */
    private void useContact() {
/*        Observable.concat(
                Observable.just(1, 2, 3),
                Observable.just(10, 20, 30),
                Observable.just(100, 200, 300),
                Observable.just(1000, 2000, 3000)
        ).subscribe(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Exception {
                Log.i(TAG, "accept:" + integer);
            }
        });*/

        Observable.concat(
                Observable.just(1, 2, 3),
                Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(90);
                        emitter.onComplete();
                    }
                }),
                Observable.just(100, 200, 300)
        ).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull Integer integer) {
                Log.i(TAG, "onNext:" + integer);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    /***
     * merge操作符:最多可支持合并4个Observable,各个Observable并列执行发射
     */
    private void useMerge() {
        //intervalRange: 间隔发射器，可视为Interval的加强版
        //start 开始数值，count累计次数，initDelay 初始等待时间，period 间隔时间点
        Observable observable1 = Observable.intervalRange(1, 5, 1, 1, TimeUnit.SECONDS);
        Observable observable2 = Observable.intervalRange(10, 5, 1, 1, TimeUnit.SECONDS);
        Observable observable3 = Observable.intervalRange(100, 5, 1, 1, TimeUnit.SECONDS);
        Observable observable4 = Observable.intervalRange(1000, 5, 1, 1, TimeUnit.SECONDS);

        Observable.merge(observable1, observable2, observable3, observable4)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.i(TAG, "onNext:" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }


    /***
     * mergeWith操作符：
     */
    private void useMergeWith() {

        //intervalRange: 间隔发射器
        //start 开始数值，count累计次数，initDelay 初始等待时间，period 间隔时间点
        Observable observable1 = Observable.intervalRange(1, 10, 0, 500, TimeUnit.MILLISECONDS);
        Observable observable2 = Observable.intervalRange(10000, 5, 1, 1, TimeUnit.SECONDS);

        observable1
                .mergeWith(observable2)
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.i(TAG, "onNext:" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError");
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }


    /***
     * zip合并操作符
     * zip 将多个Observable的发射数据依次结合到一起（成为新的数据类型）再发射，只发射和数据量最少的那个Observable一样多的数据
     */
    private void useZip() {
        Observable observable1 = Observable.just(1000, 2000, 3000);
        Observable observable2 = Observable.just("RedMi", "Xiaomi", "Huawei");

        Observable.zip(observable1, observable2, new BiFunction<Integer, String, StringBuilder>() {
            @NonNull
            @Override
            public StringBuilder apply(@NonNull Integer integer, @NonNull String s) throws Exception {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("brand:").append(s).append("\tprice:").append(integer);
                return stringBuilder;
            }
        }).subscribe(new Observer<StringBuilder>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe");
            }

            @Override
            public void onNext(@NonNull StringBuilder value) {
                Log.i(TAG, "onNext:" + value);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete");
            }
        });
    }

    private void useSwitch() {
//        Observable observable1 = Observable.intervalRange(1, 10, 0, 500, TimeUnit.MILLISECONDS);
        Observable observable1 = Observable.empty();
        Observable observable2 = Observable.intervalRange(10000, 5, 1, 1, TimeUnit.SECONDS);
        observable2.switchIfEmpty(observable1)
                .subscribe(new Consumer() {
                    @Override
                    public void accept(Object o) throws Exception {
                        Log.i(TAG, "accept:" + o);
                    }
                });
    }
}