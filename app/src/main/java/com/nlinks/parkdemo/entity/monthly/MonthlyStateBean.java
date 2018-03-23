package com.nlinks.parkdemo.entity.monthly;

/**
 * Created by dinson on 2017/11/15.
 */

public class MonthlyStateBean {
    private String stateName;
    private int color;

    public MonthlyStateBean(String stateName, int color) {
        this.stateName = stateName;
        this.color = color;
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
