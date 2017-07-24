package com.Training.ViewPageForScreenSlides;

import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;

import com.umbrella.sharedemo.R;

public class ScreenSlideActivity extends FragmentActivity {
    /**
     * Activity layout contain
     * ViewPager need
     * PagerAdapter contain
     * item of Fragment;
     * And page can set animation.
     *
     */


    ViewPager mPager;
    ScreenSlidePagerAdapter mPagerAdapter;
    public static int PAGE_COUNT = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_training_screen_slide);

        // Instantiate a ViewPager and a PagerAdapter.
        mPager = (ViewPager) findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(mPagerAdapter);
        mPager.setPageTransformer(true, new SlideTransformer());
    }

    @Override
    public void onBackPressed(){
        if (mPager.getCurrentItem() == 0){
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1);
        }
    }


}
