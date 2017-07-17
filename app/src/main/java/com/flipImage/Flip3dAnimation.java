package com.flipImage;

import android.animation.Animator;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.ImageView;

/**
 * Created by Zhang.Y.C on 2017/7/3.
 */

public class Flip3dAnimation extends Animation{

    private float fromDegrees;
    private float toDegrees;
    private float centerX;
    private float centerY;
    private Camera camera;

    ImageView view;
    Drawable frontIcon, contrarayIcon;

    public Flip3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY,
                           ImageView view, int frontIcon, int contraryIcon){
        this.fromDegrees = fromDegrees;
        this.toDegrees = toDegrees;
        this.centerX = centerX;
        this.centerY = centerY;

        this.view = view;

        this.frontIcon = view.getResources().getDrawable(frontIcon, null);
        this.contrarayIcon = view.getResources().getDrawable(contraryIcon, null);
        view.setTag(0);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight){
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }
    private static String TAG = "Flip3dAnimation.";

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation){
        float degree = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);
        if (degree >= 90 && degree < 270){

            if ((int)view.getTag() != 1){

                view.setImageDrawable(contrarayIcon);
                view.setTag(1);
            }
        }  else if (degree >= 270) {
            if ((int)view.getTag() != 2) {
                view.setImageDrawable(frontIcon);
                view.setTag(2);
            }
        }


        Matrix matrix = transformation.getMatrix();
        camera.save();
        camera.rotateY(degree);
        camera.getMatrix(matrix);
        camera.restore();

        //将 matrix 操作的基点移到整个view 的中心，不然系统默认是以 view 的左上角为基点
        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

}

/**
 * 1. getMatrix Computes the matrix corresponding to the current transformation and copies it to the supplied matrix object.
 * 2. preTranslate
 */
