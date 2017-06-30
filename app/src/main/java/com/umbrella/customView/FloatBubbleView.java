package com.umbrella.customView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;

/**
 * Created by Zhang.Y.C on 2017/6/14.
 */

public class FloatBubbleView extends View {
    public FloatBubbleView(Context context) {
        this(context, null);
    }

    public FloatBubbleView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatBubbleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }


    private Paint paint;

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        paint = new Paint();
        paint.setColor(Color.BLUE);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);

        windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    }


    /**
     * 1. 绘画floating button （绘制， 悬浮）
     * 2. 拖动事件，靠边
     * 3. 点击弹出选项菜单
     * 4. 除了点击菜单逻辑
     */

    private float x, y;

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawCircle(100, 100, 60, paint);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
    }


    @Override
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams){
        super.setLayoutParams(layoutParams);
    }


    /**
     * 2 拖动事件，靠边
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }
}
