package com.nlinks.parkdemo.entity.appstartup;

/**
 * @author Dinson - 启动页请求活动页图片
 */
public class StartupInfo {

    /**
     * cover : http://img.ph.126.net/ocT0cPlMSiTs2BgbZ8bHFw==/631348372762626203.jpg
     * linkUrl : https://www.baidu.com/?tn=57095150_2_oem_dg
     * startTime : 2017-07-06 09:32:29
     * id : 1
     * endTime : 2017-07-06 09:32:29
     */

    private String cover;
    private String linkUrl;
    private String startTime;
    private String id;
    private String endTime;

    private String localPath;//本地图片地址

    public String getLocalPath() {
        return localPath;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
