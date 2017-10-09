package com.example.fanxh.weixin_and_news;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Wei_XinActivity extends AppCompatActivity implements View.OnClickListener{
    private RelativeLayout layout_personal_information;
    private RelativeLayout layout_wallet;
    private RelativeLayout layout_collect;
    private RelativeLayout layout_photo;
    private RelativeLayout layout_card_bag;
    private RelativeLayout layout_expression;
    private RelativeLayout layout_set;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wei__xin);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        layout_personal_information = (RelativeLayout) findViewById(R.id.layout_personal_information);
        layout_wallet = (RelativeLayout) findViewById(R.id.layout_wallet);
        layout_collect = (RelativeLayout) findViewById(R.id.layout_collect);
        layout_photo = (RelativeLayout) findViewById(R.id.layout_photo);
        layout_card_bag = (RelativeLayout) findViewById(R.id.layout_card_bag);
        layout_expression = (RelativeLayout) findViewById(R.id.layout_expression);
        layout_set = (RelativeLayout) findViewById(R.id.layout_set);
        layout_personal_information.setOnClickListener(this);
        layout_wallet.setOnClickListener(this);
        layout_collect.setOnClickListener(this);
        layout_photo.setOnClickListener(this);
        layout_card_bag.setOnClickListener(this);
        layout_expression.setOnClickListener(this);
        layout_set.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.layout_personal_information:
                Toast.makeText(this,R.string.personal_information,Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_wallet:
                Toast.makeText(this,R.string.wallet,Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_collect:
                Toast.makeText(this,R.string.collect,Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_photo:
                Toast.makeText(this,R.string.photo,Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_card_bag:
                Toast.makeText(this,R.string.card_bag,Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_expression:
                Toast.makeText(this,R.string.expression,Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_set:
                Toast.makeText(this,R.string.set,Toast.LENGTH_SHORT).show();
                break;
        }

    }
}
