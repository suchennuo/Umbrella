package com.umbrella.customView;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zhang.Y.C on 2017/6/13.
 */

public class DropView extends View{
    public DropView(Context context) {
        this(context, null);
    }

    public DropView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DropView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
