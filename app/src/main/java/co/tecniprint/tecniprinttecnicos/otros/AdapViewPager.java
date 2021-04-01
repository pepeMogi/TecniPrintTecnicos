package co.tecniprint.tecniprinttecnicos.otros;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;

public class AdapViewPager extends FragmentPagerAdapter {

    private Context context;
    private ViewPager viewPager;
    private ArrayList<Fragment> fragments;

    public AdapViewPager(@NonNull FragmentManager fm, Context context, ViewPager viewPager, ArrayList<Fragment> fragments) {
        super(fm);
        this.context = context;
        this.viewPager = viewPager;
        this.fragments = fragments;
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
