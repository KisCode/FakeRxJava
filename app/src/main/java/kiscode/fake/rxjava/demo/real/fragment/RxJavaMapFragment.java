package kiscode.fake.rxjava.demo.real.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import kiscode.fake.rxjava.demo.R;

public class RxJavaMapFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = "RxJavaMapFragment";

    private Button btnMap;

    public RxJavaMapFragment() {
    }

    public static RxJavaMapFragment newInstance() {
        RxJavaMapFragment fragment = new RxJavaMapFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_rx_java_map,container,false);

        initView(view);

        return view;
    }

    private void initView(View view) {
        btnMap = view.findViewById(R.id.btn_rxjava_operate_map);
        btnMap.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.btn_rxjava_operate_map:
                mapOperator();
                break;
        }
    }

    private void mapOperator() {
        Observable.just(1,2,3,4)
                .map(new Function<Integer, Double>() {
                    @Override
                    public Double apply(@NonNull Integer integer) throws Exception {
                        return (double)integer/2;
                    }
                })
                .subscribe(new Observer<Double>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        Log.i(TAG,"onSubscribe");
                    }

                    @Override
                    public void onNext(@NonNull Double value) {
                        Log.i(TAG,"onNext:"+value);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG,"onError:"+e);
                    }

                    @Override
                    public void onComplete() {
                        Log.i(TAG,"onComplete");

                    }
                });
    }
}