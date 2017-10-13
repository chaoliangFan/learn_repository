package com.example.fanxh.httprequestapplication;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fanxh on 2017/10/11.
 */

public class HttpUtil {

    private static final int SHOW_RESPONSE = 1;
    private static Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SHOW_RESPONSE:
                    String response = (String) msg.obj;
                    //        viewHolder.newsImage.setImageBitmap(bitmap);
                    //                 newsImage.setImageBitmap(bitmap);
            }
        }
    };

    public static void sendHttpRequest(final String string, final HttpCallBackListener listener) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                InputStream inputStream = null;
                try {
                    URL url = new URL(string);
                    if (url != null) {
                        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                        httpURLConnection.setConnectTimeout(3000);
                        httpURLConnection.setRequestMethod("GET");
                        httpURLConnection.setDoInput(true);
                        int responseCode = httpURLConnection.getResponseCode();
                        Log.d("*****", "ssss" + responseCode);
                        if (responseCode == httpURLConnection.HTTP_OK) {
                            InputStream in = httpURLConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder respone = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                respone.append(line);
                            }
                            Log.d("************","11"+respone.toString());
                            if (listener != null){
                                listener.onFinish(respone.toString());
                            }
//                            Log.d("*******qqqqqqqqq", "hh");
//                            Message message = new Message();
//                            message.what = SHOW_RESPONSE;
//                            message.obj = respone;
//                            handler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    if (listener != null){
                        listener.onError(e);
                    }
                }
            }
        }).start();
    }

//    public static String sendHttpRequest(String address) {
//        //       String address = "http://tools.2345.com/frame/api/GetCalendarDay?token=d5e5ac4a4b3905a879de59eb5fb09ab5&channel=tqtest&platform=android&vn=1.6&vc=6&verAdjust=1.2&verFestival=7.0&verFesCommon=0.0&verTools=0.0";
//        HttpURLConnection httpURLConnection = null;
//        StringBuilder respone = new StringBuilder();
//        try {
//            URL url = new URL(address);
//            if (url != null) {
//                httpURLConnection = (HttpURLConnection) url.openConnection();
//                httpURLConnection.setRequestMethod("GET");
//                httpURLConnection.setConnectTimeout(3000);
//                httpURLConnection.setReadTimeout(3000);
//                httpURLConnection.setDoInput(true);
//                //               httpURLConnection.setDoOutput(true);
//                int responseCode = httpURLConnection.getResponseCode();
//                if (responseCode == httpURLConnection.HTTP_OK) {
//                    InputStream in = httpURLConnection.getInputStream();
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//                    respone = new StringBuilder();
//                    String line;
//                    while ((line = reader.readLine()) != null) {
//                        respone.append(line);
//                    }
//                }
//                Log.d("sssssssssssssssssss",respone.toString());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return respone.toString();
//    }
}
