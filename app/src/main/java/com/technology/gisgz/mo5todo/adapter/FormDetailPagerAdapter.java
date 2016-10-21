package com.technology.gisgz.mo5todo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.technology.gisgz.mo5todo.fragment.FormDetailFragment;
import com.technology.gisgz.mo5todo.fragment.FormRoutingFragment;

/**
 * Created by Jim.Lee on 2016/5/13.
 */
public class FormDetailPagerAdapter extends FragmentPagerAdapter {
    private Fragment detailFragment;
    private Fragment routingFragment;

    public FormDetailPagerAdapter(FragmentManager fm,Fragment detail,Fragment routing) {
        super(fm);
        detailFragment = detail;
        routingFragment = routing;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        if(position==0){
            return detailFragment;
        }else{
            return routingFragment;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "detail";
            case 1:
                return "routing";
        }
        return null;
    }
}
