package com.example.administrator.appupdate;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private final String CONNECTION = "http://update.app.2345.com/index.php";
    private final String DOWNURL = "http://download.app.2345.com/calendar2345/auto/12/my-toolsm_top.apk?12";
    private static InformationData informationData = new InformationData();
    private AppUpdataManger appUpdataManger;
    private LinearLayout versionTest;
    private TextView versionName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        versionName = (TextView) findViewById(R.id.version_name);
        versionName.setText(APKVersionCodeUtils.getVerName(this));
        versionTest = (LinearLayout) findViewById(R.id.version_test);
        versionTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpUtil.sendHttpRequest(CONNECTION, new HttpCallBackListener() {
                    @Override
                    public void onFinish(String response) {
                        parseJsonWithJsonObject(response);
                    }

                    @Override
                    public void onError(Exception e) {
                    }
                });
                String versionCode = String.valueOf(APKVersionCodeUtils.getVersionCode(MainActivity.this));
                Log.d("********", "取值成功" + informationData.getVersion());
                if (versionCode.equals(informationData.getVersion())) {
                    Toast.makeText(MyApplication.getContext(), R.string.check, Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MyApplication.getContext());
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
                            appUpdataManger.downloadAPK(informationData.getDownurl(), "versionName");
//                    appUpdataManger.downloadAPK(informationData.getDownurl(), informationData.getFilename());
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
            Log.d("********", "取值成功" + jsonObject.getString("filesize"));
            informationData.setMd5(jsonObject.getString("md5"));
            informationData.setVersion(jsonObject.getString("version"));
            informationData.setUser_version(jsonObject.getString("user_version"));
            informationData.setUpdatelog(jsonObject.getString("updatelog"));
            informationData.setUpdatetype(jsonObject.getString("updatetype"));
            informationData.setNeed_update(jsonObject.getString("need_pudate"));
            Log.d("********", " 取值成功" + informationData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
