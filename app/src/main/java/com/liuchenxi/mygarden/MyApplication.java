package com.liuchenxi.mygarden;

import android.app.Application;

import com.liuchenxi.foundation.TextureManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        //初始化fresco
        TextureManager.Companion.InitFresco(this);
    }
}
