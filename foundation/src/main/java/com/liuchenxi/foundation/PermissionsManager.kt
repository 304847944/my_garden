package com.liuchenxi.foundation;

import android.Manifest;
import android.annotation.SuppressLint
import android.app.Activity
import com.liuchenxi.foundation.BaseApplication.mCurrentActivity
import com.liuchenxi.foundation.base.BaseActivity
import com.tbruyelle.rxpermissions2.RxPermissions
import es.dmoral.toasty.Toasty
import io.reactivex.functions.Consumer


/**
 * 权限申请
 */
class PermissionsManager {
    companion object {
        val instance = SingletonHolder.holder
    }

    private object SingletonHolder {
        val holder= PermissionsManager()
    }

    val RC_CAMERA_AND_LOCATION: Int = 201 // 相机

    lateinit var rxPermissions: RxPermissions

    @SuppressLint("CheckResult")
    fun getPermission(rxPermissions: RxPermissions) {
        rxPermissions.requestEachCombined(
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE
        )
            .subscribe(Consumer {
                if (it.granted) {
                    // 用户已经同意该权限
                    // Log.d(TAG, permission.name + " is granted.");
                } else if (it.shouldShowRequestPermissionRationale) {
                    // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                    // Log.d(TAG, permission.name + " is denied. More info should
                    // be provided.");
                } else {
                // 用户拒绝了该权限，并且选中『不再询问』
                    // Log.d(TAG, permission.name + " is denied.");
                }
            })
    }

    fun checkPermission(mPermission: String) {
        rxPermissions.isGranted(mPermission)
    }

    init {

    }

}
