package com.example.fanxh.simpleweather;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by fanxh on 2017/11/16.
 */

public class ListViewForScorllView extends ListView {
    public ListViewForScorllView(Context context) {
        super(context);
    }
    public ListViewForScorllView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    public ListViewForScorllView(Context context, AttributeSet attrs,
                                 int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    /**
     * 重写该方法，达到使ListView适应ScrollView的效果
     */
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}

