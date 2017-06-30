package com.umbrella.customView;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.umbrella.sharedemo.R;

/**
 * Created by Zhang.Y.C on 2017/6/13.
 */

public class BlankFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_POS = "post";

    private String param1;
    private int pos;


    public BlankFragment(){

    }

    public static BlankFragment newInstance(String param1, int param2){
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putInt(ARG_POS, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        if (getArguments() != null){
            param1 = getArguments().getString(ARG_PARAM1);
            pos = getArguments().getInt(ARG_POS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.custom_view_fragment_blank, container, false);
        ImageView imageView = (ImageView)view.findViewById(R.id.iv_fg);
        switch (pos){
            case 0:
                imageView.setImageResource(R.drawable.pos0);
                break;
            case 1:
                imageView.setImageResource(R.drawable.pos1);
                break;
            case 2:
                imageView.setImageResource(R.drawable.pos2);
                break;
            default:
                imageView.setImageResource(R.drawable.pos3);
                break;
        }
        return view;
    }

    public interface OnFragmentInteractionListener{
        void onFragmentInteraction(Uri uri);
    }
}
