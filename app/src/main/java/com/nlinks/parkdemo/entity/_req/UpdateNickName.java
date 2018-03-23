package com.nlinks.parkdemo.entity._req;

/**
 * 更改密码
 */
public class UpdateNickName {

    private String userId;   //用户ID

    private String nickName; //用户昵称

    public UpdateNickName(String userId, String nickName) {
        this.userId = userId;
        this.nickName = nickName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

}
