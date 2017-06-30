package com.umbrella.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.umbrella.sharedemo.R;

import java.util.List;

/**
 * Created by Zhang.Y.C on 2017/5/20.
 */

public class NormalListViewAdapter extends BaseAdapter{
    private Context context;
    private List<String> data;

    public NormalListViewAdapter(Context context, List<String> data){
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    static class SimpleViewHolder {
        TextView tvItem;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        SimpleViewHolder viewHolder = null;
        if (convertView == null){
            viewHolder = new SimpleViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.item_text, parent, false);
            viewHolder.tvItem = (TextView)convertView.findViewById(R.id.tv_item);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (SimpleViewHolder)convertView.getTag();
        }
        viewHolder.tvItem.setText(data.get(position));

        viewHolder.tvItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return convertView;
    }
}
