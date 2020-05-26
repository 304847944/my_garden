package com.liuchenxi.foundation.http.getinterface

import com.liuchenxi.foundation.http.module.BaseData
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

interface JuheNews{
    @GET("toutiao/index")
    abstract fun getJuheNews(@Query("type") type: String, @Query("key") key: String): Observable<BaseData>
}