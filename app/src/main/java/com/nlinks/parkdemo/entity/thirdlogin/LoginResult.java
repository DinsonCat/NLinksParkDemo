package com.nlinks.parkdemo.entity.thirdlogin;

/**
 * @author Dinson - 2017/9/1
 */
public class LoginResult {
    /**
     * QQ
     *
     * nickName : DINSON丶CAT
     * headUrl : http://q.qlogo.cn/qqapp/1106023273/6E79794648C7DFAEE1D6DE683A45B898/100
     * membership : 0
     * plateNum : 鲁PP2PP2
     * userId : 7c68dffae39e42a8adea9a46d96c5f4f
     * token : 5a62dc4c-02c7-4098-bce8-48a6ce317a40
     */

    /**
     * WeChat
     *
     * nickName : 🍼沃德天·维森莫·拉莫帅
     * headUrl : http://wx.qlogo.cn/mmopen/vi_32/PiajxSqBRaELkvDCBLeWBpVGm89D15N9nI2pt5oz7iaOWdZOPjXvBGN8tYjTqBsXr2hI9w4ibdx3Kwp6UQggOaibaQ/0
     * membership : 0
     * plateNum : 浙ZCZCP2
     * userId : 7c68dffae39e42a8adea9a46d96c5f4f
     * token : 7bbd677d-1f80-4091-a651-c2a5424ff16f
     */

    private int membership;
    private String plateNum;
    private String userId;
    private String token;

    public int getMembership() {
        return membership;
    }

    public void setMembership(int membership) {
        this.membership = membership;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


}
