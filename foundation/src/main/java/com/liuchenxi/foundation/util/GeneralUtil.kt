package com.liuchenxi.foundation.util

import android.content.Context
import android.content.pm.ApplicationInfo
import com.liuchenxi.foundation.base.DeviceInfo

class GeneralUtil {
  //判断当前应用是否是debug状态
  companion object {
    private val sLock = Any()

    var sDeviceInfo: DeviceInfo? = null

    /**
     * 是否为Debug包
     */
    fun isApkInDebug(context: Context): Boolean {
      try {
        var info: ApplicationInfo = context.getApplicationInfo()
        return ((info.flags and ApplicationInfo.FLAG_DEBUGGABLE) != 0)
      } catch (e: Exception) {
        return false
      }
    }

    /**
     * 设备信息， 包括屏幕分辨率等参数
     */
    fun getDeviceInfo(context: Context): DeviceInfo? {
      if (sDeviceInfo == null) {
        synchronized(sLock) {
          if (sDeviceInfo == null) {
            sDeviceInfo = DeviceInfo(context)
          }
        }
      }
      return sDeviceInfo
    }
  }
}