package kiscode.fake.rxjava.demo.real.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.CompletableObserver;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.observables.GroupedObservable;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava 过滤操作符使用示例
 * 上游--> 过滤操作符 -->下游
 * filter、take、distinct、skip、elementAt、ignoreElement、first、last、sample
 * Author: keno
 * CreateDate: 2021/1/2 9:42
 */
public class RxJavaFilterFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RxJavaFilterFragment";

    public RxJavaFilterFragment() {
    }

    public static RxJavaFilterFragment newInstance() {
        RxJavaFilterFragment fragment = new RxJavaFilterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rx_java_filter, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
//        filter、take、distinct、skip、elementAt、ignoreElement、first、last、sample
        view.findViewById(R.id.btn_rxjava_operate_filter).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_take).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_distinct).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_skip).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_elementAt).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_ignoreElement).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_first).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_last).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_sample).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) v).getText() + "-------------------------\n");
        }

        switch (v.getId()) {
            case R.id.btn_rxjava_operate_filter:
                useFilter();
                break;
            case R.id.btn_rxjava_operate_take:
                useTake();
                break;
            case R.id.btn_rxjava_operate_distinct:
                useDistinct();
                break;
            case R.id.btn_rxjava_operate_skip:
                useSkip();
                break;
            case R.id.btn_rxjava_operate_elementAt:
                useElementAt();
                break;
            case R.id.btn_rxjava_operate_ignoreElement:
                useIgnoreElement();
                break;
            case R.id.btn_rxjava_operate_first:
                useFirst();
                break;
            case R.id.btn_rxjava_operate_last:
                useLast();
                break;
            case R.id.btn_rxjava_operate_sample:
                useSample();
                break;
        }
    }

    /***
     *  map操作符实例：
     *  把上游Int 变换为String 观察者String类型
     */
    private void useFilter() {
        //上游
        Observable.just(1, 2, 3, 4, 5)
                .filter(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        //仅return true成功到下游，如果return false则被过滤掉无法到下游
                        return integer % 2 == 0;
                    }
                })
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.i(TAG, "filter value:" + integer);
                    }
                });
    }


    private void useTake() {
        //每隔1s发射一次
        Observable.interval(1, TimeUnit.SECONDS)
                .take(5) //只返回前面指定项（5）数据并完成事件，后面忽略
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "useTake: onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long value) {
                        Log.i(TAG, "value:" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "useTake: onComplete");
                    }
                });
    }


    /***
     * distinct 去重
     */
    private void useDistinct() {
        //每隔1s发射一次
        Observable.fromArray(1, 2, 2, 3, 1, 3, 5, 4, 4, 5, 6)
                .distinct() //去重
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "distinct: onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.i(TAG, "value:" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "distinct: onComplete");
                    }
                });
    }


    /***
     * distinct 去重
     */
    private void useSkip() {
        //每隔1s发射一次
        Observable.fromArray(1, 2, 3, 4, 5, 6)
                .skip(3)  //跳过(忽略)前3项数据，只保留之后的数据
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "skip: onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Integer value) {
                        Log.i(TAG, "value:" + value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "skip: onComplete");
                    }
                });
    }


    private void useElementAt() {
        Observable.fromArray(1, 2, 3, 4, 5, 6)
                .elementAt(3)  //只发射第N项数据,index =3
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer value) throws Exception {
                        Log.i(TAG, "value:" + value);
                    }
                });
    }


    /***
     * ignoreElements 不发射任何数据，只允许（onError或onCompleted）通过
     */
    private void useIgnoreElement() {
        Observable.fromArray(1, 2, 3, 4, 5, 6)
                .ignoreElements()
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError");

                    }
                });
    }

    /***
     * 仅发射上游的第一个元素，如果上游不发射任何数据，则默认向下游发射 默认值
     */
    private void useFirst() {
//        Observable.fromArray(1, 2, 3, 4, 5, 6)
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onComplete();
            }
        }).first(-10)  //仅发射上游的第一个元素，如果上游不发射任何数据，则默认向下游发射 默认值
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer value) throws Exception {
                        Log.i(TAG, "value:" + value);
                    }
                });
    }

    /***
     * 仅发射上游的最后一个元素，如果上游不发射任何数据，则默认向下游发射 默认值
     */
    private void useLast() {
        Observable.fromArray(1, 2, 3, 4, 5, 6)
                .last(3)
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer value) throws Exception {
                        Log.i(TAG, "value:" + value);
                    }
                });
    }

    /***
     * 按照指定的间隔采样
     */
    private void useSample() {
        Observable.interval(1, TimeUnit.SECONDS)
                .sample(3, TimeUnit.SECONDS)  //按照3秒一次的时间间隔采样
                .take(10)  //仅取前10次
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {
                        Log.i(TAG, "onNext:" + aLong);
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