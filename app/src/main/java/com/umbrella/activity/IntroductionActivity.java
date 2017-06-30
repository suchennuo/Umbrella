package com.umbrella.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.GridLayoutAnimationController;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.floattingButton.FloatBall;
import com.floattingButton.FloatBallMenu;
import com.umbrella.customView.DropIndication;
import com.umbrella.customView.FloatBubbleView;
import com.umbrella.customView.FloatButtonManager;
import com.umbrella.customView.PasswordView;
import com.umbrella.sharedemo.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class IntroductionActivity extends AppCompatActivity {

    private static final  String TAG = "IntroductionActivity";
    @BindView(R.id.bt_click)
    Button btClick;

    @BindView(R.id.password_view)
    PasswordView passwordView;

    @BindView(R.id.bt_submit)
    Button btSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);

        ButterKnife.bind(this);

        initView();
        setListener();
        addFloatButton();

        initPasswordView();
    }



    private void initPasswordView(){
        passwordView.setMode(PasswordView.Mode.RECT);
        passwordView.setPasswordListener(new PasswordView.PasswordListener() {
            @Override
            public void passwordChange(String changeText) {

            }

            @Override
            public void passwordComplete() {

            }

            @Override
            public void keyEnterPress(String password, boolean isComplete) {

            }
        });
    }


    /**
     * @param view
     */
    private static final int n = 3;
    long[] clicks = new long[n];

    @OnClick(R.id.bt_click)
    public void btClick(View view){
        System.arraycopy(clicks, 1, clicks, 0, clicks.length - 1);
        clicks[clicks.length - 1] = SystemClock.uptimeMillis();
        if (clicks[0] >= (SystemClock.uptimeMillis() - 2000)){
            //Do something.
            Log.d(TAG, "bt click");
        }
    }



    private void initView(){
    }

    private void setListener(){

    }

    private void addFloatButton(){

        FloatBallMenu menu = new FloatBallMenu();
        FloatBall.SingleIcon singleIcon = new FloatBall.SingleIcon(R.drawable.floatball2, 1f, 0.3f);

        floatBall = new FloatBall.Builder(getApplicationContext()).menu(menu).icon(singleIcon).build();
        floatBall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(IntroductionActivity.this, "click", Toast.LENGTH_SHORT).show();
            }
        });

        floatBall.setLayoutGravity(Gravity.LEFT | Gravity.CENTER_VERTICAL);
    }

    @Override
    protected void onStart(){
        super.onStart();
        floatBall.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        floatBall.dismiss();
    }

    private FloatBall floatBall;

    @Override
    protected void onPause(){
        super.onPause();
    }


    /**
     * 显示隐藏 app
     * 调用 processOperation(1|2);
     * @param mask
     */
    private void processOperation(int mask){
        btSubmit.setVisibility((mask & 0x1) == 0x1 ? View.VISIBLE : View.GONE);
        btClick.setVisibility((mask & 0x2) == 0x2 ? View.VISIBLE : View.GONE);
    }
}
