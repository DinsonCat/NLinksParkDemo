package com.nlinks.parkdemo.utils;

import android.os.AsyncTask;
import android.os.Environment;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 图片下载工具类
 *
 * @author Dinson
 * @date 2017/6/26
 */
public class DownloadImageUtils {
    public interface DownLoadInterFace {
        void afterDownLoad(ArrayList<String> savePaths);
    }
    public static void downLoad(String savePath, DownLoadInterFace downLoadInterFace, String... download) {
        new DownLoadTask(savePath, downLoadInterFace).execute(download);
    }
    private static class DownLoadTask extends AsyncTask<String, Integer, ArrayList<String>> {
        private String mSavePath;
        private DownLoadInterFace mDownLoadInterFace;
        private DownLoadTask(String savePath, DownLoadInterFace downLoadTask) {
            this.mSavePath = savePath;
            this.mDownLoadInterFace = downLoadTask;
        }
        @Override
        protected ArrayList<String> doInBackground(String... params) {
            ArrayList<String> names = new ArrayList<>();
            for (String url : params) {
                if (!TextUtils.isEmpty(url)) {
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        // 获得存储卡的路径
                        FileOutputStream fos = null;
                        InputStream is = null;
                        try {
                            URL downUrl = new URL(url);
                            // 创建连接
                            HttpURLConnection conn = (HttpURLConnection) downUrl.openConnection();
                            conn.connect();
                            // 创建输入流
                            is = conn.getInputStream();
                            File file = new File(mSavePath);
                            // 判断文件目录是否存在
                            if (!file.exists()) {
                                file.mkdirs();
                            }
                            String[] split = url.split("/");
                            String fileName = split[split.length - 1];
                            File mApkFile = new File(mSavePath, fileName);
                            names.add(mApkFile.getAbsolutePath());
                            fos = new FileOutputStream(mApkFile, false);
                            int count = 0;
                            // 缓存
                            byte buf[] = new byte[1024];
                            while (true) {
                                int read = is.read(buf);
                                if (read == -1) {
                                    break;
                                }
                                fos.write(buf, 0, read);
                                count += read;
                                publishProgress(count);
                            }
                            fos.flush();
                        } catch (Exception e) {
                            LogUtils.e(e.getMessage());
                        } finally {
                            try {
                                if (is != null) {
                                    is.close();
                                }
                                if (fos != null) {
                                    fos.close();
                                }
                            } catch (IOException e1) {
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
            return names;
        }
        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            super.onPostExecute(strings);
            if (mDownLoadInterFace != null) {
                mDownLoadInterFace.afterDownLoad(strings);
            }
        }
    }
  /*  private static final String IMAGE_DIR = UIUtils.getContext().getCacheDir() + "/image";
//    private static final String IMAGE_DIR = GlobalConfig.ROOT_DIR;

    *//**
     * 下载图片到SD卡
     *
     * @param url
     * @param imageName
     *//*
    public static String DownloadImage(final String url, final String imageName) {
       *//* ImageApi imageApi = HttpHelper.getRetrofit().create(ImageApi.class);
        Call<ResponseBody> resultCall = imageApi.downloadImage(url);

        resultCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                writeResponseBodyToDisk(imageName, response.body());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
            }
        });*//*

        try {
            long fileSize;
            File fileDr = new File(IMAGE_DIR);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File out = new File(IMAGE_DIR, imageName);
            if (out.exists()) {
                return IMAGE_DIR + "/" + imageName;
            }
            out = new File(IMAGE_DIR, imageName);


            URL myURL = new URL(url);


            URLConnection conn = myURL.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();


            fileSize = conn.getContentLength();

            if (fileSize <= 0)
                UIUtils.showToast("无活动图片");
            if (is == null)
                UIUtils.showToast("stream is null");
            FileOutputStream fos = new FileOutputStream(out);
            byte buf[] = new byte[1024];
            do {
                // 循环读取
                int numread = is.read(buf);
                if (numread == -1) {
                    break;
                }
                fos.write(buf, 0, numread);
            } while (true);

            IOUtils.close(is);
            return IMAGE_DIR + "/" + imageName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    *//**
     * 保存下载的图片流写入SD卡文件
     *
     * @param imageName xxx.jpg
     * @param body      image stream
     *//*
    public static void writeResponseBodyToDisk(String imageName, ResponseBody body) {
        if (body == null) {
            UIUtils.showToast("图片源错误");
            return;
        }
        try {
            InputStream is = body.byteStream();
            File fileDr = new File(IMAGE_DIR);
            if (!fileDr.exists()) {
                fileDr.mkdir();
            }
            File file = new File(IMAGE_DIR, imageName);
            if (file.exists()) {
                file.delete();
                file = new File(IMAGE_DIR, imageName);
            }
            FileOutputStream fos = new FileOutputStream(file);
            BufferedInputStream bis = new BufferedInputStream(is);

            LogUtils.e(bis.toString());

            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.flush();
            fos.close();
            bis.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public interface ImageApi {
        *//**
         * 下载图片
         *
         * @return
         *//*
        @Streaming
        @GET
        Call<ResponseBody> downloadImage(@Url String fileUrl);
    }*/
}
