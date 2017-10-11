package com.example.fanxh.weixin_and_news;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button weChat;
    private Button news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        weChat = (Button) findViewById(R.id.wei_xin);
        weChat.setOnClickListener(this);
        news = (Button) findViewById(R.id.news);
        news.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.wei_xin:
                Intent intent = new Intent(this, WeChatActivity.class);
                startActivity(intent);
                break;
            case R.id.news:
                Intent intent1 = new Intent(this, NewsActivity.class);
                startActivity(intent1);
        }
    }
}
