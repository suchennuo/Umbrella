package com.umbrella.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umbrella.adapter.NormalListViewAdapter;
import com.umbrella.sharedemo.R;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        UMShareAPI.get(this);

        initShare();
        setClickListener();
    }


    private UMWeb umWeb;
    private String targetUrl;
    private String title;
    private String description;
    private UMImage thumb;

    private RelativeLayout refreshLayout;
    private ImageView refreshIcon;
    private TextView refreshTips;

//    private RelativeLayout loadMoreLayout;
    private ImageView loadMoreImg;
//    private TextView loadMoreTips;

    protected void initShare(){
        UMShareConfig umShareConfig = new UMShareConfig();
        umShareConfig.isNeedAuthOnGetUserInfo(true);

        UMShareAPI.get(this).setShareConfig(umShareConfig);

        umWeb = new UMWeb(targetUrl);
        umWeb.setTitle(title);
        umWeb.setThumb(thumb);
        umWeb.setDescription(description);
        initData();

        ((FloatingActionButton)findViewById(R.id.fb_jump)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(MainActivity.this, CollapsingToolbarActivity.class));
                startActivity(new Intent(MainActivity.this, IntroductionActivity.class));
            }
        });

        listView = (ListView)findViewById(R.id.lv_main);
        loadMoreImg = (ImageView) findViewById(R.id.load_more_icon);

        View headerView = getLayoutInflater().inflate(R.layout.item_header, null);
        listView.addHeaderView(headerView);
        listView.setAdapter(new NormalListViewAdapter(this, data));


        refreshLayout = (RelativeLayout) headerView.findViewById(R.id.refresh_layout);
        refreshIcon = (ImageView) headerView.findViewById(R.id.refresh_icon);

        initRotateAnimation();


        //TODO：需要状态控制，刷新的时候不能显示加载更多， 反之亦然 !
        //TODO: 当列表条目少的时候，只有在条目上滑动，才会触发 SCROLL_STATE_IDLE !
        //TODO: 频繁操作

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d(TAG, "scrollState " + scrollState);
                switch (scrollState){
                    case SCROLL_STATE_IDLE:
                        isFooterVisible = (view.getLastVisiblePosition() == view.getCount() -  1);
                        if (isFooterVisible) {
                            lastMotionY = 0;
                            Log.d(TAG, "last visible position " + view.getLastVisiblePosition() + " count " + view.getCount());
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Log.d(TAG, "visibleItemCount " + visibleItemCount + " total " + totalItemCount);
            }
        });


        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (isLoading || isLoadMore){
                    Log.d(TAG, "Eat event");
                    return true;
                }
                action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:

                        currentTopMarginY = (int)(event.getY() - lastMotionY) / 2; //为了有下拉的感觉
                        // 通过偏移下拉头的topMargin值，来实现下拉效果
                        Log.d(TAG, "absmargin " + currentTopMarginY);
                        if (currentTopMarginY > 0 && ! isLoading && !isLoadMore) {
                            setTopMargin(currentTopMarginY);
                        }

                        if (isFooterVisible && currentTopMarginY < 0 && !isLoading && !isLoadMore) {
                            Log.d(TAG, "Bottom margin " + currentTopMarginY);
                            setBottomMargin(Math.abs(currentTopMarginY));
                        }

                        break;
                    case MotionEvent.ACTION_DOWN:

                        lastMotionY = initialMotionY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isFooterVisible && currentTopMarginY < 0 && !isLoading && ! isLoadMore){
                            new LoadMoreTask().execute();
                        } else if (! isLoading && !isLoadMore){
                            new RefreshTask().execute();
                        }
                }
                return false;
            }
        });

    }

    private static String TAG = "MainActivity";

    int FIXED_TOPMARGIN = 150;

    int action;
    float lastMotionY, initialMotionY;
    int currentTopMarginY;
    boolean isFooterVisible, isLoadMore;

    private ViewGroup.MarginLayoutParams marginLayoutParams;

    private void setTopMargin(int topMargin){
        marginLayoutParams = (ViewGroup.MarginLayoutParams)refreshLayout.getLayoutParams();
        marginLayoutParams.topMargin = topMargin;
        refreshLayout.setLayoutParams(marginLayoutParams);
    }

    private void setBottomMargin(int bottomMargin){
        marginLayoutParams = (ViewGroup.MarginLayoutParams)listView.getLayoutParams();
        marginLayoutParams.bottomMargin = bottomMargin;
        listView.setLayoutParams(marginLayoutParams);
    }



    private void startRotateAnimation(View rotateIcon){
        if ( ! hasRotate) {
            rotateIcon.startAnimation(rotateAnimation);
            hasRotate = true;
        }
    }

    private void stopRotateAnimation(View rotateIcon){
        if (hasRotate){
            rotateIcon.clearAnimation();
            hasRotate = false;
        }
    }

    private RotateAnimation rotateAnimation;
    private boolean hasRotate;
    private void initRotateAnimation(){
        rotateAnimation = new RotateAnimation(0, 720, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(1200);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setRepeatMode(Animation.RESTART);
    }

    private ListView listView;
    private List<String> data;
    private void initData(){
        data = new ArrayList<>();
        for (int i = 0; i < 20; ++i){
            data.add("index " + i);
        }
    }

    private boolean isLoading = false;

    class LoadMoreTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            currentTopMarginY = Math.abs(currentTopMarginY);
            if (currentTopMarginY >= FIXED_TOPMARGIN){
                smoothSetBottomMargin(currentTopMarginY, FIXED_TOPMARGIN, LOADING_SMOOTH_UP);
                currentTopMarginY = FIXED_TOPMARGIN;
                startRotateAnimation(loadMoreImg);
                isLoadMore = true;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (currentTopMarginY >= FIXED_TOPMARGIN){
                sleep(2000);
            }
            publishProgress(0);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            smoothSetBottomMargin(currentTopMarginY, 0, LOADING_DISMISS);
            stopRotateAnimation(loadMoreImg);
            isLoadMore = false;
        }
    }


    class RefreshTask extends AsyncTask<Void, Integer, Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (currentTopMarginY >= FIXED_TOPMARGIN){
                smoothSetTopMargin(currentTopMarginY, FIXED_TOPMARGIN, LOADING_SMOOTH_UP);
                currentTopMarginY = FIXED_TOPMARGIN;
                startRotateAnimation(refreshIcon);
                isLoading = true;
            }
        }

        @Override
        protected Void doInBackground(Void... params) {
            if (currentTopMarginY >= FIXED_TOPMARGIN){
                sleep(2000);
            }
            publishProgress(0);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
            smoothSetTopMargin(currentTopMarginY, 0, LOADING_DISMISS);
            stopRotateAnimation(refreshIcon);
            isLoading = false;
        }
    }

    private void sleep(long time){
        try{
            Thread.sleep(time);
        } catch (InterruptedException exception){

        }
    }

    private ValueAnimator valueAnimator;
    private long LOADING_SMOOTH_UP = 500;
    private long LOADING_DISMISS = 300;

    private void smoothSetTopMargin(int startMargin, int endMargin, long duration){
        valueAnimator = ValueAnimator.ofInt(startMargin, endMargin);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setTopMargin((int)animation.getAnimatedValue());
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }

    private void smoothSetBottomMargin(int startMargin, int endMargin, long duration){
        valueAnimator = ValueAnimator.ofInt(startMargin, endMargin);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setBottomMargin((int)animation.getAnimatedValue());
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.start();
    }


    /**** Share model ***/
    private void setClickListener(){
//        findViewById(R.id.bt_share).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                new ShareAction(MainActivity.this)
//                        .setCallback(new ShareCallback())
////                        .setDisplayList(SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
//                        .setPlatform(SHARE_MEDIA.QQ)
//                        .withMedia(umWeb)
//                        .share();
//            }
//        });
    }


    /*
    低端手机跳转到 QQ 等 授权页面 后， 系统回收 app 分享页面，回调会失败，这样可以防杀死，
    一般不需要设置
     */
    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        UMShareAPI.get(this).onSaveInstanceState(outState);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        UMShareAPI.get(this).release();
    }


    class ShareCallback implements UMShareListener{
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onResult(SHARE_MEDIA share_media) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media) {

        }
    }


    class AuthCallback implements UMAuthListener{
        @Override
        public void onStart(SHARE_MEDIA share_media) {

        }

        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {

        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    }


}
