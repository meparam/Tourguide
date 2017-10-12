package com.thinktanki.atmfinder.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

import com.thinktanki.atmfinder.atm.ListFragment;
import com.thinktanki.atmfinder.atm.MapFragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by aruns512 on 20/12/2016.
 */
public class FragmentViewPager extends FragmentStatePagerAdapter {
    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private ArrayList<String> mTabHeader;

    public FragmentViewPager(FragmentManager fm, ArrayList<String> tabHeader) {
        super(fm);
        this.mTabHeader = tabHeader;
    }
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                ListFragment listFragment = new ListFragment("atm");
                return listFragment;
            case 1:
                MapFragment mapFragment = new MapFragment();
                return mapFragment;

            case 2:
                ListFragment listFragment3 = new ListFragment("car_rental");
                return listFragment3;
            case 3:
                ListFragment listFragment4 = new ListFragment("bus_station");
                return listFragment4;

            case 4:
                ListFragment listFragment5 = new ListFragment("restaurant");
                return listFragment5;
            case 5:
                ListFragment listFragment6 = new ListFragment("hindu_temple");
                return listFragment6;


            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mTabHeader.size();
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTabHeader.get(position);
    }
}
