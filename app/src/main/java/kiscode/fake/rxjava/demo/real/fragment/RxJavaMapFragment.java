package kiscode.fake.rxjava.demo.real.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import kiscode.fake.rxjava.demo.R;

public class RxJavaMapFragment extends Fragment {

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rx_java_create, container, false);
    }
}