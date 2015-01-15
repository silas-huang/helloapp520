package com.silasapp.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.silasapp.activity.listener.MainListener;
import com.silasapp.activity.refresh.MainRefresh;
import com.silasapp.service.MainService;
import com.silasapp.service.Task;

import java.util.HashMap;


public class MainActivity extends Activity implements ISilasActivity {
    private Context context;
    private Button btn_getServerInfo;
    private Button btn_getOrderStat;
    private TextView tv_ShowInfo;
    public ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

/*

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null) {
            Toast.makeText(Main.this, "连网正常" + info.getTypeName(), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(Main.this, "未连网", Toast.LENGTH_SHORT).show();
        }
*/


        context = MainActivity.this;
        Intent intent = new Intent(MainActivity.this, MainService.class);// 在首个Activity开启服务
        startService(intent);
        initView();
        //init();

        Log.i("system.out.print", "AAAA");
        Log.i("Create", "=====");

/*

        Button btn = (Button) findViewById(R.id.);

        btn.setOnClickListener(new View.OnClickListener() {
                                   @Override
                                   public void onClick(View v) {

                                       String ret = "";

                                       Map<String, Object> map = new HashMap<String, Object>();
                                       map.put("username", "1");
                                       map.put("password", "1");


                                       String urlString = "http://182.92.185.239:8087/index/login?username=1&password=1"; // 一個獲取菜谱的url地址
                                       AsyncHttpUtility.get(urlString, new AsyncHttpResponseHandler() {

                                           public void onFinish() { // 完成后调用，失败，成功，都要掉用

                                           }

                                           @Override
                                           public void onSuccess(int i, Header[] headers, byte[] bytes) {
                                               TextView tv = (TextView) findViewById(R.id.tv1);

                                               tv.setText(new String(bytes));
                                           }

                                           @Override
                                           public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
                                               Toast.makeText(Main.this, "onFailure",
                                                       Toast.LENGTH_LONG).show();
                                           }


                                       });


                                   }
                               }
        );
*/

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void init() {
        HashMap param = new HashMap();
        param.put("username", "1");
        param.put("password", "1");

        // 加载数据
        MainService.addActivity(MainActivity.this);
        Task task = new Task(Task.GET_SERVER_INFO, param, context);
        MainService.newTask(task);// 添加新任务
        progressDialog.show();
    }

    @Override
    public void initView() {
        btn_getServerInfo = (Button) findViewById(R.id.btn_getServerInfo);
        btn_getOrderStat = (Button) findViewById(R.id.btn_getOrderStat);
        tv_ShowInfo = (TextView) findViewById(R.id.tv_ShowInfo);

        //给按钮绑定事件
        btn_getServerInfo.setOnClickListener(MainListener.btn_getServerInfo_listener(MainActivity.this, context));
        btn_getOrderStat.setOnClickListener(MainListener.btn_getOrderStat_listener(MainActivity.this, context));

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("正在获取数据");
    }

    @Override
    public void refresh(Object... param) {
        HashMap map = (HashMap) param[0];// 获取map的数据

        MainRefresh.refresh(MainActivity.this, context, map);

        progressDialog.dismiss();
        Log.i("测试", "setadapter");
    }
}
