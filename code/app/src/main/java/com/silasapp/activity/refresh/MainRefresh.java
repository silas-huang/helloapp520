package com.silasapp.activity.refresh;

import android.app.Activity;
import android.content.Context;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.silasapp.activity.ISilasActivity;
import com.silasapp.activity.MainActivity;
import com.silasapp.activity.R;
import com.silasapp.entities.ChartOrderDTO;
import com.silasapp.entities.OrderStatByDayDTO;
import com.silasapp.service.Task;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Silas on 2015/1/13.
 */
public class MainRefresh {
    public MainRefresh() {
    }

    public static void refresh(MainActivity activity, Context context, HashMap<String, Object> map) {

        switch (Integer.parseInt(String.valueOf(map.get("task")))) {
            case Task.GET_SERVER_INFO:

                refreshServerInfo(activity, context, map);

                break;
            case Task.GET_CHART_ORDER:
                refreshChartOrder(activity, context, map);
                break;
            default:
                break;
        }

    }

    public static void refreshServerInfo(MainActivity activity, Context context, HashMap<String, Object> map) {

        TextView tv_ShowInfo = (TextView) activity.findViewById(R.id.tv_ShowInfo);
        tv_ShowInfo.setText((String) map.get("data"));

    }

    public static void refreshChartOrder(MainActivity activity, Context context, HashMap<String, Object> map) {

        String data = (String) map.get("data");

        ChartOrderDTO chartOrderDTO;

        chartOrderDTO=ChartOrderDTO.jsonToModel(data);

        TextView tv=(TextView)activity.findViewById(R.id.tv_ShowInfo);
        tv.setText("");

        ListView lv_chart_order = (ListView) activity.findViewById(R.id.lv_chart_order);

        ArrayList<HashMap<String, Object>> listItem = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < chartOrderDTO.getOrderstat_list().size(); i++) {
            OrderStatByDayDTO orderStatByDayDTO=chartOrderDTO.getOrderstat_list().get(i);
            HashMap<String, Object> mapdata = new HashMap<String, Object>();
            mapdata.put("ItemDate", orderStatByDayDTO.getDate());//图像资源的ID
            mapdata.put("ItemNumber", orderStatByDayDTO.getNumber_success());
            mapdata.put("ItemMoney", orderStatByDayDTO.getMoney_success());
            listItem.add(mapdata);
        }
        //生成适配器的Item和动态数组对应的元素
        SimpleAdapter listItemAdapter;
        listItemAdapter = new SimpleAdapter(context, listItem,//数据源
                R.layout.list_chart_orderlist,//ListItem的XML实现
                //动态数组与ImageItem对应的子项
                new String[]{"ItemDate", "ItemNumber", "ItemMoney"},
                //ImageItem的XML文件里面的一个ImageView,两个TextView ID
                new int[]{R.id.chart_order_tv_date, R.id.chart_order_tv_numer, R.id.chart_order_tv_money}
        );

        //添加并且显示
        lv_chart_order.setAdapter(listItemAdapter);

        activity.progressDialog.dismiss();

    }

}
