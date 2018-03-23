package com.nlinks.parkdemo.entity.pay;

/**
 *
 */
public class WalletMoney {


    /**
     * accountCode :
     * usdisableMoney : 0.0
     * accountName :
     * createTime : 2017-08-01 09:06:27
     * usableMoney : 4783.05
     * sharedBenefit : 0.0
     * couponCount : 6
     * updateTime : 2017-08-03 14:06:51
     * id : 64d08d29ab044d60a6e544a544bace1a
     * userId : 122f20f181d14c0595ad0311c50f1970
     */

    private String accountCode;
    private double usdisableMoney;
    private String accountName;
    private String createTime;
    private double usableMoney;
    private double sharedBenefit;
    private int couponCount;
    private String updateTime;
    private String id;
    private String userId;

    public String getAccountCode() {
        return accountCode;
    }

    public void setAccountCode(String accountCode) {
        this.accountCode = accountCode;
    }

    public double getUsdisableMoney() {
        return usdisableMoney;
    }

    public void setUsdisableMoney(double usdisableMoney) {
        this.usdisableMoney = usdisableMoney;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public double getUsableMoney() {
        return usableMoney;
    }

    public void setUsableMoney(double usableMoney) {
        this.usableMoney = usableMoney;
    }

    public double getSharedBenefit() {
        return sharedBenefit;
    }

    public void setSharedBenefit(double sharedBenefit) {
        this.sharedBenefit = sharedBenefit;
    }

    public int getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(int couponCount) {
        this.couponCount = couponCount;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
