package com.liuchenxi.mygarden

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.liuchenxi.foundation.TextureManager
//import com.mob.MobSDK
//import com.mob.OperationCallback
import pub.devrel.easypermissions.EasyPermissions



class MainActivity : AppCompatActivity() {

  val TAG: String = "MainActivity";
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    //展示同意协议！
    //跳转广告闪屏页！
    //权限申请！


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

  //权限申请
  override fun onRequestPermissionsResult(
    requestCode: Int,
    permissions: Array<String>,
    grantResults: IntArray
  ) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    // Forward results to EasyPermissions
    EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
  }
}
