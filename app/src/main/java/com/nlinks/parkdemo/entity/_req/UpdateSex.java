package com.nlinks.parkdemo.entity._req;

/**
 *  修改性别
 */

public class UpdateSex {

    public UpdateSex(String userId, Integer sex) {
        this.userId = userId;
        this.sex = sex;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    private String userId;   //用户ID

    private Integer sex;  //用户性别   0未设置    1男   2女
}
