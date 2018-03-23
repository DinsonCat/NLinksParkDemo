package com.nlinks.parkdemo.entity.park;

import java.io.Serializable;

/**
 * 停车场平面图车位标注实体类
 *
 * @author lzhixing@linewell.com
 * Created at 14:00 2016/3/9
 */
public class Coordinate implements Serializable {

    private String stallCode;
    private String plan;
    private float offsetX;
    private float offsetY;

    public String getStallCode() {
        return stallCode;
    }

    public void setStallCode(String stallCode) {
        this.stallCode = stallCode;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public float getOffsetX() {
        return offsetX;
    }

    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    public float getOffsetY() {
        return offsetY;
    }

    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }
}
