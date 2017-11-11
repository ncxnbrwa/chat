package com.yxf.chat.home;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;


/**
 * 右侧滑栏的adapter
 *
 * @author xiong
 * @name RightMenuFragAdapter
 * @date 2017/8/19
 */
public class RightMenuFragAdapter extends FragmentPagerAdapter {
    private List<Fragment> fms;

    public RightMenuFragAdapter(FragmentManager fm, List<Fragment> fms) {
        super(fm);
        this.fms = fms;
    }

    @Override
    public Fragment getItem(int position) {
        return fms.get(position);
    }

    @Override
    public int getCount() {
        return fms.size();
    }
}
