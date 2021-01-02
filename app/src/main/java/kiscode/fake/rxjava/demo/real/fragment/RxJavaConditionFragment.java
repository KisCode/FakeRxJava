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
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Predicate;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava 条件操作符使用示例
 * 上游--> 条件操作符 -->下游(Boolean)
 * all、any、contain
 * 1.all 发射的所有数据都满足某个条件
 * 2.any 发射的其中一个数据满足某个条件
 * 3.contain 发射的数据满包含某个元素
 *
 * Author: keno
 * CreateDate: 2021/1/2 15:32
 */
public class RxJavaConditionFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = RxJavaConditionFragment.class.getSimpleName();

    public RxJavaConditionFragment() {
    }

    public static RxJavaConditionFragment newInstance() {
        RxJavaConditionFragment fragment = new RxJavaConditionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rx_java_conditaion, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
//        filter、take、distinct、skip、elementAt、ignoreElement、first、last、sample
        view.findViewById(R.id.btn_rxjava_operate_all).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_any).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_contain).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) v).getText() + "-------------------------\n");
        }

        switch (v.getId()) {
            case R.id.btn_rxjava_operate_all:
                useAll();
                break;
            case R.id.btn_rxjava_operate_any:
                useAny();
                break;
            case R.id.btn_rxjava_operate_contain:
                useContains();
                break;
        }
    }

    /***
     *  all操作符实例：
     * 全部数据满足条件
     */
    private void useAll() {
        //上游
        Observable.just(1, 2, 3, 4, 5)
                .all(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 3;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean bool) throws Exception {
                        Log.i(TAG, "all value:" + bool);
                    }
                });
    }

    /***
     *  any操作符实例：
     * 任何一个数据满足条件
     */
    private void useAny() {
        //上游
        Observable.just(1, 2, 3, 4, 5)
                .any(new Predicate<Integer>() {
                    @Override
                    public boolean test(@NonNull Integer integer) throws Exception {
                        return integer > 3;
                    }
                })
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean bool) throws Exception {
                        Log.i(TAG, "any value:" + bool);
                    }
                });
    }

    /***
     *  contains操作符实例：
     * 包含
     */
    private void useContains() {
        //上游
        Observable.just(1, 2, 3, 4, 5)
                .contains(3) //是否包含元素“3”
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean bool) throws Exception {
                        Log.i(TAG, "contains value:" + bool);
                    }
                });
    }


}