package com.umbrella.activity;

import android.graphics.Color;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.umbrella.adapter.NormalRecyclerViewAdapter;
import com.umbrella.sharedemo.R;

import java.util.ArrayList;
import java.util.List;

public class CollapsingToolbarActivity extends AppBaseActivity {


    private ListView listView;

    private RecyclerView recyclerView;

    private List<String> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//       getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS); //拖动过程中状态栏透明
        setContentView(R.layout.activity_collapsing_toolbar);

//        initData();
//
//        listView = (ListView)findViewById(R.id.lv_main);
//        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, data));

        recyclerView = (RecyclerView)findViewById(R.id.lv_main);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        recyclerView.setAdapter(new NormalRecyclerViewAdapter(this));

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout)findViewById(R.id.ctl_toolbar);
        collapsingToolbarLayout.setTitle("Java body building");
        collapsingToolbarLayout.setExpandedTitleColor(Color.RED);
        collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);


    }



    private void initData(){
        data = new ArrayList<>();
        for (int i = 0; i < 20; ++i){
            data.add("index " + i);
        }
    }
}
