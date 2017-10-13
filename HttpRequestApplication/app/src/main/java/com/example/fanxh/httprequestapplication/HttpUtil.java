package com.example.fanxh.httprequestapplication;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by fanxh on 2017/10/11.
 */

public class HttpUtil {

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
                        if (responseCode == httpURLConnection.HTTP_OK) {
                            InputStream in = httpURLConnection.getInputStream();
                            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                            StringBuilder respone = new StringBuilder();
                            String line;
                            while ((line = reader.readLine()) != null) {
                                respone.append(line);
                            }
                            if (listener != null){
                                listener.onFinish(respone.toString());
                            }
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
}
