package com.example.administrator.appupdate;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

/**
 * Created by fanxh on 2017/10/20.
 */

public class AppUpdataManger {
    private DownloadManager downloadManager;
    private Context mContext;
    private long mTaskId;
    private String versionName;
    public static int i = 0;

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

    //使用系统下载器下载
    public void downloadAPK(String versionUrl, String versionName) {
        this.versionName = versionName;
        //将下载请求加入下载队列
        downloadManager = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
        //创建下载任务
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(versionUrl));
        request.setAllowedOverRoaming(false);//漫游网络是否可以下载
        //设置文件类型，可以在下载结束后自动打开该文件
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeString = mimeTypeMap.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(versionUrl));
        request.setMimeType(mimeString);//加入任务队列
        //在通知栏显示，默认就是显示的
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedOverRoaming(false);
        request.setVisibleInDownloadsUi(true);
//       sdcard的目录下的download文件夹，必须设置
//       request.setDestinationInExternalPublicDir("/sdcard/download", versionName);
//       自定义路径的方法
        request.setDestinationInExternalFilesDir(mContext, Environment.DIRECTORY_DOWNLOADS, versionName);
        //加入下载列后会给该任务返回一个long型的id,
        //通过该id可以取消任务，重启任务等等
        mTaskId = downloadManager.enqueue(request);
        //注册广播接收，监听下载状态
        mContext.registerReceiver(receiver, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_SUCCESSFUL:
                    Toast.makeText(mContext, "下载完成", Toast.LENGTH_LONG).show();
                    i =i +1;
                    //打开文件进行安装
                    installAPK(mTaskId);
                    break;
                case DownloadManager.STATUS_FAILED:
                    Toast.makeText(mContext, "更新失败", Toast.LENGTH_LONG).show();
                    break;
            }
        }
        cursor.close();
    }

    //下载到本地后执行安装根据获得的id进行安装
    protected void installAPK(long appId) {
        Intent install = new Intent(Intent.ACTION_VIEW);
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(appId);
        install.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(install);
        i=0;
    }
}