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
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiPredicate;
import io.reactivex.functions.Function;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava 异常处理操作符使用示例
 * 上游--> 异常操作符(拦截异常并处理) -->下游
 * 在RxJava中标准的异常发射 e.onError 会由下游观察者的onError方法回调；如果使用传统的抛出异常方式如 throw new Exeception则下游无法拦截崩溃
 * <p>
 * 1. onErrorReturn 可以拦截e.onError 并且给下游返回一个 标志数值，由下游onNext接收
 * 2. onErrorResumeNext 可拦截e.onError, 可以返回被观察者（可多次发射事件给下游）
 * 3. onExceptionResumeNext 可拦截捕获抛出异常 类似try catch异常，再继续发射数据；可做到让应用真正不崩溃
 * 4. retry 根据函数return true进行重试，return false则不进行重试；可指定重试次数
 * <p>
 * <p>
 * Author: keno
 * CreateDate: 2021/1/3 10:16
 */
public class RxJavaErrorHandleFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = RxJavaErrorHandleFragment.class.getSimpleName();

    public RxJavaErrorHandleFragment() {
    }

    public static RxJavaErrorHandleFragment newInstance() {
        RxJavaErrorHandleFragment fragment = new RxJavaErrorHandleFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rx_java_error_handle, container, false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_rxjava_operate_onErrorReturn).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_onErrorResumeNext).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_onExceptionResumeNext).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_operate_retry).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) v).getText() + "-------------------------\n");
        }

        switch (v.getId()) {
            case R.id.btn_rxjava_operate_onErrorReturn:
                userOnErrorReturn();
                break;
            case R.id.btn_rxjava_operate_onErrorResumeNext:
                userOnErrorResumeNext();
                break;
            case R.id.btn_rxjava_operate_onExceptionResumeNext:
                userOnExceptionResumeNext();
                break;
            case R.id.btn_rxjava_operate_retry:
                userRetry();
                break;

        }
    }


    /***
     * onErrorReturn 能接收onError并提前处理错误，并返回指定标记值 由下游onNext接收
     */
    private void userOnErrorReturn() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 20; i++) {
                    if (i == 10) {
                        emitter.onError(new Exception("An error occurred!"));
                    }
                    emitter.onNext(i);
                }
            }
        }).onErrorReturn(new Function<Throwable, Integer>() {
            @Override
            public Integer apply(@NonNull Throwable throwable) throws Exception {
                //处理错误，并返回指定值 由下游onNext接收
                Log.i(TAG, "onErrorReturn:" + throwable.getMessage());
                return 500;
            }
        }).subscribe(new Observer<Integer>() {
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
     * onErrorResumeNext 可拦截onError, 可以返回被观察者（可多次发射事件给下游）
     */
    private void userOnErrorResumeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new Exception("An error occurred!"));
                    }
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }).onErrorResumeNext(new Function<Throwable, ObservableSource<? extends Integer>>() {
            @Override
            public ObservableSource<? extends Integer> apply(@NonNull Throwable throwable) throws Exception {
                Log.i(TAG, "onErrorResumeNext");
                //返回一个被观察者Observable,可多次发送
                return Observable.create(new ObservableOnSubscribe<Integer>() {
                    @Override
                    public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                        emitter.onNext(400);
                        emitter.onNext(400);
//                        emitter.onComplete();
                    }
                });
            }
        }).subscribe(new Observer<Integer>() {
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
     * onExceptionResumeNext 可接受onError，更可以接收抛出异常 类似try catch异常，再继续发射数据
     */
    private void userOnExceptionResumeNext() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new Exception("An error occurred!"));
//                        throw new Exception("An error occurred!");
                    }
                    emitter.onNext(i);
                }
            }
        }).onExceptionResumeNext(new ObservableSource<Integer>() {
            @Override
            public void subscribe(@NonNull Observer<? super Integer> observer) {
                Log.i(TAG, "onExceptionResumeNext");
                observer.onNext(400);
            }
        }).subscribe(new Observer<Integer>() {
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
     * Retry: 异常重试操作符
     */
    private void userRetry() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10; i++) {
                    if (i == 5) {
                        emitter.onError(new Exception("An error occurred!"));
                    }
                    emitter.onNext(i);
                }
                emitter.onComplete();
            }
        }).retry(new BiPredicate<Integer, Throwable>() {
            @Override
            public boolean test(@NonNull Integer count, @NonNull Throwable throwable) throws Exception {
                //count 重试次数
                Log.i(TAG, "retry:" + count + "\t" + throwable.getMessage());
                //通过count重试判断是否继续重试
                return count < 3;
            }
        })
                /*.retry() //反复重试*/

                //一直重试
                /*.retry(new Predicate<Throwable>() {
            @Override
            public boolean test(@NonNull Throwable throwable) throws Exception {
                Log.i(TAG, "retry:" + throwable.getMessage());
                //return true 是否需要重试
                return true;  //会一直重试
            }
        })*/

                //重试3次
                /*.retry(3, new Predicate<Throwable>() {//重试3次
                    @Override
                    public boolean test(@NonNull Throwable throwable) throws Exception {
                        Log.i(TAG, "retry:" + throwable.getMessage());
                        //return true 是否需要重试
                        return true;  //会一直重试
                    }
                })*/

                .subscribe(new Observer<Integer>() {
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

}