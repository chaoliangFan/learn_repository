package com.example.fanxh.weixin_and_news;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

/**
 * Created by fanxh on 2017/10/8.
 */

public class NewsAdapter extends ArrayAdapter {
    private int resourceId;

    public NewsAdapter(@NonNull Context context,int textViewResourceId, @NonNull List objects) {
        super(context,textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        NewsData newsData = (NewsData) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,null);
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
        TextView newsDetailsText = (TextView) view.findViewById(R.id.news_details);

        Log.d("NewsAdapter",newsData.getNewsImgUrl());

        loadImage(newsImage,newsData.getNewsImgUrl());
       // newsImage.setImageResource(newsData.getNewsImgUrl());
        newsTitleText.setText(newsData.getNewsTitle());
        newsDetailsText.setText(newsData.getNewsDetails());

        return view;
    }
    private void loadImage(ImageView imageView,String string){
  //      ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        try {
            URL url = new URL(string);
            imageView.setImageBitmap(BitmapFactory.decodeStream(url.openStream()));
        }catch (Exception e){
            e.printStackTrace();
        }
        Log.d("NewsAdapter",string);
    }
}
