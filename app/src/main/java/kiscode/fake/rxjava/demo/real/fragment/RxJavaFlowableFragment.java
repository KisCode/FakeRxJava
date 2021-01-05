package kiscode.fake.rxjava.demo.real.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: 背压 Flowable使用
 * Author: keno
 * Date : 2021/1/5 17:27
 **/
public class RxJavaFlowableFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RxJavaFlowableFragment.class.getSimpleName();


    public RxJavaFlowableFragment() {
    }

    public static RxJavaFlowableFragment newInstance() {
        RxJavaFlowableFragment fragment = new RxJavaFlowableFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rx_java_flowable, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_rxjava_flowable_sync).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_flowable_async).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) view).getText() + "-------------------------\n");
        }

        switch (view.getId()) {
            case R.id.btn_rxjava_flowable_sync:
                useSync();
                break;
            case R.id.btn_rxjava_flowable_async:
                break;
        }
    }

    private void useSync() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        Log.i(TAG, "onSubscribe");
                        subscription.request(100);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.i(TAG, "onError:" + t.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

}