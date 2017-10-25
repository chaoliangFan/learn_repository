package com.example.administrator.appupdate;

import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
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

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;
import java.security.MessageDigest;

public class MainActivity extends AppCompatActivity {
    private static final String UPDATEURL = "http://update.app.2345.com/index.php";
    private InformationData mInformationData;
    ;
    private AppUpdataManger appUpdataManger;
    private LinearLayout mVersionTest;
    private TextView mShowVersionName;
    private Handler receiveMessage = new Handler() {
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
                ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connectionManager.getActiveNetworkInfo();
                if (networkInfo == null || !networkInfo.isAvailable()) {
                    Toast.makeText(MainActivity.this, "当前无网络，请先检查网络", Toast.LENGTH_SHORT).show();
                } else {
                    HttpUtil.sendHttpRequest(UPDATEURL, new HttpCallBackListener() {
                        @Override
                        public void onFinish(String response) {
                            parseJsonWithJsonObject(response);
                            if (!TextUtils.isEmpty(mInformationData.getNeed_update())) {
                                Message message = new Message();
                                message.what = 1;
                                receiveMessage.sendMessage(message);
                            } else {
                                Message message = new Message();
                                message.what = 2;
                                receiveMessage.sendMessage(message);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            Toast.makeText(MainActivity.this, "服务器无响应", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    private void parseJsonWithJsonObject(String jsonData) {
        try {
            JSONObject jsonObject = new JSONObject(jsonData);
            mInformationData = new InformationData();
            mInformationData.setAppkey(jsonObject.getString("appkey"));
            mInformationData.setChannel(jsonObject.getString("channel"));
            mInformationData.setDownurl(jsonObject.getString("downurl"));
            mInformationData.setPackname(jsonObject.getString("packname"));
            mInformationData.setFilename(jsonObject.getString("filename"));
            mInformationData.setFilesize(jsonObject.getString("filesize"));
            mInformationData.setMd5(jsonObject.getString("md5"));
            mInformationData.setVersion(jsonObject.getString("version"));
            mInformationData.setUser_version(jsonObject.getString("user_version"));
            mInformationData.setUpdatelog(jsonObject.getString("updatelog"));
            mInformationData.setUpdatetype(jsonObject.getString("updatetype"));
            mInformationData.setNeed_update(jsonObject.getString("need_pudate"));
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
            dialog.setMessage(mInformationData.getUpdatelog());
            dialog.setCancelable(false);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    File downLoadFile = new File(getSystemFilePath(MainActivity.this));
                    if (getDirMD5(downLoadFile, mInformationData.getMd5())) {
                        Toast.makeText(MainActivity.this, "本地存在", Toast.LENGTH_SHORT).show();
//                        Intent install = new Intent(Intent.ACTION_VIEW);
//                        install.setDataAndType( Uri.fromFile(downLoadFile), "application/vnd.android.package-archive");
//                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(install);
                    } else {
                        appUpdataManger = new AppUpdataManger(MainActivity.this);
                        appUpdataManger.downloadAPK(mInformationData.getDownurl(), mInformationData.getFilename());
                    }
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

    public static String getFileMD5(File file) {
        if (!file.isFile()) {
            return null;
        }
        MessageDigest digest = null;
        FileInputStream in = null;
        byte buffer[] = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * @param dir               下载目录
     * @param toDownloadFileMD5
     * @return
     */
    public boolean getDirMD5(File dir, String toDownloadFileMD5) {
        boolean isSameFile = false;
        if (!dir.isDirectory()) {
            isSameFile = false;
        } else {
            String md5;
            File files[] = dir.listFiles();
            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                if (f.isDirectory()) {
                    getDirMD5(f, toDownloadFileMD5);
                } else {
                    md5 = getFileMD5(f);
                    if (md5.equals(toDownloadFileMD5)) {
                        isSameFile = true;
                    }
                }
            }
        }
        return isSameFile;
    }

    private static String getSystemFilePath(Context context) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } else {
            cachePath = context.getFilesDir().getAbsolutePath();
        }
        return cachePath;
    }
}

