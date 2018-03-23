package com.nlinks.parkdemo.entity.parkshare;

/**
 * 共享车位状态实体
 */
public class ShareParkStateBean {
    private String stateName;
    private String detailState;
    private int color;
    private int drawableLeft;

    public ShareParkStateBean(String stateName, int color, int drawableLeft, String detailState) {
        this.stateName = stateName;
        this.detailState = detailState;
        this.color = color;
        this.drawableLeft = drawableLeft;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getDetailState() {
        return detailState;
    }

    public void setDetailState(String detailState) {
        this.detailState = detailState;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public int getDrawableLeft() {
        return drawableLeft;
    }

    public void setDrawableLeft(int drawableLeft) {
        this.drawableLeft = drawableLeft;
    }
}
