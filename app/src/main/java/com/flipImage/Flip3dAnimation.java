package com.flipImage;

import android.animation.Animator;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.view.animation.Animation;
import android.view.animation.Transformation;

/**
 * Created by Zhang.Y.C on 2017/7/3.
 */

public class Flip3dAnimation extends Animation{

    private float fromDegrees;
    private float toDegrees;
    private float centerX;
    private float centerY;
    private Camera camera;

    public Flip3dAnimation(float fromDegrees, float toDegrees, float centerX, float centerY){
        this.fromDegrees = fromDegrees;
        this.toDegrees = toDegrees;
        this.centerX = centerX;
        this.centerY = centerY;
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight){
        super.initialize(width, height, parentWidth, parentHeight);
        camera = new Camera();
    }

    @Override
    protected void applyTransformation(float interpolatedTime, Transformation transformation){
        float degree = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime);
        Matrix matrix = transformation.getMatrix();
        camera.save();
        camera.rotateY(degree);
        camera.getMatrix(matrix);
        camera.restore();

        matrix.preTranslate(-centerX, -centerY);
        matrix.postTranslate(centerX, centerY);
    }

}

/**
 * 1. getMatrix Computes the matrix corresponding to the current transformation and copies it to the supplied matrix object.
 * 2. preTranslate
 */
