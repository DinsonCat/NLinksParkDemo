package com.nlinks.parkdemo.entity.park;

import java.io.Serializable;

/**
 * 停车场平面图实体类
 * Created by lzhixing@linewell.com on 2016/3/9 15:35.
 */
public class ParkPlan implements Serializable {

    private String plan;

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }
}