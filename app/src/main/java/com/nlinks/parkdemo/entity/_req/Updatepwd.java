package com.nlinks.parkdemo.entity._req;

/**
 * 修改密码
 */
public class Updatepwd {
    private String phoneNo;     //手机号

    private String password;    //原密码

    private String newPassword; //新密码

    public Updatepwd(String phoneNo, String password, String newPassword) {
        this.phoneNo = phoneNo;
        this.password = password;
        this.newPassword = newPassword;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
