package com.liuchenxi.foundation.http

import com.liuchenxi.foundation.http.base.RetrofitBuilder
import com.liuchenxi.foundation.http.getinterface.JuheNews

class RequestList {
    val mRetrofitBuilder :RetrofitBuilder by lazy {
        RetrofitBuilder()
    }

    companion object{
        val mSingleInstance : RequestList by lazy {
            RequestList()
        }
    }


    //聚合数据网拉取数据
    fun getJuheNews(): JuheNews {
        return mRetrofitBuilder.get(JuheNews::class.java)
    }

//    fun getJuheJokes(): JuheJokes {
//        return mRetrofitBuilder.get<JuheJokes>(JuheJokes::class.java)
//    }
//
//    fun getGankFuli(): gankFuli {
//        return mRetrofitBuilder.get<gankFuli>(gankFuli::class.java)
//    }

}