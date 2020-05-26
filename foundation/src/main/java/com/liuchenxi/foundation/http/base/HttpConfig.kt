package com.liuchenxi.foundation.http.base

class HttpConfig{
    companion object{
        val DEBUG :Boolean = true
        val mHttpHeader: HttpHeader by lazy{
            val aaa:String = ""
            HttpHeader()
        }
    }

    class HttpHeader{
        val CONTENT_TYPE = "Content-Type"
        val AUTHORIZATION = "Authorization"
        val HTTP_TIMESTAMP = "Http-Timestamp"
        val HTTP_APP_VERSION = "Http-App-Version"
        val HTTP_APP_KEY = "Http-App-Key"
        val HTTP_DEVICE_ID = "Http-Device-Id"
        val HTTP_DEVICE_TYPE = "Http-Device-Type"
        val HTTP_SIGNATURE = "Http-Signature"
    }
}