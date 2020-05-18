package com.liuchenxi.foundation.dialogmanager;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Desc:568新弹窗-可复用部分，使用文档请参照代码及页面：http://confluence.cmvideo.cn/confluence/pages/viewpage.action?pageId=37363957
 *
 * Date: 2019-11-05
 * Company: @咪咕视讯
 *
 * @author [liumingxin]
 */
public class GeneralDialog extends BaseDialog {

    /**
     * Desc: /**
     *      * Desc:baseDialog构造（以下参数，没有就传空）
     *      * mTitle：主标题
     *      * mContent：内容
     *      * mFirstButton ：从左往右，从上到下 第一个按钮
     *      * mSecondButton ：从左往右，从上到下 第二个按钮
     *      * mBaseDialogListen ： 按键监听
     *      * Author: [liumingxin]
     *      * Date: 2019-11-13
     *      */
    public GeneralDialog(@NonNull Context context, String mTitle, String mContent,
                         String mFirstButton, String mSecondButton,
                         BaseDialogListen mBaseDialogListen) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mBaseDialogListen);
    }

    /**
     * Desc: /**
     *      * Desc:baseDialog构造（以下参数，没有就传空）
     *      * mTitle：主标题
     *      * mContent：内容
     *      * mFirstButton ：从左往右，从上到下 第一个按钮
     *      * mSecondButton ：从左往右，从上到下 第二个按钮
     *
     * Author: [liumingxin]
     * Date: 2019-11-13
     */
    public GeneralDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton);
    }

    /**
     * Desc: /**
     *      * Desc:baseDialog构造（以下参数，没有就传空）
     *      * mTitle：主标题
     *      * mContent：内容
     *      * mFirstButton ：从左往右，从上到下 第一个按钮
     *      * mSecondButton ：从左往右，从上到下 第二个按钮
     *      *isVip ：按钮是否用vip颜色样式，false显示蓝色，true显示金色
     *      * isHaveClose ： 是否有关闭按钮
     *
     * Author: [liumingxin]
     * Date: 2019-11-13
     */
    public GeneralDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton, boolean isVip, boolean isHaveClose) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, isVip, isHaveClose);
    }
    /**
     * Desc:baseDialog构造（以下参数，没有就传空）
     * mTitle：主标题
     * mContent：内容
     * mFirstButton ：从左往右，从上到下 第一个按钮
     * mSecondButton ：从左往右，从上到下 第二个按钮
     * mThirdlyButton ： 从左往右，从上到下 第三个按钮
     *isVip ：按钮是否用vip颜色样式，false显示蓝色，true显示金色
     * isHaveClose ： 是否有关闭按钮
     * Author: [liumingxin]
     * Date: 2019-11-13
     */
    public GeneralDialog(@NonNull Context context, String mTitle, String mContent,
        String mFirstButton, String mSecondButton, String mThirdlyButton, boolean isVip,
        boolean isHaveClose) {
        super(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
            isHaveClose);
    }

    /**
     * Desc:只有一个按钮的监听（用其他监听也不会错，不填就行）
     *
     * Author: [liumingxin]
     * Date: 2019-11-13
     */
    public void setOnClickListen(final BaseDialogListenOne mBaseDialogListen) {
        getFirstButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBaseDialogListen.firstOrLeft();
            }
        });

    }
    public void setOnClickListen(final BaseDialogListenTwo mBaseDialogListen) {
        getFirstButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBaseDialogListen.firstOrLeft();
            }
        });
        getSecondButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBaseDialogListen.secondOrRight();
            }
        });

    }
}