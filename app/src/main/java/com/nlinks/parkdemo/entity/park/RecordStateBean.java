package com.nlinks.parkdemo.entity.park;

/**
 * 停车记录支付状态的封装类
 */

public class RecordStateBean {

    private String stateName;
    private int color;
    private int imgRes;

    public int getImgRes() {
        return imgRes;
    }

    public void setImgRes(int imgRes) {
        this.imgRes = imgRes;
    }

    public RecordStateBean(String stateName, int color, int imgRes) {
        this.stateName = stateName;
        this.color = color;
        this.imgRes = imgRes;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
