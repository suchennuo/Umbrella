package com.umbrella.customView;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import com.umbrella.sharedemo.R;

/**
 * Created by Zhang.Y.RM on 2017/6/12.
 */

public class DropIndication extends View {

    private Path path;
    private Paint paint, circlePaint, clickPaint;

    private int width;
    private int height;
    private int centerX;
    private int centerY;

    private float maxLength;
    private float mInterpolatedTime;
    private float stretchDistance;
    private float moveDistance;
    private float RM;
    private float M = 0.551915024494f;
    private float radius;
    private float cDistance;

    private VPoint p2, p4;
    private HPoint p1, p3;

    private int[] roundColors = new int[4];
    private int clickColor, circleColor;
    private long duration;
    private float scale;


    public DropIndication(Context context) {
        this(context, null, 0);
    }

    public DropIndication(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropIndication(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        initParams(context, attrs, defStyleAttr);
        initCircle();
    }

    private void init(){
        paint = new Paint();
        paint.setColor(0xfffe626d);
        paint.setStyle(Paint.Style.FILL);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);

        path = new Path();

        p2 = new VPoint();
        p4 = new VPoint();

        p1 = new HPoint();
        p3 = new HPoint();
    }

    private void initParams(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DropIndicator);
        roundColors[0] = typedArray.getColor(R.styleable.DropIndicator_color1, Color.parseColor("#B04285F4"));
        roundColors[1] = typedArray.getColor(R.styleable.DropIndicator_color2, Color.parseColor("#B0EA4335"));
        roundColors[2] = typedArray.getColor(R.styleable.DropIndicator_color3, Color.parseColor("#B0FBBC05"));
        roundColors[3] = typedArray.getColor(R.styleable.DropIndicator_color4, Color.parseColor("#B034A853"));
        clickColor = typedArray.getColor(R.styleable.DropIndicator_click_color, Color.WHITE);
        circleColor = typedArray.getColor(R.styleable.DropIndicator_circle_color, Color.GRAY);
        radius = typedArray.getDimension(R.styleable.DropIndicator_radius, 50);
        duration = typedArray.getInteger(R.styleable.DropIndicator_duration, 1000);
        scale = typedArray.getFloat(R.styleable.DropIndicator_scale, 0.8f);
        typedArray.recycle();
    }
    private int startColor;
    private void initCircle(){

        circlePaint = new Paint();
        circlePaint.setColor(circleColor);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setAntiAlias(true);
        circlePaint.setStrokeWidth(3);

        clickPaint = new Paint();
        clickPaint.setColor(clickColor);
        clickPaint.setStyle(Paint.Style.STROKE);
        clickPaint.setAntiAlias(true);
        clickPaint.setStrokeWidth(radius / 2);

        paint = new Paint();
        startColor = roundColors[0];
        paint.setColor(startColor);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(1);
        paint.setAntiAlias(true);
    }

    // WHEN WHY WHERE

    private int tabNum;
    private float div;
    private float startX, startY, totalOff, currentPos;
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        width = w;
        height = h;
        div = (width - 2 * tabNum * radius) / (tabNum + 1);
        startX = div + radius;
        startY = height / 2;
        totalOff = (tabNum - 1) * (2 * radius + div) - radius;

        if (currentPos == 0){
//            radius =
        }

        super.onSizeChanged(w, h, oldw, oldh);
    }


    @Override
    protected void dispatchDraw(Canvas canvas){
        canvas.save();
        path.reset();
//        tabNum = getChildCount();
        for (int i = 0; i < tabNum; ++i){
            canvas.drawCircle(div + radius + i * (div + 2 * radius), startY, radius, circlePaint);
        }
//        processAnimationTime();

        canvas.restore();
        super.dispatchDraw(canvas);

    }





    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom){
        super.onLayout(changed, left, top, right, bottom);

        width = getWidth();
        height = getHeight();
        centerX = width/2;
        centerY = height/2;
        radius = 50;
        RM = radius*M;

        stretchDistance = radius;
        moveDistance = radius*(3/5f);
        cDistance = RM*0.45f;
        maxLength = width - radius - radius; //移动的最长距离

    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        path.reset();
        canvas.translate(radius, radius); //

        if (mInterpolatedTime >= 0 && mInterpolatedTime <= 0.2){
            model1(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.2 && mInterpolatedTime <= 0.5){
            model2(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.5 && mInterpolatedTime <= 0.8){
            model3(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.8 && mInterpolatedTime <= 0.9){
            model4(mInterpolatedTime);
        } else if (mInterpolatedTime > 0.9 && mInterpolatedTime <= 1){
            model5(mInterpolatedTime);
        }

        float offset = maxLength * (mInterpolatedTime - 0.2f);
        offset = offset > 0 ? offset : 0; //0.2f 之后，所有点 X 坐标一直增加
        p1.adjustAllX(offset);
        p2.adjustAllX(offset);
        p3.adjustAllX(offset);
        p4.adjustAllX(offset);

        path.moveTo(p1.x, p1.y);
        path.cubicTo(p1.right.x, p1.right.y, p2.bottom.x, p2.bottom.y, p2.x, p2.y);
        path.cubicTo(p2.top.x, p2.top.y, p3.right.x, p3.right.y, p3.x, p3.y);
        path.cubicTo(p3.left.x, p3.left.y, p4.top.x, p4.top.y, p4.x, p4.y);
        path.cubicTo(p4.bottom.x, p4.bottom.y, p1.left.x, p1.left.y, p1.x, p1.y);

        canvas.drawPath(path, paint);

    }

    private void model0(){
        p1.setY(radius);
        p3.setY(-radius);
        p3.x = p1.x = 0;
        p3.left.x = p1.left.x = -RM;
        p3.right.x = p1.right.x = RM;

        p2.setX(radius);
        p4.setX(-radius);
        p2.y = p4.y = 0;
        p2.top.y = p4.top.y = -RM;
        p2.bottom.y = p4.bottom.y = RM;
    }
    private void model1(float time){ // 0.1 ~ 0.2
        model0();
        time = (time - 0f) * (10f / 2); // 完成分阶段的任务，进度也应从 0 ~ 100%， 阶段0.1， 0.2 两端时间 。最终， 0.2 * 5 = 1；
        p2.setX(radius + stretchDistance * time); //当完成度为 100% 时， 圆向右凸起 2倍的 radius.
    }

    private void model2(float time){ //0.2 ~0.5 , 逐渐变成椭圆状态
        model1(0.2f);
        time = (time - 0.2f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time); //p1, p3 的 x 坐标向右移动 radius /2
        p3.adjustAllX(stretchDistance / 2 * time);
        p2.adjustY(cDistance * time); // radius * M * 0.45 , 成为一个椭圆
        p4.adjustY(cDistance * time);
    }

    private void model3(float time){ //0.5 ~ 0.8， //终态类似 model1 终态的 1/2 ， 只会凸起 1 倍的 radius
        model2(0.5f);
        time = (time - 0.5f) * (10f / 3);
        p1.adjustAllX(stretchDistance / 2 * time);
        p3.adjustAllX(stretchDistance / 2 * time);  //至此，中心坐标调整了一个 stretchDistance
        p2.adjustY(-cDistance * time); //开始恢复，变得圆
        p4.adjustY(-cDistance * time);
        p4.adjustAllX(stretchDistance / 2 * time);
    }

    private void model4(float time) { //0.8 ~0.9，P4 继续移动 X 坐标，终态为圆形
        model3(0.8f);
        time = (time - 0.8f) * 10;
        p4.adjustAllX(stretchDistance / 2 * time);
    }

    private void model5(float time){ // sin() 往复回弹
        model4(0.9f);
        time = time - 0.9f;
        p4.adjustAllX((float)(Math.sin(Math.PI*time*10f)*(2/10f*radius))); //p4 sin() 往复回弹， 恢复到原状
    }

    class VPoint{
        public float x;
        public float y;
        public PointF top = new PointF();
        public PointF bottom = new PointF();

        public void setX(float x){
            this.x = x;
            top.x = x;
            bottom.x = x;
        }

        public void adjustY(float offset){
            top.y -= offset;
            bottom.y += offset; //圆向中间凹下去
        }

        public void adjustAllX(float offset){
            this.x += offset;
            top.x += offset;
            bottom.x += offset;
        }
    }

    class HPoint{
        public float x;
        public float y;
        public PointF left = new PointF();
        public PointF right = new PointF();

        public void setY(float y){
            this.y = y;
            left.y = y;
            right.y = y;
        }

        public void adjustAllX(float offset){
            this.x += offset;
            left.x += offset;
            right.x += offset;
        }
    }

    private class MoveAnimation extends Animation {
        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t){
            super.applyTransformation(interpolatedTime, t);
            mInterpolatedTime = interpolatedTime;

            invalidate();
        }
    }

    public void startAnimation(){
        path.reset();
        mInterpolatedTime = 0;
        MoveAnimation moveAnimation = new MoveAnimation();
        moveAnimation.setDuration(50000);
        moveAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        startAnimation(moveAnimation);
    }

    @Override
    protected void onMeasure(int width, int height){
        super.onMeasure(width, height);
        int sizeWidth = MeasureSpec.getSize(width);
        int sizeHeight = MeasureSpec.getSize(height);
        int modeWidth = MeasureSpec.getSize(width);
//        int mode
    }

}
