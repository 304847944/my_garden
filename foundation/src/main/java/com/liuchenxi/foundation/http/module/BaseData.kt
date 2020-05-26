package com.liuchenxi.foundation.http.module

import com.google.gson.JsonArray
import com.google.gson.JsonObject

data class BaseData(
    var reason: String="",
    //    @SerializedName("result")
    var result: JsonObject,
    var error_code:Int = 0,
    var results: JsonArray,
    var error:Boolean=false
)