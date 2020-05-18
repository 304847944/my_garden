package com.liuchenxi.foundation.dialogmanager;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.liuchenxi.foundation.R;

/**
 * Desc:568新弹窗-可复用部分，使用文档请参照代码及页面：http://confluence.cmvideo.cn/confluence/pages/viewpage.action?pageId=37363957
 *
 * Date: 2019-11-05
 * Company: @咪咕视讯
 *
 * @author [liumingxin]
 */
public class HeadPortraitDialog extends BaseDialog {


    public HeadPortraitDialog(@NonNull Context context, String mTitle, String mContent,
                              String mFirstButton, String mSecondButton, String mThirdlyButton, String mHeadPortrait, boolean isVip,
                              boolean isHaveClose, BaseDialogListen mBaseDialogListen) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
            isHaveClose, null,mHeadPortrait, null, mBaseDialogListen);
    }

    public HeadPortraitDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton, String mThirdlyButton,String mHeadPortrait, boolean isVip,
        boolean isHaveClose) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
            isHaveClose, null,mHeadPortrait, null, null);
    }

    public HeadPortraitDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mHeadPortrait, boolean isVip,
        boolean isHaveClose) {
        super(context, mTitle, mContent, mFirstButton, null, null, isVip,
            isHaveClose, null,mHeadPortrait, null, null);
    }

    @Override public void initData() {
        super.initData();
        ConstraintLayout.LayoutParams mDialogCL =
            (ConstraintLayout.LayoutParams) mTitleView.getLayoutParams();
        mDialogCL.topMargin = dipToPx(mContext, 46);
        mTitleView.setLayoutParams(mDialogCL);

        mBgImageView.setImageResource(R.drawable.icon_group_add_new);
        mHeadPortraitBgView.setImageResource(R.drawable.base_dialog_headportrait_bg);
        mBgImageView.setVisibility(View.VISIBLE);
        mBgImageView.setMaxWidth(dipToPx(mContext, 300));
        mHeadPortraitBgView.setVisibility(View.VISIBLE);

        mHeadPortraitView.setVisibility(View.VISIBLE);
        mHeadPortraitView.setImageURI(mHeadPortrait);
        ConstraintLayout.LayoutParams mDialogBgCL =
            (ConstraintLayout.LayoutParams) mDialogBgView.getLayoutParams();
        mDialogBgCL.topToTop = mSpaceView.getId();
        mDialogBgCL.topMargin = dipToPx(mContext, 64);
        mDialogBgCL.width = dipToPx(mContext, 300);
        mDialogBgView.setLayoutParams(mDialogBgCL);
        mDialogRootView.invalidate();
        mHeadPortraitBgView.invalidate();
        mBgImageView.invalidate();
    }

    public void setOnClickListen(final BaseDialogListenOne mBaseDialogListen) {
        getFirstButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBaseDialogListen.firstOrLeft();
            }
        });

    }
}