package com.umbrella.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.umbrella.sharedemo.R;

/**
 * Created by Zhang.Y.C on 2017/5/20.
 */

public class NormalRecyclerViewAdapter extends RecyclerView.Adapter<NormalRecyclerViewAdapter.NormalTextViewHolder>{

    private Context context;

    private LayoutInflater layoutInflater;

    private String[] titles;

    public NormalRecyclerViewAdapter(Context context){
        titles = context.getResources().getStringArray(R.array.languages);
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }


    @Override
    public NormalTextViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new NormalTextViewHolder(layoutInflater.inflate(R.layout.item_text, parent, false));
    }

    @Override
    public void onBindViewHolder(NormalTextViewHolder holder, int position) {
        holder.textView.setText(titles[position]);
    }

    @Override
    public int getItemCount() {
        return titles == null ? 0 : titles.length;
    }

    public static class NormalTextViewHolder extends RecyclerView.ViewHolder{

        protected TextView textView;


        NormalTextViewHolder(View view){
            super(view);
            textView = (TextView) view.findViewById(R.id.tv_item);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }


}
