package kiscode.fake.rxjava.demo.real;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import kiscode.fake.rxjava.demo.R;

/**
* Description:  RxJava使用Sample
* Author: keno
* Date : 2020/12/21 11:39
**/
public class RxJavaSampleActivity extends AppCompatActivity {
    public static void start(Context context) {
        Intent starter = new Intent(context, RxJavaSampleActivity.class);
        context.startActivity(starter);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_java_sample);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }
}