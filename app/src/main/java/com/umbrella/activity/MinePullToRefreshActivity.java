package com.umbrella.activity;

import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.umbrella.customView.SeparatingPullToRefreshListView;
import com.umbrella.adapter.NormalListViewAdapter;
import com.umbrella.sharedemo.R;

import java.util.ArrayList;
import java.util.List;

public class MinePullToRefreshActivity extends AppBaseActivity {

    private SeparatingPullToRefreshListView pullToRefreshListView;
    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_pull_to_refresh);

        initData();
        initListView();
    }

    private void initData() {
        data = new ArrayList<>();
        for (int i = 0; i < 10; ++i) {
            data.add("custom ptr index " + i);
        }
    }

    private NormalListViewAdapter adapter;

    private void initListView() {
        pullToRefreshListView = (SeparatingPullToRefreshListView) findViewById(R.id.ptr_main);
        View headView = getLayoutInflater().inflate(R.layout.test_ptr_header, null);
        pullToRefreshListView.addHeader(headView, 100); //100dp
        adapter = new NormalListViewAdapter(this, data);
        pullToRefreshListView.setAdapter(adapter);
        pullToRefreshListView.addPullToRefreshListener(new SeparatingPullToRefreshListView.PullToRefreshInterface() {
            @Override
            public void PullUpRefresh() {
                new RefreshTask().execute();
            }

            @Override
            public void PullDownLoadMore() {
                new LoadMoreTask().execute();
            }
        });
    }

    private static int loadTimes = 1;

    public final static String TAG = "MinePullToRefresh";
    class RefreshTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "refresh in background.");
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
//            publishProgress(0);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {
//            pullToRefreshListView.completeRefresh();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "post refresh.");
            pullToRefreshListView.completeRefresh();
        }
    }


    class LoadMoreTask extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (Exception e) {

            }
//            publishProgress(0);
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... topMargin) {

        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pullToRefreshListView.completeLoadMore();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (data != null && adapter != null) {
                        for(int i = 0; i < 5; ++i){
                            data.add("load more " + loadTimes++);
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
            }, 500);
        }

    }
}
