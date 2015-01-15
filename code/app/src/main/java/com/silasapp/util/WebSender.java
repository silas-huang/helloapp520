package com.silasapp.util;


import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.util.Log;

/**
 * Created by Silas on 2015/1/12.
 */
public class WebSender {
    /**
     * 网络通信类(已优化)
     */

        /*s
         * 比较原始的 POST Cookies
         */
    public static String[] sendPost(String urlStr, String[] param,
                                    String[] value, String cookies) {
        String[] result = {"", ""};
        String paramValue = "";
        StringBuffer buffer = null;
        HttpURLConnection con = null;

        try {
            URL url = new URL(urlStr);
            con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            // con.setRequestProperty("Content-Type", "text/html");

            // set cookies
            if (!cookies.equals("") && cookies != null) {
                con.setRequestProperty("cookie", cookies);
            }

            paramValue = WebSender.getParamStr(param, value);

            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setConnectTimeout(50000);// 设置连接主机超时（单位：毫秒）
            con.setReadTimeout(50000);// 设置从主机读取数据超时（单位：毫秒）

            con.connect();// connect open

            PrintWriter out = new PrintWriter(con.getOutputStream());// 发送数据
            out.print(paramValue);
            out.flush();
            out.close();

            // get cookies
            String cks = con.getHeaderField("set-cookie");
            if (cks != "" && cks != null)
                result[1] = cks;

            // get status
            int res = 0;
            res = con.getResponseCode();

            // get info response
            if (res == 200 || res == 302) {
                BufferedReader in = new BufferedReader(new InputStreamReader(
                        con.getInputStream(), "UTF-8"));
                buffer = new StringBuffer();
                String line = "";
                while ((line = in.readLine()) != null) {
                    buffer.append(line);
                }
            } else {
                //QHFlag.e("Web Response:" + res);
            }

            con.disconnect();// Connect Close!
        } catch (Exception e) {
            // e.printStackTrace();
        }

        if (buffer.length() != 0 && buffer != null) {
            result[0] = buffer.toString();
        }
        return result;
    }

    /*
     * 使用HttpClient对象发送GET请求
     */
    public static String httpClientSendGet(String webUrl) {
        String content = null;
        // DefaultHttpClient
        DefaultHttpClient httpclient = new DefaultHttpClient();
        // HttpGet
        HttpGet httpget = new HttpGet(webUrl);
        // ResponseHandler
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try {
            content = httpclient.execute(httpget, responseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httpclient.getConnectionManager().shutdown();
        return content;
    }

    /*
     * 使用HttpClient对象发送有cookie的GET请求
     */
    public static String httpClientSendGet(String urlStr, HashMap<String, String> getParams, String Cookies) {
        String httpUrl = urlStr;

        httpUrl += "?" + getParamStr(getParams);

        HttpGet httpGet = new HttpGet(httpUrl);
        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpGet.setHeader("cookie", Cookies);
            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpClient.execute(httpGet);

            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                String strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                return strResult;
            }
        } catch (Exception e) {
        }
        return "";
    }

    /*
     * 使用HttpClient对象发送POST请求
     */
    public static String httpClientSendPost(String urlStr,
                                            HashMap<String, String> postParams) {
        String httpUrl = urlStr;
        // HttpPost对象
        HttpPost httpPost = new HttpPost(httpUrl);

        // 使用NameValuePair来保存要传递的Post参数
        List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();

        // 添加要传递的参数
        System.out.println("====================================");
        for (HashMap.Entry<String, String> entry : postParams.entrySet()) {
            postData.add(new BasicNameValuePair(entry.getKey(),
                    entry.getValue()));
        }
        System.out.println("====================================");

        try {
            // 设置字符集
            HttpEntity httpEntity = new UrlEncodedFormEntity(postData, "utf-8");
            httpPost.setEntity(httpEntity);

            // httpClient对象
            HttpClient httpClient = new DefaultHttpClient();

            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpClient.execute(httpPost);

            Log.i("AAAAA", String.valueOf(httpResponse.getStatusLine().getStatusCode()));


            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                String strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                System.out.println(strResult);
                return strResult;
            }
        } catch (Exception e) {

        }
        return "";
    }

    /*
     * 使用HttpClient对象发送POST请求
     */
    public static String httpClientSendPost(String urlStr, HashMap<String, String> postParams, String Cookies) {
        String httpUrl = urlStr;
        // HttpPost对象
        HttpPost httpPost = new HttpPost(httpUrl);

        // 使用NameValuePair来保存要传递的Post参数
        List<BasicNameValuePair> postData = new ArrayList<BasicNameValuePair>();

        // 添加要传递的参数
        System.out.println("====================================");

        for (HashMap.Entry<String, String> entry : postParams.entrySet()) {
            postData.add(new BasicNameValuePair(entry.getKey(),
                    entry.getValue()));
        }

        System.out.println("====================================");

        try {
            // 设置字符集
            HttpEntity httpEntity = new UrlEncodedFormEntity(postData, "utf-8");
            httpPost.setEntity(httpEntity);
            httpPost.setHeader("cookie", Cookies);
            // httpClient对象
            HttpClient httpClient = new DefaultHttpClient();

            // 请求HttpClient，取得HttpResponse
            HttpResponse httpResponse = httpClient.execute(httpPost);

            // 请求成功
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                // 取得返回的字符串
                String strResult = EntityUtils.toString(httpResponse
                        .getEntity());
                System.out.println(strResult);
                return strResult;
            }
        } catch (Exception e) {
        }
        return "";
    }

    public static String uploadBitmap1(String urlString, byte[] imageBytes,
                                       String cookie) {
        // String boundary = "*****";
        try {
            URL url = new URL(urlString);
            final HttpURLConnection con = (HttpURLConnection) url
                    .openConnection();
            // 允许input、Output,不使用Cache
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setUseCaches(false);

            // 设置传送的method=POST
            con.setRequestMethod("POST");
            // setRequestProperty
            con.setRequestProperty("Connection", "Keep-Alive");
            con.setRequestProperty("Charset", "UTF-8");
            con.setRequestProperty("Content-Type", "text/html");
            if (cookie != null && cookie != "") {
                Log.i("myEvent", "send cookies value is:" + cookie);
                con.setRequestProperty("cookie", cookie);
            }
            // 从主机读取数据的超时时间（单位：毫秒）
            con.setReadTimeout(50000);
            // 设置连接主机的超时时间（单位：毫秒）
            con.setConnectTimeout(50000);
            // System.out.println(con.getResponseCode());
            // 设置DataOutputStream
            DataOutputStream dsDataOutputStream = new DataOutputStream(
                    con.getOutputStream());
            // dsDataOutputStream.writeBytes(twoHyphen + boundary + endString);
            // dsDataOutputStream.writeBytes("Content-Disposition:form-data;"
            // + "name=\"file1\";filename=\"" + "11.jpg\"" + endString);
            // dsDataOutputStream.writeBytes(endString);

            dsDataOutputStream.write(imageBytes, 0, imageBytes.length);
            // dsDataOutputStream.writeBytes(endString);
            // dsDataOutputStream.writeBytes(twoHyphen + boundary + twoHyphen
            // + endString);
            dsDataOutputStream.close();
            int cah = con.getResponseCode();
            if (cah == 200) {
                InputStream isInputStream = con.getInputStream();
                int ch;
                StringBuffer buffer = new StringBuffer();
                while ((ch = isInputStream.read()) != -1) {
                    buffer.append((char) ch);
                }
                return buffer.toString();
            } else {
                return "false";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "false";
        }

    }

    /**
     * 请求字符串1 String[]
     */
    public static String getParamStr(String[] param, String[] value) {
        String res = "";
        if (param.length == value.length) {
            for (int i = 0; i < param.length; i++) {
                res += param[i] + "=" + toUTF8(value[i]) + "&";
            }
        }
        return res.substring(0, res.length() - 1);
    }

    /**
     * 请求字符串2 HashMap
     */
    public static String getParamStr(HashMap<String, String> param) {
        String result = "";
        if (param != null && param.size() > 0) {
            for (HashMap.Entry<String, String> entry : param.entrySet()) {
                result += entry.getKey() + "=" + toUTF8(entry.getValue()) + "&";
            }
            result = result.substring(0, result.length() - 1);
        }
        return result;
    }


    public static String toUTF8(String str) {
        String u = str;
        try {
            u = java.net.URLEncoder.encode(u, "UTF-8");
        } catch (Exception e) {

        }
        return u;
    }


}
