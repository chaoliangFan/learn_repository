package com.example.fanxh.simpleweather;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class SearchAreaActivity extends AppCompatActivity {
    private List<InformationBean> mInformationBeanList = new ArrayList<>();
    private Button mChooseArea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_area);
        mChooseArea = (Button) findViewById(R.id.choose_area);
        mChooseArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchAreaActivity.this, ChangeArea.class);
                startActivity(intent);
            }
        });


        SearchAreaAdapter mSearchAreaAdapter = new SearchAreaAdapter(SearchAreaActivity.this,R.layout.search_area_item,mInformationBeanList);
        ListView mSearchArea = (ListView)findViewById(R.id.search_area);
        mSearchArea.setAdapter(mSearchAreaAdapter);



    }
}
