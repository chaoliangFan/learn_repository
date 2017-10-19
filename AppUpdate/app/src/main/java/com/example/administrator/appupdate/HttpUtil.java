package com.example.administrator.appupdate;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Administrator on 2017/10/19.
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
                        httpURLConnection.setRequestMethod("POST");
                        httpURLConnection.setRequestProperty("appkey","53ba6f0ce981c77f15abb76b9ecdd979");
                        httpURLConnection.setRequestProperty("authkey","2018lzxslcmarrsuccesshappdorlverymaa");
                        httpURLConnection.setRequestProperty("channel","tqtest");
                        httpURLConnection.setRequestProperty("version","7");
                        httpURLConnection.setRequestProperty("user_version","0.1.7");
                        httpURLConnection.setRequestProperty("old_md5","6ebd29287fd14e6efb50a1922f71cbdf");
                        httpURLConnection.setRequestProperty("type","update");
                        httpURLConnection.setRequestProperty("sign","VVVTBV9TVAFQBABSBwYBVVcJXgFVUwwLUw9XBVQBVA8=");

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
