package com.example.administrator.appupdate;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import java.io.File;

/**
 * Created by fanxh on 2017/10/20.
 */

public class AppUpdataManger {
    private static DownloadManager downloadManager;
    private Context mContext;
    public static long mTaskId;
    private String versionName;
    private ProgressDialog progressDidog;

    public AppUpdataManger(Context context) {
        this.mContext = context;
    }

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();
            mContext.unregisterReceiver(receiver);
        }
    };

    public void downloadAPK(String versionUrl, String versionName) {
        showProgressDialog();
        this.versionName = versionName;
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, versionName);
        mTaskId = downloadManager.enqueue(request);
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    public void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    closeProgressDialog();
                    AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
                    dialog.setTitle("下载完成");
                    dialog.setMessage("是否立即安装");
                    dialog.setCancelable(true);
                    dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            installAPK(mTaskId);
                        }
                    });
                    dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    dialog.show();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
        cursor.close();
    }

    public void installAPK(long appId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        install.addCategory(Intent.CATEGORY_DEFAULT);
        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 24) {
            File apkPath = new File(Environment.getExternalStorageDirectory(), "Android/data/com.example.administrator.appupdate/files/Download");
            File newFile = new File(apkPath, "my-toolsm_top.apk");
            Uri contentUri = FileProvider.getUriForFile(mContext, "com.example.administrator.appupdate.file_provider", newFile);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Uri downloadFileUri = downloadManager.getUriForDownloadedFile(appId);
            install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        }
        mContext.startActivity(install);
    }

    private void showProgressDialog() {
        if (progressDidog == null) {
            progressDidog = new ProgressDialog(mContext);
            progressDidog.setMessage("正在下载...");
            progressDidog.setCanceledOnTouchOutside(false);
        }
        progressDidog.show();
    }

    private void closeProgressDialog() {
        if (progressDidog != null) {
            progressDidog.dismiss();
        }
    }
}
