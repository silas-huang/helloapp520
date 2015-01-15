package com.silasapp.entities;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Silas on 2015/1/14.
 */
public class ChartOrderDTO extends ApiBaseModel {

    private String date_begin;
    private String date_end;
    private ArrayList<OrderStatByDayDTO> orderstat_list;

    public String getDate_begin() {
        return date_begin;
    }

    public void setDate_begin(String date_begin) {
        this.date_begin = date_begin;
    }

    public String getDate_end() {
        return date_end;
    }

    public void setDate_end(String date_end) {
        this.date_end = date_end;
    }

    public ArrayList<OrderStatByDayDTO> getOrderstat_list() {
        return orderstat_list;
    }

    public void setOrderstat_list(ArrayList<OrderStatByDayDTO> orderstat_list) {
        this.orderstat_list = orderstat_list;
    }


    public static ChartOrderDTO jsonToModel(String data) {
        ChartOrderDTO chartOrderDTO = new ChartOrderDTO();

        if (data != null) {
            try {
                JSONObject obj = new JSONObject(data);
                String status = obj.optString("status");
                if (status.equals("1")) {  //数据返回成功

                    chartOrderDTO.setStatus(status);
                    chartOrderDTO.setDate_begin(obj.optString("date_begin"));
                    chartOrderDTO.setDate_end(obj.optString("date_end"));

                    JSONArray dateList = obj.optJSONArray("orderstat_list");

                    OrderStatByDayDTO orderStatByDayDTO;
                    ArrayList<OrderStatByDayDTO> orderStatByDayDTOs = new ArrayList<OrderStatByDayDTO>();
                    int i = 0;
                    for (; i < dateList.length(); i++) {

                        orderStatByDayDTO = new OrderStatByDayDTO();

                        JSONObject j = dateList.optJSONObject(i);

                        orderStatByDayDTO.setDate(j.optString("date"));
                        orderStatByDayDTO.setMoney_failed(j.optDouble("money_failed"));
                        orderStatByDayDTO.setMoney_success(j.optDouble("money_success"));
                        orderStatByDayDTO.setMoney_wailpay(j.optDouble("money_waitpay"));
                        orderStatByDayDTO.setNumber_failed(j.optInt("number_failed"));
                        orderStatByDayDTO.setNumber_success(j.optInt("number_success"));
                        orderStatByDayDTO.setNumber_waitpay(j.optInt("number_waitpay"));

                        orderStatByDayDTOs.add(orderStatByDayDTO);
                    }

                    chartOrderDTO.setOrderstat_list(orderStatByDayDTOs);

                }
            } catch (JSONException e) {
                chartOrderDTO.setStatus("-11");
            }
        } else {
            chartOrderDTO.setStatus("-10");

        }

        return chartOrderDTO;

    }

}
