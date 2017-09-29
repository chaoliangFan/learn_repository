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

public class AboutUsActivity extends AppCompatActivity{
    //private Button button;
    private ImageView imageView,imageView1,imageView4,imageView5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        ActionBar actionBar=getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
//        button=(Button)findViewById(R.id.url);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent(Intent.ACTION_VIEW);
//                intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
//                startActivity(intent);
//            }
//        });

        imageView=(ImageView) findViewById(R.id.back);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        imageView1=(ImageView) findViewById(R.id.go_on2);
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
                startActivity(intent);
            }
        });
        imageView4=(ImageView) findViewById(R.id.go_on4);
        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(AboutUsActivity.this,StatementActivity.class);
                startActivity(intent1);
            }
        });

        imageView5=(ImageView) findViewById(R.id.go_on5);
        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                switch (imageView.getId()){
//                    case R.id.back:
//                        finish();
//                        break;
//                    case R.id.go_on2:
//                        Intent intent=new Intent(Intent.ACTION_VIEW);
//                        intent.setData(Uri.parse("http://tools.2345.com/m/rili.htm"));
//                        startActivity(intent);
//                        break;
//                    case R.id.go_on4:
//                        Intent intent1=new Intent(AboutUsActivity.this,StatementActivity.class);
//                        startActivity(intent1);
//                        break;
//                    case R.id.go_on5:
                        Intent intent2=new Intent(AboutUsActivity.this,MessageBoardActivity.class);
                        startActivity(intent2);
//                        break;
//                    default:
//                        break;
//                }
            }
        });
    }
}
