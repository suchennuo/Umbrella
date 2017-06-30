package com.umbrella.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;

/**
 * Created by Zhang.Y.C on 2017/6/15.
 */

public class TickClick extends View{
    public TickClick(Context context) {
        this(context, null);
    }

    public TickClick(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TickClick(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }




    private Paint paint;
    private Path path;

    private void init(Context context, AttributeSet attrs, int defStyleAttr){
        paint = new Paint();
        paint.setColor(Color.GREEN);
//        paint.setARGB();
        paint.setAntiAlias(true); //边缘锯齿/
//        paint.setColorFilter() //颜色过滤
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(10);


    }

    @Override
    protected void onDraw(Canvas canvas){

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        canvas.translate(canvas.getWidth() / 2, canvas.getHeight()/3);
        canvas.drawCircle(0, 0, 400, paint); //圆心，半径

        //使用path 绘制路径文字
        canvas.save();
        canvas.translate(-300, -300);
//        canvas.drawRect(0, 0, 600, 600, paint);
        Path path = new Path();
        path.addArc(new RectF(0, 0, 600, 600), -180, 270);
        Paint citePaint = new Paint(paint);
        citePaint.setTextSize(40);
        citePaint.setStrokeWidth(5);
        citePaint.setStyle(Paint.Style.FILL); //沿着路径绘制一段文本
        canvas.drawTextOnPath("http://www.yongchaozhang.com/super_list/login/",
                path, 25, 0, citePaint);
        //hOffset: The distance along the path to add to the text's starting position
        //vOffset: The distance above(-) or below(+) the path to position the text ，WHAT???
        canvas.restore();

//        Paint tmpPaint = new Paint(paint);
//        tmpPaint.setStrokeWidth(5);
        Paint tmpPaint = citePaint;
        float y = -400;
        int count = 60;
        for (int i = count; i >= 1; --i){
            if ( i % 5 == 0){
                canvas.drawLine(0f, y, 0, y - 20f, tmpPaint);
                canvas.drawText(String.valueOf(i / 5 ), -10f, y - 40f, tmpPaint);
            } else {
                canvas.drawLine(0f, y, 0f, y - 10f, tmpPaint);
            }
            canvas.rotate( -360 / count, 0f, 0f); //旋转 canvas , 绕 X , Y 坐标原点 (0, 0);
        }

        tmpPaint.setColor(Color.GRAY);
        tmpPaint.setStrokeWidth(4);
        tmpPaint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(0, 0, 21, tmpPaint);

        tmpPaint.setStyle(Paint.Style.FILL);
        tmpPaint.setColor(Color.YELLOW);
        canvas.drawCircle(0, 0, 15, tmpPaint);

        canvas.save();
        int degree = (int) (mInterpolatedTime * 360);
        Log.d(TAG, "degree " + degree);
        if (degree % 6 == 0) {
            tickCount = degree;
        }
        canvas.rotate(tickCount, 0, 0);

        canvas.drawLine(0, 0, 0, -250, paint);
        canvas.restore();
    }


    private static int tickCount = 0;
    private static final String TAG = "FloatBubbleView";


    private MoveAnimation moveAnimation = null;
    public void startAnimation(){
        mInterpolatedTime = 0;
        moveAnimation = new MoveAnimation();
        moveAnimation.setDuration(60000);
        moveAnimation.setInterpolator(new LinearInterpolator());
        startAnimation(moveAnimation);
    }


    @Override
    protected void onDetachedFromWindow(){
        this.clearAnimation();
        Log.d(TAG, "clear animation ");
        super.onDetachedFromWindow();
    }

    private float mInterpolatedTime;
    /**
     * 自定义动画，重写 applyTransformation , initialize 方法
     */
    class MoveAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t){
            mInterpolatedTime = interpolatedTime;

            invalidate();
        }
    }
}
