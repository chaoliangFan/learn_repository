package com.example.fanxh.simpleweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanxh.simpleweather.gson.Hourly;
import com.example.fanxh.simpleweather.util.Utility;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fanxh on 2017/10/30.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private List<Hourly> mHourly_forecastList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mTime;
        ImageView mStatus;
        TextView mDegree;

        public ViewHolder(View view) {
            super(view);
            mTime = (TextView) view.findViewById(R.id.time);
            mStatus = (ImageView) view.findViewById(R.id.status);
            mDegree = (TextView) view.findViewById(R.id.degree);
        }
    }

    public HourlyForecastAdapter(List<Hourly> hourly_forecastList) {
        mHourly_forecastList = hourly_forecastList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(HourlyForecastAdapter.ViewHolder holder, int position) {
        Hourly mHourly_forecast = mHourly_forecastList.get(position);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date date = format.parse(mHourly_forecast.time);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            if (position == 0) {
                holder.mTime.setText("现在");
            } else if (hour < 10) {
                holder.mTime.setText("上午" + hour + "时");
            } else if (10 <= hour && hour < 12) {
                holder.mTime.setText("上午" + hour + "时");
            } else if (hour == 12) {
                holder.mTime.setText("下午12时");
            } else {
                holder.mTime.setText("下午" + (hour - 12) + "时");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Utility.setImage(mHourly_forecast.cond_txt,holder.mStatus);

        holder.mDegree.setText(mHourly_forecast.tmp + "°");
    }

    @Override
    public int getItemCount() {
        return mHourly_forecastList.size();
    }
}
