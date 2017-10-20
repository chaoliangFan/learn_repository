package com.example.administrator.appupdate;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/10/19.
 */

public class HttpUtil {
    public static void sendHttpRequest(final String string, final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    RequestBody requestBody = new FormBody.Builder()
                            .add("appkey", "53ba6f0ce981c77f15abb76b9ecdd979")
                            .add("authkey", "2018lzxslcmarrsuccesshappdorlverymaa")
                            .add("channel", "tqtest")
                            .add("version", "7")
                            .add("user_version", "0.1.7")
                            .add("old_md5", "6ebd29287fd14e6efb50a1922f71cbdf")
                            .add("type", "update")
                            .add("sign", "VVVTBV9TVAFQBABSBwYBVVcJXgFVUwwLUw9XBVQBVA8=")
                            .build();
                    Request request = new Request.Builder()
                            .url(string)
                            .post(requestBody)
                            .build();
                    Response response = client.newCall(request).execute();
                    Log.d("********", "      链接成功");
                    String responseDate = response.body().string();
                    SharedPreferences.Editor editor = MyApplication.getContext().getSharedPreferences("data", Context.MODE_PRIVATE).edit();
                    editor.putString("json",responseDate);
                    editor.commit();
                    if (listener != null) {
                        listener.onFinish(responseDate);
                    }
                } catch (Exception e) {
                    if (listener != null) {
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }
}


// new Thread(new Runnable() {
//@Override
//public void run() {
//        InputStream inputStream = null;
//        try {
//        URL url = new URL(string);
//        if (url != null) {
//        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
//        httpURLConnection.setConnectTimeout(3000);
//        httpURLConnection.setDoOutput(true);
//        httpURLConnection.setRequestMethod("POST");
//        httpURLConnection.setRequestProperty("appkey","53ba6f0ce981c77f15abb76b9ecdd979");
//        httpURLConnection.setRequestProperty("authkey","2018lzxslcmarrsuccesshappdorlverymaa");
//        httpURLConnection.setRequestProperty("channel","tqtest");
//        httpURLConnection.setRequestProperty("version","7");
//        httpURLConnection.setRequestProperty("user_version","0.1.7");
//        httpURLConnection.setRequestProperty("old_md5","6ebd29287fd14e6efb50a1922f71cbdf");
//        httpURLConnection.setRequestProperty("type","update");
//        httpURLConnection.setRequestProperty("sign","VVVTBV9TVAFQBABSBwYBVVcJXgFVUwwLUw9XBVQBVA8=");
//        httpURLConnection.setDoInput(true);
//        int responseCode = httpURLConnection.getResponseCode();
//        Log.d("********","      链接成功"+responseCode);
//        if (responseCode == httpURLConnection.HTTP_OK) {
//        InputStream in = httpURLConnection.getInputStream();
//        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//        StringBuilder respone = new StringBuilder();
//        String line;
//        while ((line = reader.readLine()) != null) {
//        respone.append(line);
//        }
//        if (listener != null){
//        listener.onFinish(respone.toString());
//        }
//        }
//        }
//        } catch (Exception e) {
//        if (listener != null){
//        listener.onError(e);
//        }
//        }
//        }
//        }).start();
