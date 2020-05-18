package com.liuchenxi.foundation;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.liuchenxi.foundation.TextureManager;

public abstract class BaseApplication extends Application {

    public static Activity mCurrentActivity;
    public static Context mApplicationContext = null;
    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationContext = this;
        //初始化fresco
        TextureManager.Companion.InitFresco(this);
    }
}
