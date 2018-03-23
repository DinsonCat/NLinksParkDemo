package com.nlinks.parkdemo.kotlin

import com.nlinks.parkdemo.global.AppConst
import com.nlinks.parkdemo.http.HttpHelper
import com.nlinks.parkdemo.http.HttpResult
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

fun main(args: Array<String>) {
   /* val builder = OkHttpClient.Builder()
    builder.connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .addInterceptor(HttpHelper.LoggingInterceptor())//添加拦截器 日志
    val retrofit = Retrofit.Builder()
        .client(builder.build())
        .addConverterFactory(GsonConverterFactory.create())//对http请求结果进行统一的预处理
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//对rxjava提供支持
        .baseUrl(AppConst.URL_PREFIX)
        .build()


    retrofit.create(testApi::class.java).getToken()
        .subscribeOn(Schedulers.io())
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe {
            println("success")
        }*/

    println("efefef")
}


interface testApi {
    @POST("admin/oss/getUploadToken")
    fun getToken(): Observable<HttpResult<Void>>
}
