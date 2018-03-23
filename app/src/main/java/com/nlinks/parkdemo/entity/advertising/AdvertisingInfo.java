package com.nlinks.parkdemo.entity.advertising;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by yanzhenyang on 2017/7/6.
 */

public class AdvertisingInfo implements Parcelable {

    /**
     @property(strong,nonatomic)NSString *cover;//f封面图片
     @property(strong,nonatomic)NSString *advertisingId;// = 1
     @property(strong,nonatomic)NSString *linkUrl;//链接地址
     @property(strong,nonatomic)NSString *name;// = "188\U6d3b\U52a8"
     @property(assign,nonatomic)long endTime;// = 1499304242000
     @property(assign,nonatomic)long startTime;
//     @property(assign,nonatomic)AppType apptype;//1、蜻蜓停车 2、华榕泊车
     @property(assign,nonatomic)AdvertisingStatus status;//1、上架中；2、未上架；3、已失效

     @property(strong,nonatomic)NSString *useCase;
     */


    private String cover;
    private String advertisingId;
    private String linkUrl;
    private String name;
    private String endTime;
    private String startTime;
//    private int apptype;
    private int status;

    private String useCase;


    protected AdvertisingInfo(Parcel in) {
        cover = in.readString();
        advertisingId = in.readString();
        linkUrl = in.readString();
        name = in.readString();
        endTime = in.readString();
        startTime = in.readString();
        status = in.readInt();
        useCase = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(cover);
        dest.writeString(advertisingId);
        dest.writeString(linkUrl);
        dest.writeString(name);
        dest.writeString(endTime);
        dest.writeString(startTime);
        dest.writeInt(status);
        dest.writeString(useCase);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AdvertisingInfo> CREATOR = new Creator<AdvertisingInfo>() {
        @Override
        public AdvertisingInfo createFromParcel(Parcel in) {
            return new AdvertisingInfo(in);
        }

        @Override
        public AdvertisingInfo[] newArray(int size) {
            return new AdvertisingInfo[size];
        }
    };

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAdvertisingId() {
        return advertisingId;
    }

    public void setAdvertisingId(String advertisingId) {
        this.advertisingId = advertisingId;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

//    public int getApptype() {
//        return apptype;
//    }
//
//    public void setApptype(int apptype) {
//        this.apptype = apptype;
//    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getUseCase() {
        return useCase;
    }

    public void setUseCase(String useCase) {
        this.useCase = useCase;
    }

}
