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

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.observables.GroupedObservable;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava Map操作符使用示例
 * 上游--> 变换操作符 -->下游
 * 1. map 把上游Int 变换为String 观察者String类型
 * 2. flatmap 把上游Int 变换为ObservableSource<String>{还能多次发射事件} 观察者String类型 ；不排序
 * 3. contactmap 把上游Int 变换为ObservableSource<String>{还能多次发射事件} 观察者String类型 ；排序
 * 4. groupby 把上游Int 变换为String分组 观察者GroupedObservable类型 {key="",value为GroupedObservable再次订阅具体分组内容}
 * 4. buffer 把上游100个Int buffer(20) 分批次发射 ，观察者List<Integer>
 * Author: keno
 * CreateDate: 2021/1/2 9:42
 */
public class RxJavaMapFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RxJavaMapFragment";

    public RxJavaMapFragment() {
    }

    public static RxJavaMapFragment newInstance() {
        RxJavaMapFragment fragment = new RxJavaMapFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rx_java_map, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_rxjava_operate_map).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_flatmap).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_concatMap).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_buffer).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_groupby).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) v).getText() + "-------------------------\n");
        }

        switch (v.getId()) {
            case R.id.btn_rxjava_operate_map:
                useMap();
                break;
            case R.id.btn_rxjava_operate_flatmap:
                useFlatMap();
                break;
            case R.id.btn_rxjava_operate_concatMap:
                useContactMap();
                break;
            case R.id.btn_rxjava_operate_groupby:
                useGroupBy();
                break;
            case R.id.btn_rxjava_operate_buffer:
                useBuffer();
                break;
        }
    }


    /***
     *  map操作符实例：
     *  把上游Int 变换为String 观察者String类型
     */
    private void useMap() {
        //上游
        Observable.just(1, 2, 3, 4)
                //变换操作符 对Observable每一项数据或函数执行变换操作
                .map(new Function<Integer, Double>() {
                    @Override
                    public Double apply(@NonNull Integer integer) throws Exception {
                        //将int类型变换为mdouble类型
                        return (double) integer / 2;
                    }
                })
                //订阅
                .subscribe(
                        //下游
                        new Observer<Double>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                Log.i(TAG, "onSubscribe");
                            }

                            @Override
                            public void onNext(@NonNull Double value) {
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
     * FlatMap变换操作符
     */
    private void useFlatMap() {
        Observable.just(100, 200, 300, 400, 500)
                .flatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                        //可多次发射 不排序
                        List<String> list = new ArrayList<>();
                        for (int i = 1; i <= 3; i++) {
                            list.add(i + ".flatMap value:" + integer);
                        }
                        return Observable.fromIterable(list).delay(1, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, s);
                    }
                });
    }

    /***
     * concatMap变换操作符 (排序)
     */
    private void useContactMap() {
        Observable.just(100, 200, 300, 400, 500)
                .concatMap(new Function<Integer, ObservableSource<String>>() {
                    @Override
                    public ObservableSource<String> apply(@NonNull Integer integer) throws Exception {
                        List<String> list = new ArrayList<>();
                        for (int i = 1; i <= 3; i++) {
                            list.add(i + ".concatMap value:" + integer);
                        }
                        return Observable.fromIterable(list).delay(1, TimeUnit.SECONDS);
                    }
                })
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Log.i(TAG, s);
                    }
                });
    }

    /***
     * GroupBy (分组)
     */
    private void useGroupBy() {
        Observable.just(550, 800, 900, 1000, 2000, 3000, 4000)
                .groupBy(new Function<Integer, String>() {
                    @Override
                    public String apply(@NonNull Integer integer) throws Exception {
                        //分组
                        return integer <= 1000 ? "千元机" : "旗舰机";
                    }
                })
                .subscribe(new Consumer<GroupedObservable<String, Integer>>() {
                    @Override
                    public void accept(GroupedObservable<String, Integer> groupedObservable) throws Exception {
                        Log.i(TAG, groupedObservable.getKey());
                        Log.i(TAG, "------------------------");

                        //groupedObservable 为Observable子类，具体分组信息，需要再次subscribe
                        groupedObservable.subscribe(new Consumer<Integer>() {
                            @Override
                            public void accept(Integer integer) throws Exception {
                                Log.i(TAG, groupedObservable.getKey() + ":" + integer);
                            }
                        });
                    }
                });
    }


    /***
     * buffer，将多个事件分批次发射
     */
    private void useBuffer() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 200; i++) {
                    emitter.onNext(i);
                }
            }
        })
                .buffer(40)
                .subscribe(new Consumer<List<Integer>>() {
                    @Override
                    public void accept(List<Integer> integers) throws Exception {
                        Log.i(TAG, "accept：" + integers);
                    }
                });
    }

}