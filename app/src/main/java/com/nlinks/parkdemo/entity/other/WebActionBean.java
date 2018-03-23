package com.nlinks.parkdemo.entity.other;

/**
 * @author Dinson - 2017/7/10
 */
public class WebActionBean {
    private int paytype;
    private double money;
    private int viewType;

    @Override
    public String toString() {
        return "WebActionBean{" +
            "paytype=" + paytype +
            ", money=" + money +
            ", viewtype=" + viewType +
            '}';
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getPaytype() {
        return paytype;
    }

    public void setPaytype(int paytype) {
        this.paytype = paytype;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }


}
