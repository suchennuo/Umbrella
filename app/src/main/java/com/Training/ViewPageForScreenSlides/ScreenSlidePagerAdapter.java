package com.Training.ViewPageForScreenSlides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Zhang.Y.C on 2017/7/17.
 */

public class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter{


    public ScreenSlidePagerAdapter(FragmentManager fm){
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        ScreenSlidePageFragment pageFragment = new ScreenSlidePageFragment();
        /**
         * 使用 Bundle 传递数据
         */
        Bundle args = new Bundle();
        args.putInt("INDEX", position);
        pageFragment.setArguments(args);

        return pageFragment;
    }

    @Override
    public int getCount() {
        return ScreenSlideActivity.PAGE_COUNT;
    }
}
