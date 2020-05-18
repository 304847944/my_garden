package com.liuchenxi.foundation.dialogmanager;

import android.content.Context;
import android.text.TextUtils;
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
public class ImageDialog extends BaseDialog {

    public ImageDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton, String mThirdlyButton, boolean isVip,
        boolean isHaveClose, String mTopImageUrl, String mContentLevel2) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
            isHaveClose, mTopImageUrl,null, mContentLevel2,null);
    }

    public ImageDialog(@NonNull Context context, String mTitle, String mContent,
                       String mFirstButton, String mSecondButton, String mThirdlyButton, boolean isVip,
                       boolean isHaveClose, String mTopImageUrl, String mContentLevel2, BaseDialogListen mBaseDialogListen) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
            isHaveClose, mTopImageUrl,null, mContentLevel2,mBaseDialogListen);
    }


    @Override protected void setTopImageView() {
        if (!TextUtils.isEmpty(this.mTopImageUrl)) {
            ConstraintLayout.LayoutParams mDialogCL =
                (ConstraintLayout.LayoutParams) mDialogBgView.getLayoutParams();
            mDialogCL.width = dipToPx(mContext, 300);
            this.mDialogBgView.setLayoutParams(mDialogCL);
            mTopImageView.setVisibility(View.VISIBLE);
            mTopImageView.setImageURI(mTopImageUrl);
            setContentLevel2View();
            mDialogBgView.setBackgroundResource(R.drawable.base_dialog_notop_corner);
        } else {
            mTopImageView.setVisibility(View.GONE);
        }
    }

    @Override protected void setContentLevel2View() {
        if (!TextUtils.isEmpty(mContentLevel2)) {
            mContentLevel2View.setVisibility(View.VISIBLE);
            mContentLevel2View.setText(mContentLevel2);
        }
    }
}