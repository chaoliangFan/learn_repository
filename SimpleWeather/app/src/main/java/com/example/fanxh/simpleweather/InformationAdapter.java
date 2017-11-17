package com.example.fanxh.simpleweather;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by fanxh on 2017/11/17.
 */

public class InformationAdapter extends ArrayAdapter<Information> {
    private int resourceId;
    public InformationAdapter(Context context, int textViewResourceId, List<Information> objects) {
        super(context, textViewResourceId, objects);
        resourceId=textViewResourceId;
    }
    public View getView(int position, View convertView, ViewGroup parent){
        Information information=getItem(position);//获取当前项的Fruit实例
        View view= LayoutInflater.from(getContext()).inflate(resourceId,null);
        TextView mSystemTime = (TextView) view.findViewById(R.id.system_time);
        getSystemTime(mSystemTime);
        TextView mCity = (TextView) view.findViewById(R.id.city);
        mCity.setText(information.getCounty_name());
        if (!TextUtils.isEmpty(information.getStatus())) {
            TextView mDegrees = (TextView) view.findViewById(R.id.degrees);
            mDegrees.setText(information.getDegree());
            setBackGround(information.getStatus(), view);
        }
        return view;
    }

    public void getSystemTime(TextView mSystemTime) {
        if (mSystemTime != null) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
            String t = format.format(new Date());
            try {
                Date date = format.parse(t);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(date);
                int hour = calendar.get(Calendar.HOUR_OF_DAY);
                int minute = calendar.get(Calendar.MINUTE);
                String setMinute = "";
                if (minute < 10) {
                    setMinute = "0" + minute;
                } else {
                    setMinute = "" + minute;
                }
                if (hour < 12) {
                    mSystemTime.setText("上午" + hour + ":" + setMinute);
                } else if (hour == 12) {
                    mSystemTime.setText("下午" + hour + ":" + setMinute);
                } else {
                    mSystemTime.setText("下午" + (hour - 12) + ":" + setMinute);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    public static void setBackGround(String string, View view) {
        final String SUN = "晴";
        final String OVERCAST = "阴";
        final String CLOUDY = "多云";
        final String LIGHT_RAIN = "小雨";
        final String MODERATE_RAIN = "中雨";
        final String HEAVY_RAIN = "大雨";
        final String SHOWER_RAIN = "阵雨";
        final String THUNDERSHOWER = "雷阵雨";
        final String LIGHT_SNOW = "小雪";
        if (!TextUtils.isEmpty(string) && view != null) {
            switch (string) {

                case SUN:
                    view.setBackgroundResource(R.drawable.ic_choose_sun_bg);
                    break;
                case OVERCAST:
                    view.setBackgroundResource(R.drawable.ic_choose_overcast_bg);
                    break;
                case CLOUDY:
                    view.setBackgroundResource(R.drawable.ic_choose_cloudy_bg);
                    break;
                case LIGHT_RAIN:
                    view.setBackgroundResource(R.drawable.i_light_rain);
                    break;
                case MODERATE_RAIN:
                    view.setBackgroundResource(R.drawable.i_moderate_rain);
                    break;
                case HEAVY_RAIN:
                    view.setBackgroundResource(R.drawable.i_heavy_rain);
                    break;
                case SHOWER_RAIN:
                    view.setBackgroundResource(R.drawable.i_shower_rain);
                    break;
                case THUNDERSHOWER:
                    view.setBackgroundResource(R.drawable.i_thundershower);
                    break;
                case LIGHT_SNOW:
                    view.setBackgroundResource(R.drawable.i_light_snow);
                    break;
                default:
                    break;
            }
        }
    }
}
