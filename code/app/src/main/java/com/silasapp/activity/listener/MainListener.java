package com.silasapp.activity.listener;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.content.Context;

import com.silasapp.activity.MainActivity;
import com.silasapp.activity.R;
import com.silasapp.service.MainService;
import com.silasapp.service.Task;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Silas on 2015/1/13.
 */
public class MainListener {
    public MainListener() {
    }


    public static View.OnClickListener btn_getServerInfo_listener(final MainActivity activity, final Context context) {


        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                TextView textView = (TextView) activity.findViewById(R.id.tv_ShowInfo);
                textView.setText("123");
                activity.init();
            }
        };

    }


    public static View.OnClickListener btn_getOrderStat_listener(final MainActivity activity, final Context context) {


        return new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, -7);
                Date begin_date = calendar.getTime();
                calendar = Calendar.getInstance();
                calendar.add(Calendar.DATE, 1);
                Date end_date = calendar.getTime();
                HashMap param = new HashMap();
//                param.put("begindate", sdf.format(begin_date));
//                param.put("enddate", sdf.format(end_date));

                param.put("begindate", "2015-01-07");
                param.put("enddate", "2015-01-14");



                // 加载数据
                MainService.addActivity(activity);
                Task task = new Task(Task.GET_CHART_ORDER, param, context);
                MainService.newTask(task);// 添加新任务
                ProgressDialog progressDialog = null;

                activity.progressDialog.show();

            }
        };

    }


}
