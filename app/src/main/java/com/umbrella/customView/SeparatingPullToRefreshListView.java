package com.umbrella.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.umbrella.sharedemo.R;

/**
 * Created by Zhang.Y.C on 2017/5/12.
 */

public class SeparatingPullToRefreshListView extends FrameLayout{
    private Context context;
    public SeparatingPullToRefreshListView(Context context) {
        super(context);
        init(context, null);
    }

    public SeparatingPullToRefreshListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SeparatingPullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private static String TAG = "PullToRefreshListView";

    public void addHeader(View header, int headerHeight){
        if (header == null){
            return;
        }
        int heightPx = dp2px(headerHeight);
        Log.d(TAG, "header height " + heightPx);
        if (heightPx < dp2px(50)){ //下拉刷新的空间
            throw new IllegalArgumentException("Header height must be bigger than 150px");
        }

        //设置 header  的 margin top  hold place.
        holdPlace.getLayoutParams().height = heightPx - FRESH_HEIGHT;
        ((ViewGroup)headView).addView(header);

        listView.addHeaderView(headView);
    }


    public void setAdapter(ListAdapter adapter){
        listView.setAdapter(adapter);
    }

    private View headView;
    private View holdPlace;
    private View rootView;
    private View fackFooter;
    private ListView listView;

    private int FRESH_HEIGHT = dp2px(50);

    private void init(Context context, AttributeSet attrs){

        this.context = context;
        rootView = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_layout, this); //null

        fackFooter = rootView.findViewById(R.id.load_more_layout);
        loadMoreImg = (ImageView) rootView.findViewById(R.id.load_more_icon);

        listView = (ListView)rootView.findViewById(R.id.lv_main);

        headView = LayoutInflater.from(context).inflate(R.layout.item_header, null);
        holdPlace = headView.findViewById(R.id.hold_place);
        refreshLayout = (RelativeLayout) headView.findViewById(R.id.refresh_layout);
        refreshIcon = (ImageView) headView.findViewById(R.id.refresh_icon);

        initRotateAnimation();
        initListView();
    }

    private RelativeLayout refreshLayout;
    private ImageView refreshIcon;
    private TextView refreshTips;

    //    private RelativeLayout loadMoreLayout;
    private ImageView loadMoreImg;
//    private TextView loadMoreTips;

    private void initListView(){

        //TODO：需要状态控制，刷新的时候不能显示加载更多， 反之亦然 !
        //TODO: 当列表条目少的时候，只有在条目上滑动，才会触发 SCROLL_STATE_IDLE !
        //TODO: 频繁操作

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.d(TAG, "scrollState " + scrollState);
                switch (scrollState){
                    case SCROLL_STATE_IDLE:
                        isBottom = (view.getLastVisiblePosition() == view.getCount() -  1);
                        if (isBottom) {
                            lastMotionY = 0;
                            Log.d(TAG, "last visible position " + view.getLastVisiblePosition() + " count " + view.getCount());
                        } else {
                        }
                        break;
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });



        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:

                        currentMarginY = (int)(event.getY() - lastMotionY) / 2; //为了有下拉的感觉
                        // 通过偏移下拉头的topMargin值，来实现下拉效果
                        Log.d(TAG, "current margin Y " + currentMarginY);
                        if (isPrepareRefresh()) {
                            Log.d(TAG, "set top margin ");
                            setTopMargin(currentMarginY);
                        }

                        if (isPrepareLoadMore()) {
                            Log.d(TAG, "set load more ");
                            setBottomMargin(Math.abs(currentMarginY));
                        }
                        break;
                    case MotionEvent.ACTION_DOWN:

//                        lastMotionY = initialMotionY = event.getY();
//                        Log.d (TAG, "last motion Y " + lastMotionY);
                        break;
                    case MotionEvent.ACTION_UP:
                        if (isPrepareLoadMore()){
                            prePullUpLoadMore();
                        } else if (isPrepareRefresh()){
                            prePullDownRefresh();
                        }
                        break;
                }
                return false;
            }
        });
    }


    @Override
    public boolean onInterceptTouchEvent(MotionEvent event){

        if (isLoadMore || isLoading){
            return true;
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN){
            lastMotionY = event.getY();
        }
        return super.onInterceptTouchEvent(event);
    }


    private boolean isTop = true;
    
    private boolean isPrepareRefresh(){
        isTop = listView.getChildCount() == 0 || listView.getChildAt(0).getTop() == 0;

        return isTop && currentMarginY > 0 && !isLoading && !isLoadMore;
    }
    
    private boolean isPrepareLoadMore(){
        return isBottom && currentMarginY < 0 && !isLoading && !isLoadMore;
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


    int action;
    float lastMotionY, initialMotionY;
    int currentMarginY;
    boolean isBottom, isLoadMore;
    int FIXED_TOPMARGIN = 150;

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



    public void prePullDownRefresh(){
        if (currentMarginY >= FIXED_TOPMARGIN) {
            Log.d(TAG, "start refreshing.");
            smoothSetTopMargin(currentMarginY, FIXED_TOPMARGIN, LOADING_SMOOTH_UP);
            currentMarginY = FIXED_TOPMARGIN;
            startRotateAnimation(refreshIcon);
            isLoading = true;
            listView.setEnabled(false);
            if (pullToRefreshInterface != null){
                pullToRefreshInterface.PullUpRefresh();
            }
        } else {
            smoothSetTopMargin(currentMarginY, 0, LOADING_DISMISS);
        }
    }

    public void completeRefresh(){
        if ( ! isLoading){
            return;
        }
        Log.d(TAG, "stop refresh.");
        smoothSetTopMargin(currentMarginY, 0, LOADING_DISMISS);
        stopRotateAnimation(refreshIcon);
        isLoading = false;
        listView.setEnabled(true);
    }

    public void prePullUpLoadMore(){

        currentMarginY = Math.abs(currentMarginY);

        if (currentMarginY >= FIXED_TOPMARGIN){
            Log.d(TAG, "start load more.");
            smoothSetBottomMargin(currentMarginY, FIXED_TOPMARGIN, LOADING_SMOOTH_UP);
            currentMarginY = FIXED_TOPMARGIN;
            startRotateAnimation(loadMoreImg);
            isLoadMore = true;
            listView.setEnabled(false);

            if (pullToRefreshInterface != null){
                pullToRefreshInterface.PullDownLoadMore();
            }

        } else {
            smoothSetBottomMargin(currentMarginY, 0, LOADING_DISMISS);
        }
    }

    public void completeLoadMore(){
        if (! isLoadMore ){
            return;
        }
        Log.d(TAG, "stop load more.");
        smoothSetBottomMargin(currentMarginY, 0, LOADING_DISMISS);
        stopRotateAnimation(loadMoreImg);
        isLoadMore = false;

        //FIXME: is bottom 判断的有问题
        isBottom = false;
        currentMarginY = 0;

        listView.setEnabled(true);
    }

    private boolean isLoading = false;

    public interface PullToRefreshInterface{
        void PullUpRefresh();
        void PullDownLoadMore();
    }

    public PullToRefreshInterface pullToRefreshInterface;

    public void addPullToRefreshListener(PullToRefreshInterface listener){
        this.pullToRefreshInterface = listener;
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
    private int dp2px(int dp){
        Resources resources = getResources();
        return (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }



}
