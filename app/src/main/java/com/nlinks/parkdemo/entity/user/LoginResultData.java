package com.nlinks.parkdemo.entity.user;


/**
 * 登陆接口返回数据
 */
public class LoginResultData {


    /**
     * membership : 1
     * token : 2ab87edf-8e9e-4b33-a13e-9e7e681f7703
     * userId : 19ff049d11e944cdbd1ccd4f163f6475
     * plateNum : 赣GQGQGG
     */

    private int membership;
    private String token;
    private String userId;
    private String plateNum;

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }
}
