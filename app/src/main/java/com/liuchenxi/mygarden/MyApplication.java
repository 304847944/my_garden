package com.liuchenxi.mygarden;

import android.app.Activity;
import android.app.Application;
import android.graphics.Typeface;

import androidx.annotation.ColorInt;

import com.liuchenxi.foundation.BaseApplication;
import com.liuchenxi.foundation.TextureManager;

import es.dmoral.toasty.Toasty;

public class MyApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化fresco
        TextureManager.Companion.InitFresco(this);
        //设置默认toast相关
        Toasty.Config.getInstance().setToastTypeface(Typeface.DEFAULT).allowQueue(false).apply();
    }
}
