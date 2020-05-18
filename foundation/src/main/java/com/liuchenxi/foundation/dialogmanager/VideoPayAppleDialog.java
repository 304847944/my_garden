package com.liuchenxi.foundation.dialogmanager;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * @author wenyuan
 * 2019.11.14修改 by: liumingxin，使用文档请参照代码及页面：http://confluence.cmvideo.cn/confluence/pages/viewpage.action?pageId=37363957
 */
public class VideoPayAppleDialog extends BaseDialog {

    /**
     * Desc:注意这里的vip为，按钮是否颜色是vip的金色，false为普通弹窗的蓝色！
     *
     * Author: [liumingxin]
     * Date: 2019-11-13
     */
    public VideoPayAppleDialog(@NonNull Context context, String level2,
                               String level3, String level4, String level5, String mFirstButton, String mSecondButton, String mThirdButton,
                               boolean isVip, boolean isHaveClose,
                               BaseDialogListen mBaseDialogListen) {
        super(context, null,null,level2,level3,level4,level5,mFirstButton,mSecondButton,mThirdButton,isVip,isHaveClose,null,null,mBaseDialogListen);
    }


    @Override public void initData() {
        super.initData();
        mLevelParentView.setVisibility(View.VISIBLE);
    }

    //只设置标题
    public void setMessage(String title){
        // 设置值 标题
        if (mContentLevel2View != null) {
            mContentLevel2View.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                mContentLevel2View.setText(Html.fromHtml(title));
                mContentLevel2View.setGravity(Gravity.CENTER);
            }
        }
    }

    public void setMessage(String title,int neededPrice,String deadLine){
        // 设置值 标题
        if (mContentLevel2View != null) {
            mContentLevel2View.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                mContentLevel2View.setText(Html.fromHtml(title));
            }
        }
        // 价格
        if (mContentLevel3View != null) {
            String strPrice = "";
            if (neededPrice > 0) {
                strPrice = "需购买价格：<font color=#C4852F> " + neededPrice + "元 </font>";
            }
            mContentLevel3View.setVisibility(TextUtils.isEmpty(strPrice) ? GONE : VISIBLE);
            mContentLevel3View.setText(Html.fromHtml(strPrice));
        }
        // 拥有的券
        if (mContentLevel4View != null) {
            mContentLevel4View.setVisibility(!TextUtils.isEmpty(deadLine) ? VISIBLE : GONE);
            if (!TextUtils.isEmpty(deadLine)) {
                String strHasTicket = "下载有效期：<font color=#C4852F> " + deadLine + "</font>";
                mContentLevel4View.setText(Html.fromHtml(strHasTicket));
            }
        }
    }
    //下载用券弹窗设置
    public void setMessageForDownload(String title,int needPrice,int needTicket,int hasTicket,String deadline){
        // 设置值 标题
        if (mContentLevel2View != null) {
            mContentLevel2View.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                mContentLevel2View.setText(Html.fromHtml(title));
            }
        }
        // 价格
        if (mContentLevel3View != null) {
            String strPrice = "";
            String strTickek = "";
            if (needPrice > 0) {
                strPrice = "需购买价格：<font color=#C4852F> " + needPrice + "元 </font>";
            }
            if (needTicket > 0) {
                strTickek = " (需<font color=#C4852F>" + needTicket + "张</font>通看券)";
            }
            String concat = strPrice.concat(strTickek);
            mContentLevel3View.setVisibility(TextUtils.isEmpty(concat) ? GONE : VISIBLE);
            mContentLevel3View.setText(Html.fromHtml(concat));
        }
        // 拥有的券
        if (mContentLevel4View != null) {
            mContentLevel4View.setVisibility(hasTicket >= 0 ? VISIBLE : GONE);
            if (hasTicket >= 0) {
                String strHasTicket = "您剩余通看券：<font color=#C4852F> " + hasTicket + "张</font>";
                mContentLevel4View.setText(Html.fromHtml(strHasTicket));
            }
        }
        // 有效期
        if (mContentLevel5View != null) {
            if (TextUtils.isEmpty(deadline)) {
                mContentLevel5View.setVisibility(GONE);
            } else {
                String strDeadline = "下载有效期：<font color=#C4852F> " + deadline + "</font>";
                mContentLevel5View.setText(Html.fromHtml(strDeadline));
                mContentLevel5View.setVisibility(VISIBLE);
            }
        }

    }


    /**
     * @param isVipContent
     * @param title
     * @param neededPrice
     * @param neededTicket
     * @param hasTicketCount
     * @param deadline
     */
    public void setMessage(boolean isVipContent, String title, int neededPrice,
                           int neededTicket, int hasTicketCount, String deadline) {
        // 设置值 标题
        if (mContentLevel2View != null) {
            mContentLevel2View.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                mContentLevel2View.setText(Html.fromHtml(title));
            }
        }
        // 价格
        if (mContentLevel3View != null) {
            String strPrice = "";
            String strTickek = "";
            if (neededPrice > 0 && !isVipContent) {
                strPrice = "价格：<font color=#C4852F> " + neededPrice + "元 </font>";
            }
            if (neededTicket > 0) {
                if (TextUtils.isEmpty(strPrice)) {
                    strTickek = "或使用<font color=#C4852F>" + neededTicket + "张</font>通看券";
                } else {
                    strTickek = " (需<font color=#C4852F>" + neededTicket + "张</font>通看券)";
                }
            }
            String concat = strPrice.concat(strTickek);
            mContentLevel3View.setVisibility(TextUtils.isEmpty(concat) ? GONE : VISIBLE);
            mContentLevel3View.setText(Html.fromHtml(concat));
        }
        // 拥有的券
        if (mContentLevel4View != null) {
            mContentLevel4View.setVisibility(hasTicketCount >= 0 ? VISIBLE : GONE);
            if (hasTicketCount >= 0) {
                String strHasTicket = "您还剩余<font color=#C4852F> " + hasTicketCount + "张</font>通看券";
                mContentLevel4View.setText(Html.fromHtml(strHasTicket));
            }
        }
        // 有效期
        if (mContentLevel5View != null) {
            if (TextUtils.isEmpty(deadline)) {
                mContentLevel5View.setVisibility(GONE);
            } else {
                String strDeadline = "观看有效期：<font color=#C4852F> " + deadline + "</font>";
                mContentLevel5View.setText(Html.fromHtml(strDeadline));
                mContentLevel5View.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * Desc:
     *
     * Author: wangzhilong
     * Date: 2019-06-11
     */
    public void setMessageMoney(String title, double vipPrice, double price, String deadline) {
        // 设置值 标题
        if (mContentLevel2View != null) {
            mContentLevel2View.setVisibility(TextUtils.isEmpty(title) ? GONE : VISIBLE);
            if (!TextUtils.isEmpty(title)) {
                mContentLevel2View.setText(title);
            }
        }
        // 价格
        if (mContentLevel3View != null) {
            //会员6元购买(原价12元)
            String strPrice ="";
            if(vipPrice>=0){
                strPrice= "<font color=#F6D08D>钻石会员"
                    + doubleTrans(vipPrice)
                    + "元购买</font>";
            }
            if(price>=0){
                strPrice=strPrice+ "<font color=#333333> (原价"
                    + doubleTrans(price)
                    + "元)</font>";
            }

            mContentLevel3View.setVisibility(TextUtils.isEmpty(strPrice) ? GONE : VISIBLE);
            mContentLevel3View.setText(Html.fromHtml(strPrice));
        }

        // 有效期
        if (mContentLevel4View != null) {
            if (TextUtils.isEmpty(deadline)) {
                mContentLevel4View.setVisibility(GONE);
            } else {
                String strDeadline = "观看有效期：<font color=#F6D08D>" + deadline + "</font>";
                mContentLevel4View.setText(Html.fromHtml(strDeadline));
                mContentLevel4View.setVisibility(VISIBLE);
            }
        }
    }

    /**
     * 1.5= 1.5 & 1.0=1
     * @return
     */
    public String doubleTrans(double d){
        if(Math.round(d)-d==0){
            return String.valueOf((long)d);
        }
        return String.valueOf(d);
    }
}
