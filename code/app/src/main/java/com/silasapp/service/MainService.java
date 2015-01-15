package com.silasapp.service;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.silasapp.activity.ISilasActivity;
import com.silasapp.config.App;
import com.silasapp.util.SHA1;
import com.silasapp.util.WebSender;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Silas on 2015/1/12.
 */
public class MainService extends Service implements Runnable {

    private String apiUrl; //api接口调用地址，实例初始化时开始调用


    private static ArrayList<ISilasActivity> allActivity = new ArrayList<ISilasActivity>();
    private static ArrayList<Task> allTasks = new ArrayList<Task>();
    List<String> p = new ArrayList<String>();// post的参数 注意设置成全局变量后只能一次放松一个Post了
    List<String> v = new ArrayList<String>();// post的数值
    public static boolean isRun = false;


    //构造方法1
    public MainService() {
        this.apiUrl = App.APIURL;  //默认接口地址
    }

    //构造方法2，接口调用地址可配置
    public MainService(String url) {
        this.apiUrl = url;
    }

    public void getServerStateInfo() {

    }


    public static void newTask(Task t) {
        allTasks.add(t);
    }

    public static void addActivity(ISilasActivity iw) {
        allActivity.add(iw);
    }

    public static void removeActivity(ISilasActivity iw) {
        allActivity.remove(iw);
    }

    public static ISilasActivity getActivityByName(String name) {
        for (ISilasActivity iw : allActivity) {
            if (iw.getClass().getName().indexOf(name) >= 0) {
                return iw;
            }
        }
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

        isRun = true;
        new Thread(this).start();
        Log.i("system.out.print", "ServerService    onCreate()");
        Log.e("=============================", "MainService    onCreate()");
    }

    @Override
    public void onDestroy() {
        isRun = false;
        stopSelf();
        super.onDestroy();
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        while (isRun) {
            try {
                if (allTasks.size() > 0) {
                    // 执行任务,可以遍历执行多个任务
                    // doTask(allTasks.get(0));
                    for (Task task : allTasks) {
                        doTask(task);
                    }

                } else {
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                }
            } catch (Exception e) {
                if (allTasks.size() > 0)
                    allTasks.remove(allTasks.get(0));
                Log.d("error", "------------------" + e);
            }
        }
    }


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * 退出应用程序
     *
     * @param context
     */
    public static void exitAPP(Context context) {

        Intent it = new Intent("com.silasapp.service.MainService");
        context.stopService(it);// 停止服务
        // 杀死进程 我感觉这种方式最直接了当
        android.os.Process.killProcess(android.os.Process.myPid());

    }

    public static void finshall() {

        for (ISilasActivity activity : allActivity) {// 遍历所有activity 一个一个删除
            ((Activity) activity).finish();
        }
    }

    /**
     * 网络连接异常
     *
     * @param context
     */

    public static void AlertNetError(final Context context) {
        AlertDialog.Builder alerError = new AlertDialog.Builder(context);
        alerError.setTitle("网络错误");
        alerError.setMessage("请检查网络");
        alerError.setNegativeButton("退出", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                exitAPP(context);
            }
        });
        alerError.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                context.startActivity(new Intent(
                        android.provider.Settings.ACTION_WIRELESS_SETTINGS));
            }
        });
        alerError.create().show();
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);

            MainService.getActivityByName("MainActivity")
                    .refresh(msg.obj);

            /*switch (msg.what) {
                case Task.GET_SERVER_INFO:
                    MainService.getActivityByName("MainActivity")
                            .refresh(msg.obj);// 调用Main的方法刷新控件

                    break;
                case Task.GET_CHART_ORDER:
                    MainService.getActivityByName("MainActivity")
                            .refresh(msg.obj);// 调用Main的方法刷新控件
                    break;
                default:
                    break;
            }*/
        }

    };


    private HashMap<String,String> buildParams(HashMap<String, String> map) {

        String paramsStr = "";
        String sign = "";

        //step1：参数进行字典排序
        Iterator iter = map.entrySet().iterator();
        ArrayList<String> arrayList = new ArrayList<String>();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            //String key =(String)  entry.getKey();
            String val = (String) entry.getValue();
            arrayList.add(val);
        }
        Collections.sort(arrayList);

        for (String p : arrayList) {
            sign += p;
        }
        sign += App.SECRET;  //参数（字段排序，除key,sign参数除外） + 用户密钥

        //step2：参数字符串 sha1加密

        sign = new SHA1().getDigestOfString(sign.getBytes());

        map.put("sign", sign);
        map.put("key",App.KEY);
        return map;

    }

    private void doTask(Task task) throws JSONException {
        // TODO Auto-generated method stub
        Message msg = handler.obtainMessage();
        msg.what = task.getTaskId();
        String httpResult;
        HashMap map = new HashMap();


        switch (task.getTaskId()) {
            case Task.GET_SERVER_INFO:// 获取服务器信息

                httpResult = WebSender.httpClientSendGet(this.apiUrl + "/index/login", buildParams(task.getTaskParams()), null);
                map.put("data", httpResult);
                map.put("task", task.getTaskId());
                map.put("taskParams", task.getTaskParams());
                msg.obj = map;
                break;
            case Task.GET_CHART_ORDER:
                httpResult = WebSender.httpClientSendGet(this.apiUrl + "/chart/order", buildParams(task.getTaskParams()), null);
                map.put("data", httpResult);
                map.put("task", task.getTaskId());
                map.put("taskParams", task.getTaskParams());
                msg.obj = map;
                break;
            default:
                break;
        }
        allTasks.remove(task);
        // 通知主线程更新UI
        handler.sendMessage(msg);
    }
}

