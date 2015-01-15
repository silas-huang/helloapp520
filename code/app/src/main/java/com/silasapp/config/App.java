package com.silasapp.config;

import android.app.Application;

/**
 * Created by Silas on 2015/1/12.
 */
public class App extends Application {

    /*
    *
    * 开始声明app需要用到的全局变量
    *
    * */

    public final static String APIURL = "http://182.92.185.239:8087";
    public final static String KEY="20150115001";
    public final static String SECRET="!@#$%12345";

    @Override
    public void onCreate() {

        super.onCreate();
    }
}
