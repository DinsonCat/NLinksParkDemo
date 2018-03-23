package com.nlinks.parkdemo.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.nlinks.parkdemo.api.AppStartupApi;
import com.nlinks.parkdemo.entity.appstartup.StartupInfo;
import com.nlinks.parkdemo.http.BaseObserver;
import com.nlinks.parkdemo.http.HttpHelper;
import com.nlinks.parkdemo.http.RxSchedulers;
import com.nlinks.parkdemo.utils.CacheUtils;
import com.nlinks.parkdemo.utils.DownloadImageUtils;
import com.nlinks.parkdemo.utils.FileUtils;
import com.nlinks.parkdemo.utils.LogUtils;
import com.nlinks.parkdemo.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;

/**
 * 闪屏页的图片下载服务
 *
 * @author Dinson
 * @date 2017/6/26
 */
public class SplashDownLoadService extends IntentService {


    public SplashDownLoadService() {
        super("SplashDownLoad");
    }

    public static void startDownLoadSplashImage(Context context) {
        Intent intent = new Intent(context, SplashDownLoadService.class);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        loadSplashNetDate();
    }

    /**
     * 下载图片
     */
    private void loadSplashNetDate() {

        LogUtils.i("loadSplashNetDate() called");

//        String url = "http://ondlsj2sn.bkt.clouddn.com/FnR-HX50f9CT6__4u2VooWRtZLSN.png";
//        String name = "nizhenbang.png";

         HttpHelper.getRetrofit().create(AppStartupApi.class).getStartUpPic()
         .compose(RxSchedulers.io_main()).subscribe(new BaseObserver<StartupInfo>() {
            @NonNull
            @Override
            public void onHandleSuccess(StartupInfo startupInfo) {
                if (startupInfo.getCover() != null) {
                    StartupInfo startInfo = CacheUtils.getStartupInfo();
                    if (startInfo == null) {
                        LogUtils.e("splashLocal 为空导致下载");
                        //开始下载图片
                        loadPic(startupInfo);
                    } else if (isNeedDownLoad(startInfo.getLocalPath(), startupInfo.getCover())) {
                        LogUtils.e("isNeedDownLoad 导致下载");
                        //开始下载图片
                        loadPic(startupInfo);
                    }
                } else {
                    //没有最新活动，删除之前的启动页
                    CacheUtils.clearStartupInfo();
                    deletePic();
                }
            }

            @Override
            public void onHandleError(int code, String message) {
            }
        });
    }

    private void loadPic(final StartupInfo startupInfo) {
        // CacheUtils.setCache();
        String[] split = startupInfo.getCover().split("/");
        final String name = split[split.length - 1];

        DownloadImageUtils.downLoad(getApplicationContext().getCacheDir() + "/image/", new DownloadImageUtils.DownLoadInterFace() {
            @Override
            public void afterDownLoad(ArrayList<String> savePaths) {
                if (savePaths.size() == 1) {
                    LogUtils.i("闪屏页面下载完成:" + savePaths);
                    if (StringUtils.isEmpty(savePaths.get(0))) return;
                    startupInfo.setLocalPath(savePaths.get(0));
                    CacheUtils.setStartupInfo(startupInfo);
                } else {
                    LogUtils.e("闪屏页面下载失败" + savePaths);
                }

            }
        }, startupInfo.getCover());


       /* Single.just(startupInfo.getCover()).map(new Function<String, String>() {
            @Override
            public String apply(String s) throws Exception {
//                String path = DownloadImageUtils.DownloadImage(startupInfo.getCover(), s);
                String path = Glide.with(getApplicationContext()).load(s)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                        .get().getPath();

                LogUtils.e("最新活动页的图片地址："+path);
                if (path == null) return "";
                return path;
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        if (StringUtils.isEmpty(s)) return;
                        startupInfo.setLocalPath(s);
                        CacheUtils.setStartupInfo(startupInfo);
                    }
                });*/
    }

    /**
     * 没有最新活动，删除之前的启动页
     */
    private void deletePic() {
        StartupInfo info = CacheUtils.getStartupInfo();
        if (info == null) return;
        String path = info.getLocalPath();
        if (StringUtils.isEmpty(path)) return;
        File file = new File(path);
        FileUtils.deleteFile(file);
    }


    /**
     * @param path 本地存储的图片绝对路径
     * @param url  网络获取url
     * @return 比较储存的 图片名称的哈希值与 网络获取的哈希值是否相同
     */
    private boolean isNeedDownLoad(String path, String url) {
        // 如果本地存储的内容为空则进行下载
        if (TextUtils.isEmpty(path)) {
            return true;
        }
        // 如果本地文件不存在则进行下载，这里主要防止用户误删操作
        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        // 如果两者都存在则判断图片名称的 hashCode 是否相同，不相同则下载
        if (getImageName(path).hashCode() != getImageName(url).hashCode()) {
            return true;
        }
        return false;
    }

    private String getImageName(String url) {
        if (TextUtils.isEmpty(url)) {
            return "";
        }
        String[] split = url.split("/");
        String nameWith_ = split[split.length - 1];
        String[] split1 = nameWith_.split("\\.");
        return split1[0];
    }

}
