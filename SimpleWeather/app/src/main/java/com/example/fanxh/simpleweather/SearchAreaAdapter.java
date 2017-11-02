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
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        InformationBean mInformationBean = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        TextView mSystemTime = (TextView) view.findViewById(R.id.system_time);
        TextView mCity = (TextView) view.findViewById(R.id.city);
        TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
        String t = format.format(new Date());
        if (Time(t) < 10) {
            mSystemTime.setText("上午" + t.substring(12, 16));
        } else if (10 <= Time(t) && Time(t) < 12) {
            mSystemTime.setText("上午" + t.substring(11, 16));
        } else if (Time(t) == 12) {
            mSystemTime.setText("下午12时");
        } else {
            mSystemTime.setText("下午" + Integer.toString(Time(t) - 12) + t.substring(13, 16));
        }
        mCity.setText(mInformationBean.getCity());
        mDegrees.setText(mInformationBean.getDegrees() + "°");
        switch (mInformationBean.getStatus()) {
            case "晴":
                view.setBackgroundResource(R.drawable.ic_choose_sun_bg);
                break;
            case "阴":
                view.setBackgroundResource(R.drawable.ic_choose_overcast_bg);
                break;
            case "多云":
                view.setBackgroundResource(R.drawable.ic_choose_cloudy_bg);
                break;
            case "小雨":
                view.setBackgroundResource(R.drawable.i_light_rain);
                break;
            case "中雨":
                view.setBackgroundResource(R.drawable.i_moderate_rain);
                break;
            case "大雨":
                view.setBackgroundResource(R.drawable.i_heavy_rain);
                break;
            case "阵雨":
                view.setBackgroundResource(R.drawable.i_shower_rain);
                break;
            default:
                break;
        }
        return view;
    }

    public int Time(String string) {
        String str = string;
        String detailedTime = str.substring(11, 13);
        return Integer.parseInt(detailedTime);
    }
}
