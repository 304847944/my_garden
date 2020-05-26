package com.liuchenxi.foundation.http.base

import android.graphics.Bitmap
import com.orhanobut.logger.Logger
import io.reactivex.Observer
import io.reactivex.disposables.Disposable

abstract class RequestCallBack<T> : Observer<T> {
    private val TAG = "RequestCallback"
    abstract fun onResponse(response: T)
    abstract fun onFailure(error: String)
    override fun onSubscribe(d: Disposable) {
        Logger.d("onSubscribe+$d")
    }

    override fun onNext(t: T) {
        Logger.d(t.toString())
        onResponse(t)
    }

    override fun onError(e: Throwable) {
        Logger.d("onError+$e")
        onFailure(e.toString())
    }

    override fun onComplete() {
        Logger.d("onComplete+完成！")
    }
}