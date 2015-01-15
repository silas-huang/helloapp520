package com.silasapp.service;

/**
 * 任务实体,由于储存认为信息
 */
        import java.util.HashMap;

        import android.content.Context;

public class Task {

    public static final int GET_SERVER_INFO = 1;    //获取服务器运行状况信息
    public static final int GET_CHART_ORDER = 2;    //获取订单日统计

    private int taskId;
    private HashMap taskParams;
    private Context ctx;

    public Task(int taskId, HashMap taskParams, Context ctx) {
        super();
        this.taskId = taskId;
        this.taskParams = taskParams;
        this.ctx = ctx;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public HashMap getTaskParams() {
        return taskParams;
    }

    public void setTaskParams(HashMap taskParams) {
        this.taskParams = taskParams;
    }

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }
}
