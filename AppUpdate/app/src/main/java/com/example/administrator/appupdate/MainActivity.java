package com.example.administrator.appupdate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String UPDATEURL = "http://update.app.2345.com/index.php";
    private InformationData informationData = new InformationData();
    ;
    private AppUpdataManger appUpdataManger;
    private LinearLayout mVersionTest;
    private TextView mShowVersionName;
    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mShowDialog(msg.what);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        mShowVersionName = (TextView) findViewById(R.id.version_name);
        mShowVersionName.setText(APKVersionCodeUtils.getVerName(this));
        mVersionTest = (LinearLayout) findViewById(R.id.version_test);
        mVersionTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendHttpRequest(UPDATEURL, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        parseJsonWithJsonObject(response);
                        if (!TextUtils.isEmpty(informationData.getNeed_update())) {
                            Message message = new Message();
                            message.what = 1;
                            handler.sendMessage(message);
                        } else {
                            Message message = new Message();
                            message.what = 2;
                            handler.sendMessage(message);
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        Toast.makeText(MainActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void parseJsonWithJsonObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            informationData.setAppkey(jsonObject.getString("appkey"));
            informationData.setChannel(jsonObject.getString("channel"));
            informationData.setDownurl(jsonObject.getString("downurl"));
            informationData.setPackname(jsonObject.getString("packname"));
            informationData.setFilename(jsonObject.getString("filename"));
            informationData.setFilesize(jsonObject.getString("filesize"));
            informationData.setMd5(jsonObject.getString("md5"));
            informationData.setVersion(jsonObject.getString("version"));
            informationData.setUser_version(jsonObject.getString("user_version"));
            informationData.setUpdatelog(jsonObject.getString("updatelog"));
            informationData.setUpdatetype(jsonObject.getString("updatetype"));
            informationData.setNeed_update(jsonObject.getString("need_pudate"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void mShowDialog(int msgWhat) {
        if (msgWhat == 1) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("当前已是最新版本");
            dialog.setCancelable(true);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        } else {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("新版本更新");
            dialog.setMessage(informationData.getUpdatelog());
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    appUpdataManger = new AppUpdataManger(MainActivity.this);
                    appUpdataManger.downloadAPK(informationData.getDownurl(), informationData.getFilename());
                }
            });
            dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
            dialog.show();
        }
    }
}

