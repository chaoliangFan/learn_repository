package com.example.fanxh.weixin_and_news;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity {

    private List<Data> DataList = new ArrayList<Data>();
    private List<NewsData> newsDataList = new ArrayList<NewsData>();
    private final static String fileName = "data.json";
    private ListView newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }
        String str = (String) getJson(fileName,NewsActivity.this);
        parseJSONWithGSON(str);
        NewsAdapter adapter = new NewsAdapter(this,R.layout.news_item,newsDataList);
        newsList = (ListView) findViewById(R.id.news_show);
        newsList.setAdapter(adapter);
        newsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                NewsData newsData = newsDataList.get(i);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(newsData.getNewsUrl()));
                startActivity(intent);
            }
        });
    }

    private void parseJSONWithGSON(String jsonData){
        Gson gson = new Gson();
        // ArrayList<Data> dataList =  new ArrayList<Data>();
        List<Data> DataList = gson.fromJson(jsonData,new TypeToken<List<Data>>(){}.getType());
        for (Data news : DataList){
            NewsData newsData = new NewsData();
          //  Log.d("MainActivity","icon is "+ news.getTitle());
            newsData.setNewsImgUrl(news.getImgUrl());
            newsData.setNewsTitle(news.getTitle());
            newsData.setNewsDetails(news.getDetails());
            newsData.setNewsUrl(news.getUrl());
          //  Log.d("MainActivity","icon is "+ newsData.getNewsTitle());
            newsDataList.add(newsData);
        }
    }

    public static String getJson(String fileName,Context context) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            AssetManager assetManager = context.getAssets();
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(
                    assetManager.open(fileName)));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
