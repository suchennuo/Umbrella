package com.Training.ViewPageForScreenSlides;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.TextViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.umbrella.sharedemo.R;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zhang.Y.C on 2017/7/14.
 */

public class ScreenSlidePageFragment extends Fragment {

    @BindView(R.id.tv_content)
    TextView tvContent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_screen_slide_page, container, false);
        ButterKnife.bind(this, rootView);

        Bundle args = getArguments();
        int index = args.getInt("INDEX");
        tvContent.setText("Fragment " + index);
        return rootView;
    }

}
