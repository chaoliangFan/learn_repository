package com.example.fanxh.wannl;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener{
    private ImageView titleBack;
    private RelativeLayout statement;
    private RelativeLayout webVersion;
    private RelativeLayout messageBoard;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        titleBack = ( ImageView) findViewById(R.id.back);
        titleBack.setOnClickListener(this);
        webVersion = (RelativeLayout) findViewById(R.id.web_version);
        webVersion.setOnClickListener(this);
        statement = (RelativeLayout) findViewById(R.id.statement);
        statement.setOnClickListener(this);
        messageBoard = (RelativeLayout) findViewById(R.id.message_board);
        messageBoard.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.back:
                finish();
                break;
            case R.id.web_version:
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
                startActivity(intent);
                break;
            case R.id.statement:
                Intent intent1 = new Intent(AboutUsActivity.this,StatementActivity.class);
                startActivity(intent1);
                break;
            case R.id.message_board:
                Intent intent2 = new Intent(AboutUsActivity.this,MessageBoardActivity.class);
                startActivity(intent2);
                break;
            default:
                break;
        }
    }
}
