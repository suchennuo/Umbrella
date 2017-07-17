package com.umbrella.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;

import com.flipImage.Flip3dAnimation;
import com.flipImage.Flip3dAnimationListener;
import com.umbrella.Utils.AESCipher;
import com.umbrella.sharedemo.R;

import java.util.Timer;
import java.util.TimerTask;

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

    private static int duration = 2000;
    private Timer timer;
    private Message message;
    private static int START_ANIMATION = 1;

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message message){
            if (message.what == START_ANIMATION) {
                frontView.startAnimation(flip3dAnimation);
            }
        }

    };

    @OnClick(R.id.bt_3dflip_flip)
    public void flip(View view){
//        if (isFront){
//            applyRotation(0, 360);
//        } else {
//            applyRotation(0, -90);
//        }
//        isFront = !isFront;
        applyRotation(0, 360);
        timerTask = new TimerTask() {
            @Override
            public void run() {
                message = Message.obtain();
                message.what = START_ANIMATION;
                handler.sendMessage(message);
            }
        };

        timer = new Timer();
        timer.schedule(timerTask, 1000, 5000);

        //NOTE: Only the original thread that create a view hierarchy can touch its views.


        //AES 加解密
        try {
            String encryptStr = AESCipher.aesEncryptString("中国-China", KEY);
            Log.d(TAG, "encrypt str " + encryptStr);
            String decryptStr = AESCipher.aesDecryptString(encryptStr, KEY);
            Log.d(TAG, "decrypt str " + decryptStr);
        } catch (Exception e){

        }
    }
    private static final String TAG = "Flip3dActivity";
    public static String KEY = "5kjsmr*,ljsJwyrs";

    Flip3dAnimation flip3dAnimation;
    private void applyRotation(float start, float end){
        float centerX = frontView.getWidth() / 2.0f;
        float centerY = frontView.getHeight() / 2.0f;

        flip3dAnimation = new Flip3dAnimation(start, end, centerX, centerY,
                frontView, R.drawable.search_brain_power, R.drawable.search_chinese_composition_pressed);
        flip3dAnimation.setDuration(3000);
        flip3dAnimation.setFillAfter(true);
        flip3dAnimation.setInterpolator(new BounceInterpolator());//(new AccelerateInterpolator());
    }



    private TimerTask timerTask;


    @Override
    protected void onResume(){
        super.onResume();

    }

    @Override
    protected void onPause(){
        super.onPause();
        if (timerTask != null) {
            timerTask.cancel();
            frontView.clearAnimation();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

}
