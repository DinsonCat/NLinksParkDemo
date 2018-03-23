package com.nlinks.parkdemo.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.nlinks.parkdemo.entity.appstartup.StartupInfo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * 文件缓存工具类
 */
public class CacheUtils {

    private static File sCacheDir = UIUtils.getContext().getCacheDir();


    /**
     * 设置splash的json数据
     *
     * @param bean 数据对象
     */
    public static void setStartupInfo(StartupInfo bean) {
        String json = new Gson().toJson(bean);
        setCache("splash", json);
    }

    public static StartupInfo getStartupInfo() {
        Gson gson = new Gson();
        StartupInfo result = null;
        String splash = getCache("splash");
        if (splash != null) {
            result = gson.fromJson(splash, StartupInfo.class);
        }
        return result;
    }
    public static void clearStartupInfo(){
        deleteCache("splash");
    }


    /////////////////////////////////分割线//////////////////////////////////////////////////

    public static void setCache(String url, String json) {
        setCache(url, json, 0);
    }

    public static void setCache(String url, String json, long deathLine) {
        File cacheFile = new File(sCacheDir, MD5.encode(url));
        FileWriter fw = null;
        try {
            fw = new FileWriter(cacheFile);
            if (deathLine != 0) {
                deathLine = System.currentTimeMillis() + deathLine;
            }
            //0表示没有有效期
            fw.write(deathLine + "\n");
            fw.write(json);
            fw.flush();
        } catch (IOException e) {
            Log.e("CacheUtils", "", e);
        } finally {
            IOUtils.close(fw);
        }
    }

    public static String getCache(String url) {
        File cacheFile = new File(sCacheDir, MD5.encode(url));

        if (cacheFile.exists()) {
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(cacheFile));
                long deathLine = Long.parseLong(reader.readLine());

                if (System.currentTimeMillis() < deathLine || deathLine == 0) {

                    StringBuffer sb = new StringBuffer();
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    return sb.toString();

                }
            } catch (Exception e) {
                return null;
            } finally {
                IOUtils.close(reader);
            }
        }
        return null;
    }
    /**
     * 删除缓存
     */
    public static void deleteCache(String url) {
        File cacheFile = new File(sCacheDir, MD5.encode(url));
        if (cacheFile.exists()) {
            cacheFile.delete();
        }
    }


}
