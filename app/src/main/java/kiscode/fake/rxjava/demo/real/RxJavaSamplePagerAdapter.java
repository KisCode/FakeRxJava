package kiscode.fake.rxjava.demo.real;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import kiscode.fake.rxjava.demo.R;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaConditionFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaCreateFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaEmptyFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaErrorHandleFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaFilterFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaFlowableFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaMapFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaMergeFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaSchedulerFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class RxJavaSamplePagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.title_operator_create, R.string.title_operator_map, R.string.title_operator_filter, R.string.title_operator_condition, R.string.title_operator_merge, R.string.title_operator_error, R.string.title_operator_scheduler, R.string.title_operator_flowable};
    private final Context mContext;

    public RxJavaSamplePagerAdapter(Context context, FragmentManager fm) {
        super(fm,BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (TAB_TITLES[position]) {
            case R.string.title_operator_create:
                return RxJavaCreateFragment.newInstance();
            case R.string.title_operator_map:
                return RxJavaMapFragment.newInstance();
            case R.string.title_operator_filter:
                return RxJavaFilterFragment.newInstance();
            case R.string.title_operator_condition:
                return RxJavaConditionFragment.newInstance();
            case R.string.title_operator_merge:
                return RxJavaMergeFragment.newInstance();
            case R.string.title_operator_error:
                return RxJavaErrorHandleFragment.newInstance();
            case R.string.title_operator_scheduler:
                return RxJavaSchedulerFragment.newInstance();
            case R.string.title_operator_flowable:
                return RxJavaFlowableFragment.newInstance();
            default:
                return RxJavaEmptyFragment.newInstance();
        }
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return TAB_TITLES.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return mContext.getResources().getString(TAB_TITLES[position]);
    }
}