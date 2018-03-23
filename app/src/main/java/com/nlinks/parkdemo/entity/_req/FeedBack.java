package com.nlinks.parkdemo.entity._req;

/**
 * Created by PC-DINSON on 2017/4/12.
 */
public class FeedBack {

    private String typeId;   //工单类型id

    private String typeName; //工单类型名

    private String content;  //工单内容

    private String userId;   //用户id

    private String mobilePhone; //用户手机号码

    private String plateNum; //用户车牌号

    private String attachUrl;  //附件图片url地址,多个url以,相隔

    public FeedBack(String typeId, String typeName, String content, String userId, String mobilePhone, String plateNum, String attachUrl) {
        this.typeId = typeId;
        this.typeName = typeName;
        this.content = content;
        this.userId = userId;
        this.mobilePhone = mobilePhone;
        this.plateNum = plateNum;
        this.attachUrl = attachUrl;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getPlateNum() {
        return plateNum;
    }

    public void setPlateNum(String plateNum) {
        this.plateNum = plateNum;
    }

    public String getAttachUrl() {
        return attachUrl;
    }

    public void setAttachUrl(String attachUrl) {
        this.attachUrl = attachUrl;
    }
}
