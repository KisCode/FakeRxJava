package kiscode.fake.rxjava.demo.real.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import kiscode.fake.rxjava.demo.R;


public class RxJavaCreateFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "RxJavaCreateFragment";

    private Button btnRxjavaOperateObservable;

    public RxJavaCreateFragment() {
    }

    public static RxJavaCreateFragment newInstance() {
        RxJavaCreateFragment fragment = new RxJavaCreateFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_rx_java_create, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        btnRxjavaOperateObservable = view.findViewById(R.id.btn_rxjava_operate_observable);

        btnRxjavaOperateObservable.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava_operate_observable:
                useObservable();
                break;
            default:
                break;
        }
    }

    private void useObservable() {
        new ObservableOnSubscribe<Integer>(){

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {

            }
        };
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                emitter.onNext(1);
                emitter.onNext(2);
                emitter.onNext(3);
                emitter.onComplete();
            }
        })
           .subscribeOn(Schedulers.io())
           .observeOn(AndroidSchedulers.mainThread())
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
}