package com.devstories.anipointcompany.android.base;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Iterator;
import java.util.Map;

public class HttpClient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

//         System.out.println("GET : " + Config.url + url + "?" + params);

        if (params != null) {
            client.get(Config.url + url, params, responseHandler);
        } else {
            client.get(Config.url + url, responseHandler);
        }
    }


    public static void get(String url, Map<String, String> headers, AsyncHttpResponseHandler responseHandler) {

//         System.out.println("GET : " + Config.url + url + "?" + params);

        client.setTimeout(60 * 1000);

        if(headers != null) {
            Iterator keys = headers.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = headers.get(key);

                client.addHeader(key, value);
            }
        }

        client.get(url, responseHandler);

    }

    public static void get(String url, Map<String, String> headers, RequestParams params, AsyncHttpResponseHandler responseHandler) {

//         System.out.println("GET : " + Config.url + url + "?" + params);

        client.setTimeout(60 * 1000);

        if(headers != null) {
            Iterator keys = headers.keySet().iterator();
            while (keys.hasNext()) {
                String key = (String) keys.next();
                String value = headers.get(key);

                client.addHeader(key, value);
            }
        }

        if (params != null) {
            client.get(url, params, responseHandler);
        } else {
            client.get(url, responseHandler);
        }
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {

        System.out.println("POST : " + Config.url + url + "?" + params);

        client.post(Config.url + url, params, responseHandler);
    }

    public static void cancelALL() {
        client.cancelAllRequests(true);
    }
}
