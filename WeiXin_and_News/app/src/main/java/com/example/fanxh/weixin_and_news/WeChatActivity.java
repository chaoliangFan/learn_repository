package com.example.fanxh.weixin_and_news;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class WeChatActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout layoutPersonalInformation;
    private RelativeLayout layoutWallet;
    private RelativeLayout layoutCollect;
    private RelativeLayout layoutPhoto;
    private RelativeLayout layoutCardBag;
    private RelativeLayout layoutExpression;
    private RelativeLayout layoutSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        layoutPersonalInformation = (RelativeLayout) findViewById(R.id.layout_personal_information);
        layoutWallet = (RelativeLayout) findViewById(R.id.layout_wallet);
        layoutCollect = (RelativeLayout) findViewById(R.id.layout_collect);
        layoutPhoto = (RelativeLayout) findViewById(R.id.layout_photo);
        layoutCardBag = (RelativeLayout) findViewById(R.id.layout_card_bag);
        layoutExpression = (RelativeLayout) findViewById(R.id.layout_expression);
        layoutSetting = (RelativeLayout) findViewById(R.id.layout_set);
        layoutPersonalInformation.setOnClickListener(this);
        layoutWallet.setOnClickListener(this);
        layoutCollect.setOnClickListener(this);
        layoutPhoto.setOnClickListener(this);
        layoutCardBag.setOnClickListener(this);
        layoutExpression.setOnClickListener(this);
        layoutSetting.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_personal_information:
                Toast.makeText(this, R.string.personal_information, Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_wallet:
                Toast.makeText(this, R.string.wallet, Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_collect:
                Toast.makeText(this, R.string.collect, Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_photo:
                Toast.makeText(this, R.string.photo, Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_card_bag:
                Toast.makeText(this, R.string.card_bag, Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_expression:
                Toast.makeText(this, R.string.expression, Toast.LENGTH_SHORT).show();
                break;
            case R.id.layout_set:
                Toast.makeText(this, R.string.set, Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
