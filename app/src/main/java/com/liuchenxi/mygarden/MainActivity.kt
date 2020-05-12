package com.liuchenxi.mygarden

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.liuchenxi.foundation.PermissionsManager
import com.liuchenxi.foundation.TextureManager
import com.liuchenxi.mygarden.base.BaseActivity
import pub.devrel.easypermissions.AfterPermissionGranted
//import com.mob.MobSDK
//import com.mob.OperationCallback
import pub.devrel.easypermissions.EasyPermissions
import pub.devrel.easypermissions.PermissionRequest


class MainActivity : BaseActivity() {

  val TAG: String = "MainActivity";
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    //展示同意协议！
    //跳转广告闪屏页！
    //权限申请！


  }

  companion object{
    const val CALL_PHONE = 100
  }


  @AfterPermissionGranted(CALL_PHONE)
  private fun callService() {
    val permissions = arrayOf(Manifest.permission.CALL_PHONE,Manifest.permission.CAMERA)
    if (EasyPermissions.hasPermissions(this, *permissions)) {
//      CommonUtil.callPhone(this@AboutActivity, aboutUsEntity.getTelephone())
    } else {
      EasyPermissions.requestPermissions(this, "请授予拨打电话权限", CALL_PHONE, *permissions)
    }

  }
  override fun onResume() {
    super.onResume()
    PermissionsManager.methodRequiresCamaraPermission(this)
    callService()
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

  override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
    PermissionsManager.onRequestPermissionsResultCamera(requestCode,permissions,grantResults)
  }
}
