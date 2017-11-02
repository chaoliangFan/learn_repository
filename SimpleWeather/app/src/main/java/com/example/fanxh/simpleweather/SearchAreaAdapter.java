package com.example.fanxh.simpleweather;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

/**
 * Created by fanxh on 2017/11/2.
 */

public class SearchAreaAdapter extends ArrayAdapter<InformationBean> {
    private int resourceId;

    public SearchAreaAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<InformationBean> objects) {
        super(context,textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        InformationBean mInformationBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
        TextView mSystemTime = (TextView) view.findViewById(R.id.system_time);
        TextView mCity = (TextView)view.findViewById(R.id.city);
        TextView mDegrees = (TextView)view.findViewById(R.id.degrees);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String t=format.format(new Date());
        mSystemTime.setText(t);
        mCity.setText(mInformationBean.getCity());
        mDegrees.setText(mInformationBean.getDegrees());

        return view;
    }
}
