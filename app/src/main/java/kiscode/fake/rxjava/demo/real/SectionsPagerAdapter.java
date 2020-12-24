package kiscode.fake.rxjava.demo.real;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import kiscode.fake.rxjava.demo.R;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaCreateFragment;
import kiscode.fake.rxjava.demo.real.fragment.RxJavaMapFragment;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.operator_create, R.string.operator_map};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (TAB_TITLES[position]) {
            case R.string.operator_create:
                return RxJavaCreateFragment.newInstance();
//            case R.string.operator_map:
//                return RxJavaMapFragment.newInstance();
            default:
                return RxJavaMapFragment.newInstance();
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