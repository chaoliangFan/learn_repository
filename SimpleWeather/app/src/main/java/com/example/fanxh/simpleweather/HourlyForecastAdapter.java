package com.example.fanxh.simpleweather;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fanxh.simpleweather.gson.Hourly_forecast;

import java.util.List;

/**
 * Created by fanxh on 2017/10/30.
 */

public class HourlyForecastAdapter extends RecyclerView.Adapter<HourlyForecastAdapter.ViewHolder> {

    private List<Hourly_forecast> mHourly_forecastList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        TextView mTime;
        ImageView mStatus;
        TextView mDegree;
        public ViewHolder(View view){
            super(view);
            mTime = (TextView) view.findViewById(R.id.time);
            mStatus = (ImageView)view.findViewById(R.id.status);
            mDegree = (TextView)view.findViewById(R.id.degree);
        }
    }
    public HourlyForecastAdapter(List<Hourly_forecast> hourly_forecastList){
        mHourly_forecastList = hourly_forecastList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hourly_forecast_item,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    public void onBindViewHolder(HourlyForecastAdapter.ViewHolder holder, int position) {
        Hourly_forecast mHourly_forecast = mHourly_forecastList.get(position);

        if (position == 0){
            holder.mTime.setText("现在");
        }else if (Time(mHourly_forecast.date)<10){
            holder.mTime.setText("上午"+ mHourly_forecast.date.substring(12, 13) + "时");
        }else if (10 <= Time(mHourly_forecast.date) && Time(mHourly_forecast.date) < 12) {
            holder.mTime.setText("上午"+ mHourly_forecast.date.substring(11, 13) + "时");
        }else if(Time(mHourly_forecast.date) == 12) {
            holder.mTime.setText("下午12时");
        }else {
            holder.mTime.setText("下午"+Integer.toString(Time(mHourly_forecast.date)-12)+"时");
        }
        switch (mHourly_forecast.cond.txt){

             case "晴":
                 holder.mStatus.setImageResource(R.drawable.i_sun);
                 break;
             case "阴" :
                 holder.mStatus.setImageResource(R.drawable.i_overcast);
                 break;
             case "多云" :
                 holder.mStatus.setImageResource(R.drawable.i_cloudy);
                 break;
             case "小雨":
                 holder.mStatus.setImageResource(R.drawable.i_light_rain);
                 break;
             case "中雨":
                 holder.mStatus.setImageResource(R.drawable.i_moderate_rain);
                 break;
             case "大雨":
                 holder.mStatus.setImageResource(R.drawable.i_heavy_rain);
                 break;
             case "阵雨":
                 holder.mStatus.setImageResource(R.drawable.i_shower_rain);
                 break;
             case "雷阵雨":
                 holder.mStatus.setImageResource(R.drawable.i_thundershower);
                 break;
             case "小雪":
                 holder.mStatus.setImageResource(R.drawable.i_light_snow);
                 break;
                 default:
         }

        holder.mDegree.setText(mHourly_forecast.tmp+"°");
    }

    @Override
    public int getItemCount() {
        return mHourly_forecastList.size();
    }

    public int Time(String string) {
        String str = string;
        String detailedTime = str.substring(11, 13);
        return Integer.parseInt(detailedTime);
    }

}
