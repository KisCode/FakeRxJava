package kiscode.fake.rxjava.demo.real.fragment;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.fragment.app.Fragment;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import kiscode.fake.rxjava.demo.R;

/**
 * Description: RxJava Scheduler线程调度
 * Schedulers的集中线程
 * 1. Schedulers.io() : io流操作，网络请求、文件流等耗时操作
 * 2. Schedulers.newThread(): 新建一个线程
 * 3. Schedulers.computation(): 代表CPU大量计算所需要的线程
 * 4. AndroidSchedulers.mainThread()：Android UI线程
 * <p>
 * subscribeOn() 给上游分配多次，只会在第一次切换
 * observeOn():给下游分配多次线程，每次都会去切换
 * <p>
 * 同步和异步的区别
 * 1. 如果上游和下游都未指定线程，则默认为主线程同步执行，上游发射一次下游接收一次
 * 2. 如果上游和下游指定不同线程，则为异步执行，上游全部发射完成，切换线程后下游接收
 * Author: keno
 * CreateDate: 2021/1/3 15:57
 */

public class RxJavaSchedulerFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RxJavaSchedulerFragment.class.getSimpleName();

    private ImageView ivPicture;

    public RxJavaSchedulerFragment() {
    }

    public static RxJavaSchedulerFragment newInstance() {
        RxJavaSchedulerFragment fragment = new RxJavaSchedulerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rx_java_scheduler, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        ivPicture = view.findViewById(R.id.iv_picture);

        view.findViewById(R.id.btn_rxjava_asyn).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_syn).setOnClickListener(this);
        view.findViewById(R.id.btn_rxjava_download_picture).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view instanceof Button) {
            Log.i(TAG, "\n-------------------------" + ((Button) view).getText() + "-------------------------\n");
        }

        switch (view.getId()) {
            case R.id.btn_rxjava_asyn:
                useAysnThread();
                break;
            case R.id.btn_rxjava_syn: //同步调用
                useSynThread();
                break;
            case R.id.btn_rxjava_download_picture: //下载图片
                downloadPicture();
                break;
        }
    }


    /***
     * 使用异步线程调用
     * Schedulers.io() : io流操作，网络请求、文件流等耗时操作
     * Schedulers.newThread(): 新建一个线程
     * Schedulers.computation(): 代表CPU大量计算所需要的线程
     *
     * AndroidSchedulers.mainThread()：Android UI线程
     *
     * subscribeOn() 给上游分配多次，只会在第一次切换
     * observeOn():给下游分配多次线程，每次都会去切换
     */
    private void useAysnThread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                Log.i(TAG, "Observable in " + Thread.currentThread().getName());
                for (int i = 0; i < 10; i++) {
                    emitter.onNext(1100 + 100 * i);
                }
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io()) //指定上游被观察者 io线程
                //给上游分配多次线程，只会在第一次切换
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribeOn(AndroidSchedulers.mainThread())

                //给下游分配多次线程，每次都会去切换
                .observeOn(AndroidSchedulers.mainThread()) //指定下游观察者 UI线程
                .observeOn(Schedulers.io())
                .observeOn(Schedulers.computation())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe in " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "onNext:" + integer + " in " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError in " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete in " + Thread.currentThread().getName());
                    }
                });
    }


    /***
     * 如果上游和下游都未指定线程，则默认为主线程同步执行，上游发射一次下游接收一次
     *<p>
     * 2021-01-03 15:18:48.821 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游发射： 1
     * 2021-01-03 15:18:48.823 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游 onNext:1
     * 2021-01-03 15:18:48.823 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游发射： 2
     * 2021-01-03 15:18:48.823 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游 onNext:2
     * 2021-01-03 15:18:48.824 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游发射： 3
     * 2021-01-03 15:18:48.824 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游 onNext:3
     * 2021-01-03 15:18:48.825 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游onComplete
     * 2021-01-03 15:18:48.825 16080-16080/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游onComplete in main
     *</p>
     *
     *
     * 如果上游和下游指定不同线程，则为异步执行，上游发射完成，切换线程后下游接收
     * <p>
     *  2021-01-03 15:20:31.644 16244-16281/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游发射： 1
     * 2021-01-03 15:20:31.645 16244-16281/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游发射： 2
     * 2021-01-03 15:20:31.645 16244-16281/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游发射： 3
     * 2021-01-03 15:20:31.645 16244-16281/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 上游onComplete
     * 2021-01-03 15:20:31.652 16244-16244/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游 onNext:1
     * 2021-01-03 15:20:31.652 16244-16244/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游 onNext:2
     * 2021-01-03 15:20:31.652 16244-16244/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游 onNext:3
     * 2021-01-03 15:20:31.652 16244-16244/kiscode.fake.rxjava.demo I/RxJavaSchedulerFragment: 下游onComplete in main
     * </p>
     */
    private void useSynThread() {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 1; i < 4; i++) {
                    Log.i(TAG, "上游发射： " + i);
                    emitter.onNext(i);
                }
                Log.i(TAG, "上游onComplete");
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe in " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        Log.i(TAG, "下游 onNext:" + integer);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError in " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "下游onComplete in " + Thread.currentThread().getName());
                    }
                });
    }


    /***
     * 异步请求图片实例
     */
    private void downloadPicture() {
        String path = "https://www.baidu.com/img/PCtm_d9c8750bed0b3c7d089fa7d55720d6cf.png";
        Observable.just(path)
                .map(new Function<String, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull String s) throws Exception {
                        Log.i(TAG, "下载图片 in " + Thread.currentThread().getName());
                        URL url = new URL(s);
                        URLConnection urlConnection = url.openConnection();
                        HttpURLConnection httpURLConnection = (HttpURLConnection) urlConnection;
                        httpURLConnection.setConnectTimeout(30 * 1000);
                        int responseCode = httpURLConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            Bitmap bitmap = BitmapFactory.decodeStream(httpURLConnection.getInputStream());
                            return bitmap;
                        }
                        return null;
                    }
                })
                .map(new Function<Bitmap, Bitmap>() {
                    @Override
                    public Bitmap apply(@NonNull Bitmap bitmap) throws Exception {
                        //
                        Log.i(TAG, Thread.currentThread().getName() + "\tLog Width:" + bitmap.getWidth() + "\tHeight:" + bitmap.getHeight());
                        return bitmap;
                    }
                })
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Bitmap>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG, "onSubscribe in " + Thread.currentThread().getName());
                    }

                    @Override
                    public void onNext(@NonNull Bitmap bitmap) {
                        Log.i(TAG, "onNext in " + Thread.currentThread().getName());
                        ivPicture.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.i(TAG, "onError in " + Thread.currentThread().getName() + "\t" + e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG, "onComplete in " + Thread.currentThread().getName());
                    }
                });
    }

}