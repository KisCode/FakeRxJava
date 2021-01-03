package kiscode.fake.rxjava.demo.real.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import kiscode.fake.rxjava.demo.R;

/***
 * 空页面
 */
public class RxJavaEmptyFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RxJavaEmptyFragment.class.getSimpleName();


    public RxJavaEmptyFragment() {
    }

    public static RxJavaEmptyFragment newInstance() {
        RxJavaEmptyFragment fragment = new RxJavaEmptyFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rx_java_empty, container, false);
        initView(rootView);
        return rootView;
    }

    private void initView(View view) {
        view.findViewById(R.id.btn_rxjava_empty).setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rxjava_empty:
                show();
                break;
        }
    }


    private void show() {
        Toast.makeText(getContext(), R.string.app_name, Toast.LENGTH_LONG).show();
    }

}