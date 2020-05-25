package com.liuchenxi.foundation.base

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import com.liuchenxi.foundation.BaseApplication

open class BaseActivity : AppCompatActivity() {

    private var sm: SensorManager? = null
    private var mSensor: Sensor? = null

    // 监听要是写了就会触发！不写不触发！目前是在onresume了，需要优化再说
    private var mOrientationListener: OrientationSensorListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        initTopbar()
//        sm = this.getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        mSensor = sm?.getDefaultSensor(Sensor.TYPE_GRAVITY)
//        BaseApplication.mCurrentActivity = this

    }

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
//        BaseApplication.mCurrentActivity = this
//        if (mOrientationListener != null) {
//            sm?.registerListener(mOrientationListener, mSensor, SensorManager.SENSOR_DELAY_UI)
//        }
    }

    override fun onPause() {
        super.onPause()
//        if (mOrientationListener != null) {
//            sm?.unregisterListener(mOrientationListener, mSensor)
//        }
    }

    //模仿bilibili的top！
    fun initTopbar() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(true)
    }

    //模仿bilibili的top！
    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {
//        menuInflater.inflate(R.menu.test_menu, menu)
        return true
    }

    /**
     * 设置屏幕旋转监听器
     * @param orientationListener
     */
    protected fun setOrientationListener(orientationListener: OrientationSensorListener) {

        if (mOrientationListener != null) {
            sm?.unregisterListener(mOrientationListener, mSensor)
        }

        mOrientationListener = orientationListener
        if (mOrientationListener != null) {
            sm?.registerListener(mOrientationListener, mSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }
}