package com.liuchenxi.foundation.dialogmanager;

import android.content.Context;
import android.text.method.ScrollingMovementMethod;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.liuchenxi.foundation.BaseApplication;
import com.liuchenxi.foundation.base.BaseActivity;
import com.liuchenxi.foundation.base.DeviceInfo;

/**
 * Desc:568新弹窗-可复用部分（用于内容可以滑动的样式具体详见：），使用文档请参照代码及页面：http://confluence.cmvideo.cn/confluence/pages/viewpage.action?pageId=37363957
 *
 * Date: 2019-11-05
 * Company: @咪咕视讯
 *
 * @author [liumingxin]
 */
public class GeneralScrollDialog extends BaseDialog {

    public GeneralScrollDialog(@NonNull Context context, String mTitle, String mContent,
                               String mFirstButton, String mSecondButton,
                               BaseDialogListen mBaseDialogListen) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mBaseDialogListen);
    }

    public GeneralScrollDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton);
    }

    public GeneralScrollDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton, boolean isVip, boolean isHaveClose) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, isVip, isHaveClose);
    }

    public GeneralScrollDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton, String mThirdlyButton, boolean isVip,
        boolean isHaveClose) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
            isHaveClose);
    }
    public void setOnClickListen(final BaseDialogListenOne mBaseDialogListen) {
        getFirstButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBaseDialogListen.firstOrLeft();
            }
        });

    }

    @Override public void initData() {
        super.initData();
        ConstraintLayout.LayoutParams mDialogCL =
            (ConstraintLayout.LayoutParams) mDialogBgView.getLayoutParams();
        mDialogCL.width = dipToPx(mContext, 300);
        try {
            if(DeviceInfo.bIsPortrait(BaseApplication.mCurrentActivity))
            {
                mDialogCL.matchConstraintMaxHeight = (int) (DeviceInfo.getDeviceHeight()*0.45f);
                mContentView.setMaxHeight((int) (DeviceInfo.getDeviceHeight()*0.39f));
            }else {
                mDialogCL.matchConstraintMaxHeight = (int) (DeviceInfo.getDeviceWidth()*0.45f);
                mContentView.setMaxHeight((int) (DeviceInfo.getDeviceWidth()*0.39));
            }
        }catch (Exception e){
            mDialogCL.matchConstraintMaxHeight = dipToPx(mContext,300);
            mContentView.setMaxHeight((int) (DeviceInfo.getDeviceWidth()*0.39));
        }
        //mDialogCL.matchConstraintMaxHeight = 850;
        this.mDialogBgView.setLayoutParams(mDialogCL);
        mContentView.setMovementMethod(ScrollingMovementMethod.getInstance());
        mContentView.setScrollbarFadingEnabled(false);
        mContentBgView.setVisibility(View.VISIBLE);
        mDialogRootView.invalidate();
        setOrientationChange(new BaseDialog.OrientationChange() {
            @Override
            public void orientationChange() {
                dismiss();
            }
        });
    }
}