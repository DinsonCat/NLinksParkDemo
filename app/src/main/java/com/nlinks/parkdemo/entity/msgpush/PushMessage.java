package com.nlinks.parkdemo.entity.msgpush;

import android.os.Parcel;
import android.os.Parcelable;

import com.nlinks.parkdemo.widget.recycleview.TypeFactory;
import com.nlinks.parkdemo.widget.recycleview.Visitable;

/**
 * @author Dinson - 2017/9/6
 */
public class PushMessage implements Parcelable,Visitable{

    /**
     * id : 46dd09952d2a446895fc21fa4deef0cc
     * msgType : 3
     * message : 尊敬的浙C12345车主，您于2017年09月06日10时20分成功缴纳停车费5.0元，请于15分钟内离场，以免产生额外费用，点击查看倒计时。
     * sendtime : 2017-09-06 10:21:01
     * isread : 0
     * parkRecordID : be718231914b11e7ad1d1c1b0dc73439
     * title : 车辆缴费
     */

    private String id;
    private int msgType;
    private String message;
    private String sendtime;
    private int isread;
    private String parkRecordID;
    private String title;


    protected PushMessage(Parcel in) {
        id = in.readString();
        msgType = in.readInt();
        message = in.readString();
        sendtime = in.readString();
        isread = in.readInt();
        parkRecordID = in.readString();
        title = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeInt(msgType);
        dest.writeString(message);
        dest.writeString(sendtime);
        dest.writeInt(isread);
        dest.writeString(parkRecordID);
        dest.writeString(title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PushMessage> CREATOR = new Creator<PushMessage>() {
        @Override
        public PushMessage createFromParcel(Parcel in) {
            return new PushMessage(in);
        }

        @Override
        public PushMessage[] newArray(int size) {
            return new PushMessage[size];
        }
    };

    @Override
    public String toString() {
        return "PushMessage{" +
            "id='" + id + '\'' +
            ", msgType=" + msgType +
            ", message='" + message + '\'' +
            ", sendtime='" + sendtime + '\'' +
            ", isread=" + isread +
            ", parkRecordID='" + parkRecordID + '\'' +
            ", title='" + title + '\'' +
            '}';
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getMsgType() {
        return msgType;
    }

    public void setMsgType(int msgType) {
        this.msgType = msgType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendtime() {
        return sendtime;
    }

    public void setSendtime(String sendtime) {
        this.sendtime = sendtime;
    }

    public int getIsread() {
        return isread;
    }

    public void setIsread(int isread) {
        this.isread = isread;
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

    @Override
    public int type(TypeFactory typeFactory) {
        return typeFactory.type(this);
    }
}
