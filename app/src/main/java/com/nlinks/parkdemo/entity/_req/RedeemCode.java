package com.nlinks.parkdemo.entity._req;

/**
 * @author Dinson - 2017/7/8
 */
public class RedeemCode {

    /**
     * redeemCode : string
     * userId : string
     */

    private String redeemCode;
    private String userId;

    public RedeemCode(String redeemCode, String userId) {
        this.redeemCode = redeemCode;
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "RedeemCode{" +
                "redeemCode='" + redeemCode + '\'' +
                ", userId='" + userId + '\'' +
                '}';
    }

    public String getRedeemCode() {
        return redeemCode;
    }

    public void setRedeemCode(String redeemCode) {
        this.redeemCode = redeemCode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
