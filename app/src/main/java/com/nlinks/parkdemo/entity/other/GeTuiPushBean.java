package com.nlinks.parkdemo.entity.other;

/**
 * @author Dinson - 2017/8/18
 */
public class GeTuiPushBean {
    /**
     * message : 尊敬的浙A12345车主，您于2017年09月04日09时59分成功缴纳停车费5元，请于15分钟内离场，以免产生额外费用，点击查看倒计时。
     * parkRecordID : 30206b598fbd11e7ad1d1c1b0dc73439
     * title : 车辆缴费
     * type : 3
     * url : www.baidu.com
     */
    private String message;
    private String parkRecordID;
    private String title;
    private int type;
    private String url;

    @Override
    public String toString() {
        return "PushBean{" +
            "message='" + message + '\'' +
            ", parkRecordID='" + parkRecordID + '\'' +
            ", title='" + title + '\'' +
            ", type=" + type +
            ", url='" + url + '\'' +
            '}';
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getParkRecordID() {
        return parkRecordID;
    }

    public void setParkRecordID(String parkRecordID) {
        this.parkRecordID = parkRecordID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
