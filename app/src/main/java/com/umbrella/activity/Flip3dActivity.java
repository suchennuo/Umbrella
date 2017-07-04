package com.umbrella.activity;

import android.animation.ObjectAnimator;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.flipImage.Flip3dAnimation;
import com.flipImage.Flip3dAnimationListener;
import com.umbrella.sharedemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class Flip3dActivity extends AppCompatActivity {

    @BindView(R.id.iv_3dflip_front)
    ImageView frontView;

    @BindView(R.id.iv_3dflip_contrary)
    ImageView contraryView;

    @BindView(R.id.iv_3dflip_float)
    ImageView floatView;

    @BindView(R.id.bt_3dflip_flip)
    Button btFlip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip3d);
        ButterKnife.bind(this);
    }

    private boolean isFront = true;

    @OnClick(R.id.iv_3dflip_float)
    public void floatView(View view){
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(view, "rotationY", 0, 360);
        rotateAnimator.setDuration(10000);
        rotateAnimator.start();
    }


    @OnClick(R.id.bt_3dflip_flip)
    public void flip(View view){
        if (isFront){
            applyRotation(0, 90);
        } else {
            applyRotation(0, 90);
        }
        isFront = !isFront;
    }

    private void applyRotation(float start, float end){
        float centerX = frontView.getWidth() / 2.0f;
        float centerY = frontView.getHeight() / 2.0f;

        Flip3dAnimation flip3dAnimation = new Flip3dAnimation(start, end, centerX, centerY);
        flip3dAnimation.setDuration(500);
//        flip3dAnimation.setFillAfter(true);
//        flip3dAnimation.setRepeatMode();
        flip3dAnimation.setRepeatCount(Animation.INFINITE);
        flip3dAnimation.setInterpolator(new AccelerateInterpolator());
        flip3dAnimation.setAnimationListener(new Flip3dAnimationListener(isFront, frontView, contraryView));
        if (isFront){
            frontView.startAnimation(flip3dAnimation);
        } else {
            contraryView.startAnimation(flip3dAnimation);
        }
    }
}
