package com.liuchenxi.foundation;

import android.app.Activity;
import android.app.Application;

import com.liuchenxi.foundation.TextureManager;

public abstract class BaseApplication extends Application {

    public static Activity mCurrentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        //初始化fresco
        TextureManager.Companion.InitFresco(this);
    }
}
