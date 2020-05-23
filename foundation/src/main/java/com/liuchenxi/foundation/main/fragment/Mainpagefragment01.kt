package com.liuchenxi.foundation.main.fragment

import android.util.Log
import android.view.View
import com.liuchenxi.foundation.R
import com.liuchenxi.foundation.base.BaseFragment
import com.orhanobut.logger.Logger
import es.dmoral.toasty.Toasty
import java.util.ArrayList

class Mainpagefragment01 : BaseFragment(){
//    lateinit var mHomeAdapter: HomeAdaptertest
    override fun lazyLoad() {
        Logger.w("Fragment1" + (if (isInit) "已经初始并已经显示给用户可以加载数据" else "没有初始化不能加载数据") + ">>>>>>>>>>>>>>>>>>>")
//        initRecycleview()
//        ButtonRecycleTest()
    }

    override fun setContentView(): Int {
        return R.layout.fragment01xml
    }

    override fun onResume() {
        val message = "Fragment1onResume"
        Logger.w(message)
        super.onResume()
    }
//
//    fun ButtonRecycleTest() {
//        //var mB: Button = binding.button;
//        //var imageTest: ImageView = binding.imageView;
//        //获取新闻相关内容
//        getJuheNews.GetNewsTest(object : Base_observer<Response_News> {
//            override fun onSuccess(data: Response_News?) {
//                LogUtil.ShowLog("成功 ！！！！！！！！！！")
//                mHomeAdapter.replaceData(data?.data!!)
//            }
//            override fun onFail() {
//                LogUtil.ShowLog("失败 ！！！！！！！！！！")
//            }
//        });
//    }
//
//    fun initRecycleview() {
//        recyclerView = findViewById(R.id.recy)
//        // mswipeLayout = findViewById(R.id.swipeLayout)
////        mswipeLayout.setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener {
////            showToast( "SwipeLayout")
////        })
////        mswipeLayout.setColorSchemeColors(Color.rgb(47, 223, 189))
//        val layoutManager = LinearLayoutManager(this.context)
//        //设置布局管理器
//        recyclerView.setLayoutManager(layoutManager)
//        //设置为垂直布局，这也是默认的
//        layoutManager.orientation = OrientationHelper.VERTICAL
//        //设置Adapter
//        mHomeAdapter = HomeAdaptertest(R.layout.item_news, null)
//        recyclerView.setAdapter(mHomeAdapter)
//        //设置分隔线
//        //recyclerView.addItemDecoration(DividerGridItemDecoration(this))
//        //设置增加或删除条目的动画
//        recyclerView.setItemAnimator(DefaultItemAnimator())
//        mHomeAdapter.setOnItemClickListener(object : BaseQuickAdapter.OnItemClickListener {
//            override fun onItemClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//                LogUtil.ShowLog("我点击了：" + view + "--" + position)
//            }
//        })
//        mHomeAdapter.setOnItemChildClickListener(object : BaseQuickAdapter.OnItemChildClickListener {
//            override fun onItemChildClick(adapter: BaseQuickAdapter<*, *>?, view: View?, position: Int) {
//                LogUtil.ShowLog("onItemChildClick: " + "position-" + position + "--view-" + view);
//                if (view?.id == R.id.news_pic) {
//                    LogUtil.ShowLog("onItemChildClick: " + "我点击了图片");
//                }
//            }
//        })
//        //添加动画
//        mHomeAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN)
//        //添加头、尾
////        var view: View = layoutInflater.inflate(R.layout.item_news, recyclerView.parent as ViewGroup, false)
////        (view.findViewById<ImageView>(R.id.news_pic)).setImageResource(R.mipmap.ic_launcher)
////
////        mHomeAdapter.addHeaderView(view);
//        // mHomeAdapter.addFooterView(view);
//        //下拉加载
////        mHomeAdapter.setOnLoadMoreListener(BaseQuickAdapter.RequestLoadMoreListener {
////           showToast("家在更是多！！！")
////        });
//
////        //上拉加载
////        mHomeAdapter.setUpFetchEnable(true);
////        mHomeAdapter.setUpFetchListener(BaseQuickAdapter.UpFetchListener() {
////            showToast("上拉加载更多！")
////        });
//    }

}
