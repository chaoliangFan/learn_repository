package com.example.fanxh.weixin_and_news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.INotificationSideChannel;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static android.R.attr.bitmap;

/**
 * Created by fanxh on 2017/10/8.
 */

public class NewsAdapter extends ArrayAdapter {
    private int resourceId;
    private static final int SHOW_RESPONSE = 1;
    private ImageView newsImage;
    ViewHolder viewHolder;
    Bitmap bitmap;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    bitmap = (Bitmap) msg.obj;
                    viewHolder.newsImage.setImageBitmap(bitmap);
                    //                 newsImage.setImageBitmap(bitmap);
            }
        }
    };

    public NewsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<NewsData> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsData newsData = (NewsData) getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, null);
            viewHolder = new ViewHolder();
            viewHolder.newsImage = (ImageView) view.findViewById(R.id.news_image);
            viewHolder.newsTitleText = (TextView) view.findViewById(R.id.news_title);
            viewHolder.newsDetailsText = (TextView) view.findViewById(R.id.news_details);
            view.setTag(viewHolder);
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag();
        }
//if (getImageViewInputStream(newsData.getImgUrl()))
        getImageViewInputStream(newsData.getIcon());
//        viewHolder.newsImage.setImageBitmap(bitmap);
        viewHolder.newsTitleText.setText(newsData.getTitle());
        viewHolder.newsDetailsText.setText(newsData.getDesc());
        return view;
    }

    class ViewHolder {
        ImageView newsImage;
        TextView newsTitleText;
        TextView newsDetailsText;
    }

    public void getImageViewInputStream(final String string) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                Bitmap bitmap = null;
                try {
                    URL url = new URL(string);
                    if (url != null) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setConnectTimeout(3000);
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setDoInput(true);
                        int responseCode = httpURLConnection.getResponseCode();
                        Log.d("*****", "ssss" + responseCode);
                        if (responseCode == httpURLConnection.HTTP_OK) {
                            inputStream = httpURLConnection.getInputStream();
                            bitmap = BitmapFactory.decodeStream(inputStream);
                            Log.d("*******qqqqqqqqq", "hh");
                            Message message = new Message();
                            message.what = SHOW_RESPONSE;
                            message.obj = bitmap;
                            handler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}
