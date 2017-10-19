package com.example.administrator.appupdate;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final String CONNECTION = "http://update.app.2345.com/index.php";
    //  private List<InformationDate> idList = new ArrayList<>();
    private InformationDate informationDate;
    private Button versionTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        HttpUtil.sendHttpRequest(CONNECTION, new HttpCallBackListener() {
            @Override
            public void onFinish(String response) {
                parseJsonWithJsonObject(response);
            }

            @Override
            public void onError(Exception e) {
            }
        });


        String versionCode = String.valueOf(APKVersionCodeUtils.getVersionCode(this));
        if (versionCode.equals(informationDate.getVersion())) {
            Toast.makeText(this, R.string.check, Toast.LENGTH_SHORT).show();
        } else {
            //       ?????;
        }

    }


    private void parseJsonWithJsonObject(String jsonData) {
        try {
//            JSONArray jsonArray = new JSONArray(jsonData);
//            for (int i = 0; i < jsonArray.length(); i++) {
//                JSONObject jsonObject = jsonArray.getJSONObject(i);
            JSONObject jsonObject = new JSONObject(jsonData);
            //               InformationDate informationDate = new InformationDate();
            informationDate.setAppkey(jsonObject.getString("appkey"));
            informationDate.setChannel(jsonObject.getString("channel"));
            informationDate.setDownurl(jsonObject.getString("downurl"));
            informationDate.setPackname(jsonObject.getString("packname"));
            informationDate.setFilename(jsonObject.getString("filename"));
            informationDate.setFilesize(jsonObject.getString("filesize"));
            informationDate.setMd5(jsonObject.getString("md5"));
            informationDate.setVersion(jsonObject.getString("version"));
            informationDate.setUser_version(jsonObject.getString("user_version"));
            informationDate.setUpdatelog(jsonObject.getString("updatelog"));
            informationDate.setUpdatetype(jsonObject.getString("updatetype"));
            informationDate.setNeed_update(jsonObject.getString("need_pudate"));
            //               idList.add(informationDate);
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
