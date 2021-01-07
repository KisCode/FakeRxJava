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
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: 背压 Flowable使用
 * Flowable是为了解决 Observable上下游 流速不均匀问题，下游根据自己处理能力 响应式拉取上游发射数据
 * 1. 背压的几种策略
 * BackpressureStrategy.BUFFER 上游发射大量事件，下游阻塞处理不过来，放入缓存池(缓存池无限制,直到OOM)，等待下游来接收事件处理，下游可通过Subscription.request(count)从缓冲池处理指定次数
 * BackpressureStrategy.ERROR 上游发射事件超过默认上限 128，将抛出MissingBackpressureException
 * BackpressureStrategy.DROP 上游发射事件超过上限，则抛弃超出缓存的剩余事件，执行onComplete
 * BackpressureStrategy.LATEST 上游发射大量事件下游阻塞不过来，则抛弃超出缓存的剩余事 件但还是会保存最后一次事件 ，执行onComplete
 * <p>
 * Author: keno
 * Date : 2021/1/5 17:27
 **/
public class RxJavaFlowableFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RxJavaFlowableFragment.class.getSimpleName();

    private Subscription mSubscription;

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
        view.findViewById(R.id.btn_rxjava_backpress_exception).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_flowable_sync).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_flowable_async).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_flowable_request).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_flowable_latest).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) view).getText() + "-------------------------\n");
        }

        switch (view.getId()) {
            case R.id.btn_rxjava_backpress_exception:
                useBackPressException();
                break;
            case R.id.btn_rxjava_flowable_sync:
                useSync();
                break;
            case R.id.btn_rxjava_flowable_async:
                useAsyc();
                break;
            case R.id.btn_rxjava_flowable_request:
                requestSubscription();
                break;
            case R.id.btn_rxjava_flowable_latest:
                requestBackpressLatest();
                break;
        }
    }


    private void useBackPressException() {
        //https://www.cnblogs.com/welhzh/p/7941358.html
        //Observable 不再支持背压，上下游速度不均不会抛出MissingBackpressureException
/*
//        Observable.interval(1, TimeUnit.MILLISECONDS)
        Observable.create(new ObservableOnSubscribe<Long>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Long> emitter) throws Exception {
                for (int i = 0; i < Integer.MAX_VALUE; i++) {
                    emitter.onNext((long) i);
                }
                emitter.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i(TAG, "accept:" + aLong);
                    }
                });*/

        //
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                // 默认缓存数量128，超过则抛出异常
                // io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
//                for (int i = 0; i < 129; i++) {
                for (int i = 0; i < 128; i++) {
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.ERROR)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        Log.i(TAG, "onSubscribe");
                        mSubscription = subscription;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError:" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    private void useSync() {
        /***
         * BackpressureStrategy.BUFFER
         * BackpressureStrategy.ERROR 放入Flowable异步缓存池的数据超限，将抛出MissingBackpressureException
         */
        Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 1280; i++) {
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }, BackpressureStrategy.BUFFER)
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        Log.i(TAG, "onSubscribe");
//                        subscription.request(100);
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        //同步背压，上游发送下游若不做处理则抛出异常
                        Log.e(TAG, "onError:" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    /***
     * 异步背压
     */
    private void useAsyc() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 0; i < 1280; i++) {
                                    Log.i(TAG, "上游：subscribe:" + i);
                                    emitter.onNext(i);
                                }
                                emitter.onComplete();
                            }
                        }
                , BackpressureStrategy.BUFFER
//        , BackpressureStrategy.DROP
        )
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
/*                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        //默认缓存大小为 128,如发射超出则抛出异常 BackpressureStrategy.BUFFER
//                        onError:io.reactivex.exceptions.MissingBackpressureException: create: could not emit value due to lack of requests
                        Log.i(TAG, "accept:" + integer);
                    }
                })*/
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        Log.i(TAG, "onSubscribe");
                        mSubscription = subscription;

                        //向上游请求执行请求数量 下游根据自己的处理能力响应式拉取
                        subscription.request(100);

//                        subscription.cancel();  //切断上下游
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.i(TAG, "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError:" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }

    private void requestBackpressLatest() {
        Flowable.create(new FlowableOnSubscribe<Integer>() {
                            @Override
                            public void subscribe(@NonNull FlowableEmitter<Integer> emitter) throws Exception {
                                for (int i = 1; i < 1280; i++) {
                                    Thread.sleep(1);//1ms 发射一次
                                    Log.i(TAG, "上游：subscribe:" + i);
                                    emitter.onNext(i);
                                }
                                emitter.onComplete();
                            }
                        }
                , BackpressureStrategy.LATEST
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        Log.i(TAG, "onSubscribe");
                        mSubscription = subscription;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        try {
                            Thread.sleep(100);//100ms 接收一次
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        Log.i(TAG, "onNext:" + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.e(TAG, "onError:" + t.toString());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete");
                    }
                });
    }




    private void requestSubscription() {
        if (mSubscription != null) {
            //从上游缓存中取事件进行处理
            mSubscription.request(100);
        }
    }


}