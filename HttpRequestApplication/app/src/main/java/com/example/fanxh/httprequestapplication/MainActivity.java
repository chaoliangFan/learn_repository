package com.example.fanxh.httprequestapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String CONNECTION = "http://tools.2345.com/frame/api/GetCalendarDay?token=d5e5ac4a4b3905a879de59eb5fb09ab5&channel=tqtest&platform=android&vn=1.6&vc=6&verAdjust=1.2&verFestival=7.0&verFesCommon=0.0&verTools=0.0";
    private List<FortunetellingBean> fbList = new ArrayList<FortunetellingBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.hide();
        }

        SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
        String jsonData = pref.getString("json","");
        if (jsonData != ""){
            parseJsonWithJsonObject(jsonData);
        }else {
            HttpUtil.sendHttpRequest(CONNECTION, new HttpCallBackListener() {
                @Override
                public void onFinish(String response) {
                    parseJsonWithJsonObject(response);
                }

                @Override
                public void onError(Exception e) {
                }
            });
        }
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.fortunetelling_show);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
            FortunetellingAdapter adapter = new FortunetellingAdapter(fbList);
            recyclerView.setAdapter(adapter);
    }
        private void parseJsonWithJsonObject(String jsonData){
        try {
            JSONObject jsonObject1 = new JSONObject(jsonData).getJSONObject("tools");
            JSONArray jsonArray = jsonObject1.getJSONArray("data");
            for (int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                FortunetellingBean fortunetellingBean = new FortunetellingBean();
                fortunetellingBean.setName(jsonObject.getString("name"));
                fortunetellingBean.setUrl(jsonObject.getString("url"));
                fortunetellingBean.setIconUrl(jsonObject.getString("iconUrl"));
                fbList.add(fortunetellingBean);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
