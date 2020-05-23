package com.liuchenxi.mygarden

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.liuchenxi.foundation.BaseApplication
import com.liuchenxi.foundation.PermissionsManager
import com.liuchenxi.foundation.base.BaseActivity
import com.liuchenxi.foundation.base.DeviceInfo
import com.liuchenxi.foundation.dialogmanager.BaseDialog
import com.liuchenxi.foundation.dialogmanager.GeneralDialog
import com.liuchenxi.foundation.main.fragment.Mainpagefragment01
import com.liuchenxi.foundation.util.GeneralUtil
import com.liuchenxi.mygarden.databinding.ActivityMainBinding
import com.orhanobut.logger.Logger
import com.tbruyelle.rxpermissions2.RxPermissions
import es.dmoral.toasty.Toasty
import java.util.ArrayList


//import com.mob.MobSDK
//import com.mob.OperationCallback

class MainActivity : BaseActivity() {
    lateinit var rootView: ActivityMainBinding
    val TAG: String = "MainActivity";
    private var mTopTab: TabLayout? = null
    private var mViewPage: ViewPager? = null
    private var mBottomLayout: View? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        rootView = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setContentView(R.layout.activity_main)
        //展示同意协议！
        //跳转广告闪屏页！
        //权限申请！
        getPermission()
        dialogTest()
    }

    fun getPermission() {
        PermissionsManager.instance.getPermission()
    }

    fun dialogTest(){
        //弹窗测试
        val mm:GeneralDialog = GeneralDialog(this,"这是一个测试","hahahahahahahhaha","确定","取消")
        mm.setOnClickListen(object : BaseDialog.BaseDialogListenTwo {
            override fun firstOrLeft() {
                Toasty.info(MyApplication.mCurrentActivity,"left！").show()
            }

            override fun secondOrRight() {
                mm.dismiss()
            }
        })
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

    private fun initData() {
        val fragments = ArrayList<Fragment>()
        fragments.add(Mainpagefragment01())
        fragments.add(Mainpagefragment01())
        fragments.add(Mainpagefragment01())
        fragments.add(Mainpagefragment01())
        fragments.add(Mainpagefragment01())

        mViewPage?.setOffscreenPageLimit(2)
        mViewPage?.setAdapter(object : FragmentPagerAdapter(supportFragmentManager) {
            override fun getItem(position: Int): Fragment {
                return fragments[position]
            }

            override fun getCount(): Int {
                return fragments.size
            }
        }
        )
        mTopTab?.setupWithViewPager(mViewPage)
        mTopTab?.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab?> {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                Toasty.info(rootView.root.context,this.javaClass.name + "onTabReselected").show()
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
                Toasty.info(rootView.root.context,this.javaClass.name + "onTabUnselected").show()
            }

            override fun onTabSelected(p0: TabLayout.Tab?) {
                Toasty.info(rootView.root.context,this.javaClass.name + "onTabSelected").show()
            }
        })

        //模拟数据
        var mTitleList: ArrayList<String> = ArrayList<String>()
        mTitleList.add("图文1")
        mTitleList.add("图文2")
        mTitleList.add("图文3")
        mTitleList.add("图文4")
        mTitleList.add("图文5")
        mTitleList.add("图文6")
        for (i: Int in 0 until mTitleList!!.size) {
            mTopTab?.getTabAt(i)
                ?.setText(mTitleList.get(i))
        }
        Logger.d("hello,欢迎来到cwzj")//测试Logger
    }
}
