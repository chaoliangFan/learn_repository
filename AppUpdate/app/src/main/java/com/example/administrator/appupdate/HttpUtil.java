package com.example.administrator.appupdate;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Administrator on 2017/10/19.
 */

public class HttpUtil {
    public static void sendHttpRequest(final String string, final Callback listener) {
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
        client.newCall(request).enqueue(listener);
    }
}
