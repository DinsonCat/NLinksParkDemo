package com.nlinks.parkdemo.http;


import com.nlinks.parkdemo.global.AppConst;
import com.nlinks.parkdemo.utils.LogUtils;

import java.io.IOException;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Retrofit统一封装设置,返回retrofit对象
 */
public class HttpHelper {

    private static final int DEFAULT_TIMEOUT = 60;

    private Retrofit retrofit;


    //构造方法私有
    private HttpHelper() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new LoggingInterceptor());//添加拦截器 日志

        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())//对http请求结果进行统一的预处理
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxjava提供支持
                .baseUrl(AppConst.URL_PREFIX)
                .build();
    }

    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpHelper INSTANCE = new HttpHelper();
    }

    //获取Retrofit
    public static Retrofit getRetrofit() {
        return SingletonHolder.INSTANCE.retrofit;
    }



    /**
     * 请求日志
     */
    public static class LoggingInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            LogUtils.i("===================================================================="  );
            LogUtils.v(request.toString());
            long t1 = System.nanoTime();
            Response response = chain.proceed(chain.request());
            long t2 = System.nanoTime();
            LogUtils.v(String.format(Locale.getDefault(), "Received response in %.1fms", (t2 - t1) / 1e6d));
            okhttp3.MediaType mediaType = response.body().contentType();
            String content = response.body().string();
            LogUtils.v("response body:" + content);

            Response.Builder builder = response.newBuilder();
            if (response.code() == 401) {
                LogUtils.e("change");
                builder.code(200);
                content = "{\"statusCode\": 10086,\"data\": {},\"statusMsg\": \"Token过期\"}";
            }

            LogUtils.i("====================================================================");

            return builder.body(okhttp3.ResponseBody.create(mediaType, content)).build();
        }
    }
}
