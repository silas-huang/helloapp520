package com.silasapp.entities;

/**
 * Created by Silas on 2015/1/14.
 */
public class OrderStatByDayDTO {
    private String date;
    private int number_success;
    private int number_failed;
    private int number_waitpay;

    private double money_success;
    private double money_failed;
    private double money_wailpay;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getNumber_success() {
        return number_success;
    }

    public void setNumber_success(int number_success) {
        this.number_success = number_success;
    }

    public int getNumber_failed() {
        return number_failed;
    }

    public void setNumber_failed(int number_failed) {
        this.number_failed = number_failed;
    }

    public int getNumber_waitpay() {
        return number_waitpay;
    }

    public void setNumber_waitpay(int number_waitpay) {
        this.number_waitpay = number_waitpay;
    }

    public double getMoney_success() {
        return money_success;
    }

    public void setMoney_success(double money_success) {
        this.money_success = money_success;
    }

    public double getMoney_failed() {
        return money_failed;
    }

    public void setMoney_failed(double money_failed) {
        this.money_failed = money_failed;
    }

    public double getMoney_wailpay() {
        return money_wailpay;
    }

    public void setMoney_wailpay(double monet_wailpay) {
        this.money_wailpay = monet_wailpay;
    }
}
