package com.flipImage;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

/**
 * Created by Zhang.Y.C on 2017/7/3.
 */

public class Flip3dAnimationListener implements Animation.AnimationListener {
    ImageView frontImage;
    ImageView contraryImage;
    boolean isFront;

    public Flip3dAnimationListener (boolean isFront, ImageView ivFront, ImageView ivContrary){
        this.isFront = isFront;
        frontImage = ivFront;
        contraryImage = ivContrary;
    }
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        frontImage.post(new Runnable() {
            @Override
            public void run() {
                Flip3dAnimation flip3dAnimation;
                float centerX = frontImage.getWidth() / 2.0f;
                float centerY = frontImage.getHeight() / 2.0f;

                if (isFront){
                    frontImage.setVisibility(View.GONE);
                    contraryImage.setVisibility(View.VISIBLE);
                    contraryImage.requestFocus();
                    flip3dAnimation = new Flip3dAnimation(-90, 0, centerX, centerY);
                } else {
                    frontImage.setVisibility(View.VISIBLE);
                    contraryImage.setVisibility(View.GONE);
                    frontImage.requestFocus();
                    flip3dAnimation = new Flip3dAnimation(90, 0, centerX, centerY);
                }
                flip3dAnimation.setDuration(500);
                flip3dAnimation.setFillAfter(true);
                flip3dAnimation.setInterpolator(new DecelerateInterpolator());

                if (isFront){
                    contraryImage.startAnimation(flip3dAnimation);
                } else {
                    frontImage.startAnimation(flip3dAnimation);
                }
            }
        });
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}
