package com.liuchenxi.mygarden

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.liuchenxi.foundation.PermissionsManagerActivity.Companion.getPermission
import com.liuchenxi.foundation.base.BaseActivity
import com.liuchenxi.foundation.dialogmanager.BaseDialog
import com.tbruyelle.rxpermissions2.RxPermissions
import es.dmoral.toasty.Toasty


//import com.mob.MobSDK
//import com.mob.OperationCallback

class MainActivity : BaseActivity() {

    val TAG: String = "MainActivity";
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //展示同意协议！
        //跳转广告闪屏页！
        //权限申请！
        val text: TextView = findViewById(R.id.text_bt)
        text.setOnClickListener(View.OnClickListener {
            Toasty.info(this, "点击按钮！").show()
        })
        getPermission()
        var mm:BaseDialog
    }

    fun getPermission() {
        var rxPermissions = RxPermissions(this)
        getPermission(rxPermissions)
    }

//  //隐私协议
//  fun submitPrivacyGrantResult(granted: Boolean) {
//    MobSDK.submitPolicyGrantResult(granted, object : OperationCallback<Void?>() {
//      override fun onFailure(p0: Throwable?) {
//        Log.d(TAG, "隐私协议授权结果提交：失败");
//      }
//
//      override fun onComplete(p0: Void?) {
//        Log.d(TAG, "隐私协议授权结果提交：成功");
//      }
//    })
//  }
}
