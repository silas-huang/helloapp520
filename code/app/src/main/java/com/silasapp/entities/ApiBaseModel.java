package com.silasapp.entities;

/**
 * Created by Silas on 2015/1/14.
 */
public class ApiBaseModel {

    private String status;
    private String failed_code;
    private String failed_msg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFailed_code() {
        return failed_code;
    }

    public void setFailed_code(String failed_code) {
        this.failed_code = failed_code;
    }

    public String getFailed_msg() {
        return failed_msg;
    }

    public void setFailed_msg(String failed_msg) {
        this.failed_msg = failed_msg;
    }
}
