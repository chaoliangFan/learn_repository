package com.example.fanxh.weixin_and_news;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by fanxh on 2017/10/8.
 */

public class NewsAdapter extends ArrayAdapter {
    private ListView mListView;
//    private ImageView newsImage;

    /**
     * LruCache 缓存图片
     */
    private LruCache<String, BitmapDrawable> mMemoryCache;

    public NewsAdapter(@NonNull Context context, int textViewResourceId, @NonNull List<NewsData> objects) {
        super(context, textViewResourceId, objects);
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
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (mListView == null) {
            mListView = (ListView) parent;
        }
        NewsData newsData = (NewsData) getItem(position);
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.news_item, null);
        } else {
            view = convertView;
        }
        ImageView newsImage = (ImageView) view.findViewById(R.id.news_image);
        TextView newsTitleText = (TextView) view.findViewById(R.id.news_title);
        TextView newsDetailsText = (TextView) view.findViewById(R.id.news_details);
        newsImage.setTag(newsData.getIcon());
        BitmapDrawable drawable = getBitmapFromMemoryCache(newsData.getIcon());
        if (drawable != null) {
            newsImage.setImageDrawable(drawable);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask();
            task.execute(newsData.getIcon());
        }
        newsTitleText.setText(newsData.getTitle());
        newsDetailsText.setText(newsData.getDesc());
        return view;
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
            BitmapDrawable drawable = new BitmapDrawable(getContext().getResources(), bitmap);
            addBitmapToMemoryCache(imageUrl, drawable);
            return drawable;
        }

        @Override
        protected void onPostExecute(BitmapDrawable drawable) {
            ImageView imageView = (ImageView) mListView.findViewWithTag(imageUrl);
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
                httpURLConnection.setConnectTimeout(3000);
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setDoInput(true);
                int responseCode = httpURLConnection.getResponseCode();
                Log.d("*****", "ssss" + responseCode);
                if (responseCode == httpURLConnection.HTTP_OK) {
                    inputStream = httpURLConnection.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
