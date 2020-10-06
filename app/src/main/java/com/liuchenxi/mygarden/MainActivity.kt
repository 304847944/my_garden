package com.liuchenxi.mygarden

import android.os.Bundle
import android.view.TextureView
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.liuchenxi.foundation.PermissionsManager
import com.liuchenxi.foundation.base.BaseActivity
import com.liuchenxi.foundation.texture.CameraRenderer
import com.liuchenxi.mygarden.databinding.ActivityMainBinding


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
//        mTopTab = rootView.mainTopTab
//        mViewPage = rootView.mainViewpage
//        mBottomLayout = rootView.activitybottom
        //展示同意协议！
        //跳转广告闪屏页！
        //权限申请！
//        getPermission()
//        dialogTest()
        //初始化fragment页面
        rootView.testButton.setOnClickListener(View.OnClickListener {
            initCamaraTest()
        })
    }

    lateinit var mCanara:CameraRenderer
    lateinit var textureView:TextureView
    fun initCamaraTest(){
        textureView = TextureView(this)
        rootView.cameraPaper.addView(textureView)
        mCanara = CameraRenderer.mCameraRenderer
        mCanara.CameraRenderInit(this)
        textureView.surfaceTextureListener = mCanara
        textureView.addOnLayoutChangeListener { v, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom ->
            mCanara.onSurfaceTextureSizeChanged(
                null,
                v.width,
                v.height
            )
        }
    }

    override fun onResume() {
        super.onResume()
        getPermission()
    }

    fun getPermission() {
        PermissionsManager.instance.getPermission()
    }

//    fun dialogTest(){
//        //弹窗测试
//        val mm:GeneralDialog = GeneralDialog(this,"这是一个测试","hahahahahahahhaha","确定","取消")
//        mm.setOnClickListen(object : BaseDialog.BaseDialogListenTwo {
//            override fun firstOrLeft() {
//                Toasty.info(MyApplication.mCurrentActivity,"left！").show()
//            }
//
//            override fun secondOrRight() {
//                mm.dismiss()
//            }
//        })
//    }
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
//
//    private fun initData() {
//
//        val fragments = ArrayList<Fragment>()
//        fragments.add(Mainpagefragment01())
//        fragments.add(Mainpagefragment01())
//        fragments.add(Mainpagefragment01())
//        fragments.add(Mainpagefragment01())
//        fragments.add(Mainpagefragment01())
//
//        mViewPage!!.setAdapter(object : FragmentPagerAdapter(supportFragmentManager) {
//            override fun getItem(position: Int): Fragment {
//                return fragments[position]
//            }
//
//            override fun getCount(): Int {
//                return fragments.size
//            }
//        }
//        )
//        mTopTab?.setupWithViewPager(mViewPage)
//        mTopTab?.addOnTabSelectedListener(object : TabLayout.BaseOnTabSelectedListener<TabLayout.Tab?> {
//            override fun onTabReselected(p0: TabLayout.Tab?) {
//                Toasty.info(rootView.root.context,this.javaClass.name + "onTabReselected").show()
//            }
//
//            override fun onTabUnselected(p0: TabLayout.Tab?) {
//                Toasty.info(rootView.root.context,this.javaClass.name + "onTabUnselected").show()
//            }
//
//            override fun onTabSelected(p0: TabLayout.Tab?) {
//                Toasty.info(rootView.root.context,this.javaClass.name + "onTabSelected").show()
//                RequestList.mSingleInstance.getJuheNews()
//                    .getJuheNews("yule", "3fea208f1bec73e501a802af8514bfa6")
//                    .map({ t: BaseData ->
//                        Gson().fromJson(t.result, Response_JuheNews::class.java)
//                    })
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe( object : RequestCallBack<Response_JuheNews?>() {
//                        override fun onResponse(response: Response_JuheNews?) {
//                            Logger.d("onResponse:" + response?.data?.get(0)?.uniquekey)
//                        }
//
//                        override fun onFailure(error: String) {
//                            Logger.d("onFailure:" + error.toString())
//                        }
//                    })
//            }
//        })
//
////        //模拟数据
//        var mTitleList: ArrayList<String> = ArrayList<String>()
//        mTitleList.add("图文1")
//        mTitleList.add("图文2")
//        mTitleList.add("图文2")
//        mTitleList.add("图文2")
//        mTitleList.add("图文2")
//        for (i: Int in 0 until mTitleList!!.size) {
////            mTopTab!!.getTabAt(i)!!.setCustomView(R.layout.tab_indicator_view_auto_repay)
//            mTopTab!!.getTabAt(i)
//                ?.setText(mTitleList.get(i))
//        }
//        Logger.d("hello,欢迎来到cwzj")//测试Logger
//    }


}
