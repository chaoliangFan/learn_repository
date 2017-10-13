package com.example.fanxh.httprequestapplication;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private final String API = "http://tools.2345.com/frame/api/GetCalendarDay?token=d5e5ac4a4b3905a879de59eb5fb09ab5&channel=tqtest&platform=android&vn=1.6&vc=6&verAdjust=1.2&verFestival=7.0&verFesCommon=0.0&verTools=0.0";
    private List<FortunetellingBean> fbList = new ArrayList<FortunetellingBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }


//        HttpUtil.sendHttpRequest(API);
//        parseJsonWithJsonObject(HttpUtil.sendHttpRequest(API));
        HttpUtil.sendHttpRequest(API, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                parseJsonWithJsonObject( response);
            }

            @Override
            public void onError(Exception e) {
                Log.d("EEEEEEEEEEEEEEEEE","AAAAAAAA");
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fortunetelling_show);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        FortunetellingAdapter adapter = new FortunetellingAdapter(fbList);
        recyclerView.setAdapter(adapter);
    }



  //  private void parseJsonWithJsonObject(String jsonDate){
        private void parseJsonWithJsonObject(String jsonData){

        try {
            JSONObject jsonObject1 = new JSONObject(jsonData).getJSONObject("tools");
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FortunetellingBean fortunetellingBean = new FortunetellingBean();
                fortunetellingBean.setName(jsonObject.getString("name"));
                fortunetellingBean.setUrl(jsonObject.getString("url"));
                Log.d("*****************","***"+ fortunetellingBean.getUrl());
                fortunetellingBean.setIconUrl(jsonObject.getString("iconUrl"));
                fbList.add(fortunetellingBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
