package com.example.fanxh.httprequestapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by fanxh on 2017/10/12.
 */

public class FortunetellingAdapter extends RecyclerView.Adapter <FortunetellingAdapter.ViewHolder>{
    private List<FortunetellingBean> mFortunetellingBeanList;


    private RecyclerView mRecyclerView;
    /**
     * LruCache 缓存图片
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View fortunetellingView;

        TextView name;
        ImageView iconUrl;

        public ViewHolder(View view){
            super(view);


            fortunetellingView = view;

            name = (TextView) view.findViewById(R.id.fortunetelling_title);
            iconUrl = (ImageView) view.findViewById(R.id.fortunetelling_photo);
        }
    }


    public FortunetellingAdapter(List<FortunetellingBean> fortunetellingBeanList){
        mFortunetellingBeanList = fortunetellingBeanList;
        Log.d("########","1111111111");


        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        int cacheSize = maxMemory / 8;
        mMemoryCache = new LruCache<String, BitmapDrawable>(cacheSize) {
            @Override
            protected int sizeOf(String key, BitmapDrawable drawable) {
                return drawable.getBitmap().getByteCount();
            }
        };
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mRecyclerView == null) {
            mRecyclerView = (RecyclerView) parent;
        }

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fortunetelling_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);

        holder.fortunetellingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FortunetellingBean fortunetellingBean = mFortunetellingBeanList.get(position);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(fortunetellingBean.getUrl()));
                MyApplication.getContext().startActivity(intent);
            }
        });
        holder.iconUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                FortunetellingBean fortunetellingBean = mFortunetellingBeanList.get(position);


                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(fortunetellingBean.getUrl()));
                MyApplication.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
    FortunetellingBean fortunetellingBean = mFortunetellingBeanList.get(position);
        Log.d("########","22222222222");
        holder.name.setText(fortunetellingBean.getName());
        holder.iconUrl.setTag(fortunetellingBean.getIconUrl());
        Log.d("########","33333333333");
        BitmapDrawable drawable = getBitmapFromMemoryCache(fortunetellingBean.getIconUrl());
        Log.d("########","4444444444");
        if (drawable != null) {
            holder.iconUrl.setImageDrawable(drawable);
            Log.d("########","555555555555");
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(fortunetellingBean.getIconUrl());
            Log.d("########","6666666666666666666");
        }
        holder.name.setText(fortunetellingBean.getName());
    }
    @Override
    public int getItemCount() {
        return mFortunetellingBeanList.size();
    }

    /**
     * 将一张图片存储到LruCache中
     */
    public void addBitmapToMemoryCache(String key, BitmapDrawable drawable) {
        if (getBitmapFromMemoryCache(key) == null) {
            mMemoryCache.put(key, drawable);
        }
    }

    /**
     * 从LruCache中去一张图片
     */
    public BitmapDrawable getBitmapFromMemoryCache(String key) {
        return mMemoryCache.get(key);
    }

    /**
     * 异步下载图片
     */
    class BitmapWorkerTask extends AsyncTask<String, Void, BitmapDrawable> {

        String imageUrl;

        @Override
        protected BitmapDrawable doInBackground(String... params) {
            imageUrl = params[0];
            // 在后台开始下载图片
            Bitmap bitmap = getImageViewInputStream(imageUrl);
            BitmapDrawable drawable = new BitmapDrawable(MyApplication.getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = (ImageView) mRecyclerView.findViewWithTag(imageUrl);
            if (imageView != null && drawable != null) {
                imageView.setImageDrawable(drawable);
            }
        }
    }

    /**
     * 建立http请求，获取bitmap
     */
    public Bitmap getImageViewInputStream(String string) {
        InputStream inputStream = null;
        Bitmap bitmap = null;
        try {
            URL url = new URL(string);
            if (url != null) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                int responseCode = httpURLConnection.getResponseCode();
                Log.d("########","HTTP_OK"+responseCode);
                if (responseCode == httpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.d("########","图片请求完成");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

}
