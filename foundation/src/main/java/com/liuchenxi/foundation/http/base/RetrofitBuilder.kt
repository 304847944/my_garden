package com.liuchenxi.foundation.http.base

import android.content.Context
import android.util.Log
import com.orhanobut.logger.Logger
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.*
import java.util.concurrent.TimeUnit
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.PrettyFormatStrategy
import com.orhanobut.logger.FormatStrategy
import com.orhanobut.logger.LogStrategy

//缺少日志存储到本地（日志的拓展，查下logger是否支持）
//缺少网络缓存（okhttp的chach已经有了，测试下要）
class RetrofitBuilder {
   private var mCacheLocation: File? //这里写死好了！
   private var mToken:String = ""
   private @Volatile var mRetrofit: Retrofit? = null
   private val mHttpLogger: com.liuchenxi.foundation.http.base.HttpLoggingInterceptor by lazy {
       com.liuchenxi.foundation.http.base.HttpLoggingInterceptor()
   }
   private var URL_TIMEOUT: Int = 10
   private var READ_TIMEOUT: Int = 10
   private val DEVICE_TYPE = "android"
   private var BaseUrl = "http://v.juhe.cn/"
   private var BaseUrl2 = "http://v.juhe.cn/joke/content"
    constructor(cacheLocationFile: File) {
        mCacheLocation = cacheLocationFile
    }

    constructor(){
        mCacheLocation = null
    }

    fun setToken(token:String): RetrofitBuilder {
        mToken = token
        mRetrofit = null
        return this
    }

    //OkHttpClient与buidle的关系，与builder.build的关系
    //chain和日志拦截器的原理
    //进一步梳理责任链机制
    fun getRetrofitClient(): OkHttpClient {
        var mBuidle: OkHttpClient.Builder = OkHttpClient.Builder()

        //设置网络数据缓存
        if (mCacheLocation != null) {
            var cacheDir: File = File(mCacheLocation, UUID.randomUUID().toString())
            val cache = Cache(cacheDir, 1024)
            mBuidle.cache(cache)
        }

        // 设置超时时间
        mBuidle.connectTimeout(URL_TIMEOUT.toLong(), TimeUnit.SECONDS)//链接超时
        mBuidle.readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)//读取超时

        //添加请求头
        mBuidle.addInterceptor(Interceptor { chain ->
            val newRequest = chain.request().newBuilder()
            newRequest.addHeader(
                HttpConfig.mHttpHeader.HTTP_TIMESTAMP,
                System.currentTimeMillis().toString()
            )
            //newRequest.addHeader(Config.Header.HTTP_APP_VERSION, SystemUtil.getAppVersionName(mContext));
            //newRequest.addHeader(Config.Header.HTTP_APP_KEY, AppConfig.APP_KEY);
            //newRequest.addHeader(Config.Header.HTTP_DEVICE_ID, SystemUtil.getDeviceId(mContext));
            newRequest.addHeader(HttpConfig.mHttpHeader.HTTP_DEVICE_TYPE, DEVICE_TYPE)
            if (mToken != null) {
                newRequest.addHeader(HttpConfig.mHttpHeader.AUTHORIZATION, "Liu $mToken")
            }
            chain.proceed(newRequest.build())
        })
        //添加日志拦截器
//        val logging = HttpLoggingInterceptor(mHttpLogger)
////        logging.setLevel(HttpLoggingInterceptor.Level.BASIC)
//        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
//        logging.setLevel(if (HttpConfig.DEBUG) HttpLoggingInterceptor.Level.HEADERS else HttpLoggingInterceptor.Level.NONE)
//        logging.setLevel(if (HttpConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE)
        mBuidle.addInterceptor(mHttpLogger)

        return mBuidle.build()
    }

    fun getRetrofit(url: String): Retrofit {
            return mRetrofit ?: Retrofit.Builder()
                .client(getRetrofitClient())
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
    }

    fun getRetrofit(): Retrofit {
        return mRetrofit ?: Retrofit.Builder()
            .client(getRetrofitClient())
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
    }

    //API接口.执行！
    operator fun <T> get(clazz: Class<T>): T {
        return getRetrofit().create(clazz)
    }

    //API接口.执行！
    operator fun <T> get(clazz: Class<T>, url: String): T {
        return getRetrofit(url).create(clazz)
    }

}