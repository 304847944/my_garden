package com.liuchenxi.foundation.dialogmanager;


import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.facebook.drawee.view.SimpleDraweeView;
import com.liuchenxi.foundation.R;

/**
 * Desc:弹窗类
 *
 * Date: 20200512
 *
 * @author [liumingxin]
 */
public class BaseDialog extends Dialog {

    protected Context mContext;
    protected ConstraintLayout mDialogRootView;
    protected ConstraintLayout mDialogBgView;
    protected TextView mLeftBtnView;
    protected TextView mRightBtnView;
    protected TextView mUpBtnView;
    protected TextView mDownBtnView;
    protected TextView mDownDownBtnView;
    protected TextView mContentLevel2View;
    protected TextView mContentLevel3View;
    protected TextView mContentLevel4View;
    protected TextView mContentLevel5View;

    protected View mLineView;
    protected TextView mTitleView;
    protected TextView mContentView;
    protected ImageView mCloseBtnView;
    protected ImageView mBgImageView;
    protected ImageView mHeadPortraitBgView;
    protected SimpleDraweeView mHeadPortraitView;
    protected SimpleDraweeView mTopImageView;
    protected BaseDialogListen mBaseDialogListen;
    protected ConstraintLayout mLevelParentView;
    protected View mContentBgView;
    protected View mSpaceView;

    protected String mTitle;
    protected String mContent;
    protected String mFirstButton;
    protected String mSecondButton;
    protected String mThirdlyButton;
    protected String mTopImageUrl;
    protected String mContentLevel2;
    protected String mContentLevel3;
    protected String mContentLevel4;
    protected String mContentLevel5;
    protected String mHeadPortrait;

    boolean isVip = false;

    boolean isHavaClose = false;

    //按钮的几中类型
    int LEFT_RIGHT = 0;
    int UP_DOWN = 1;
    int UP_MIDDLE = 2;
    int SINGLE = 3;

    public BaseDialog(@NonNull Context context, String mTitle, String mContent, String mFirstButton,
                      String mSecondButton, BaseDialogListen mBaseDialogListen) {
        this(context, mTitle, mContent, mFirstButton, mSecondButton, null, false,
                false, null, null,null, mBaseDialogListen);
    }

    public BaseDialog(@NonNull Context context, String mTitle, String mContent, String mFirstButton,
                      String mSecondButton) {
        this(context, mTitle, mContent, mFirstButton, mSecondButton, null, false,
                false, null,null, null, null);
    }

    public BaseDialog(@NonNull Context context, String mTitle, String mContent, String mFirstButton,
                      String mSecondButton, boolean isVip, boolean isHaveClose) {
        this(context, mTitle, mContent, mFirstButton, mSecondButton, null, isVip,
                isHaveClose, null,null, null, null);
    }

    public BaseDialog(@NonNull Context context, String mTitle, String mContent, String mFirstButton,
                      String mSecondButton, String mThirdlyButton, boolean isVip, boolean isHaveClose) {
        this(context, mTitle, mContent, mFirstButton, mSecondButton, mThirdlyButton, isVip,
                isHaveClose, null,null, null, null);
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
     * mTopImageUrl ：弹窗顶部大图
     * mHeadPortrait ：是否显示头像（直播战队弹窗）
     * mContentLevel2 ： 二级内容
     * mBaseDialogListen ： 按键监听
     * Author: [liumingxin]
     * Date: 2019-11-13
     */
    public BaseDialog(@NonNull Context context, String mTitle, String mContent, String mFirstButton,
                      String mSecondButton, String mThirdlyButton, boolean isVip, boolean isHaveClose,
                      String mTopImageUrl,String mHeadPortrait, String mContentLevel2, BaseDialogListen mBaseDialogListen) {
        super(context);
        this.mContext = context;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.isVip = isVip;
        this.isHavaClose = isHaveClose;
        this.mFirstButton = setTextContent(mFirstButton);
        this.mSecondButton = setTextContent(mSecondButton);
        this.mThirdlyButton = setTextContent(mThirdlyButton);
        this.mTopImageUrl = mTopImageUrl;
        this.mContentLevel2 = mContentLevel2;
        this.mHeadPortrait = mHeadPortrait;
        this.mBaseDialogListen =  mBaseDialogListen;
        show();
    }

    public BaseDialog(@NonNull Context context, String mTitle, String mContent,String level2,
                      String level3,String level4,String level5, String mFirstButton,
                      String mSecondButton, String mThirdlyButton, boolean isVip, boolean isHaveClose,
                      String mTopImageUrl,String mHeadPortrait, BaseDialogListen mBaseDialogListen) {
        //super(context);
        super(context);
        this.mContext = context;
        this.mTitle = mTitle;
        this.mContent = mContent;
        this.isVip = isVip;
        this.isHavaClose = isHaveClose;
        this.mFirstButton = setTextContent(mFirstButton);
        this.mSecondButton = setTextContent(mSecondButton);
        this.mThirdlyButton = setTextContent(mThirdlyButton);
        this.mTopImageUrl = mTopImageUrl;
        this.mContentLevel2 = mContentLevel2;
        this.mHeadPortrait=mHeadPortrait;
        this.mContentLevel2 = level2;
        this.mContentLevel3 = level3;
        this.mContentLevel4 = level4;
        this.mContentLevel5 = level5;
        this.mBaseDialogListen = mBaseDialogListen;
        show();
    }

    private String setTextContent(String pullContent) {
        if (pullContent != null) {
            return pullContent;
        } else {
            return "";
        }
    }

    protected void setVipBg(TextView mView) {
        mView.setBackground(
                mContext.getResources().getDrawable(R.drawable.base_dialog_btn_right_vip));
        mView.setTextColor(mContext.getResources().getColor(R.color.color_FF9A692D));
        mLeftBtnView.setBackground(mContext.getResources().getDrawable(R.drawable.base_dialog_btn_left_vip));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.base_dialog_general_old);
        initView();
        setCanceledOnTouchOutside(false);
        // getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        getWindow().setLayout((ViewGroup.LayoutParams.MATCH_PARENT), ViewGroup.LayoutParams.MATCH_PARENT);
    }

    private void initView() {
        mDialogRootView = findViewById(R.id.base_dialog_root);//xml根节点
        mDialogBgView = findViewById(R.id.base_dialog_bg);//dialog_bg
        mLeftBtnView = findViewById(R.id.base_dialog_left_btn);//第一个按钮
        mRightBtnView = findViewById(R.id.base_dialog_right_btn);//第二个按钮
        mLineView = findViewById(R.id.base_dialog_line);//分割线
        mTitleView = findViewById(R.id.base_dialog_title);//标题
        mContentView = findViewById(R.id.base_dialog_content);//内容
        mContentBgView = findViewById(R.id.base_dialog_content_bg);//Content渐变透明bg
        mCloseBtnView = findViewById(R.id.base_dialog_close_btn);//关闭按钮
        mUpBtnView = findViewById(R.id.base_dialog_up_btn);//第一个按钮
        mDownBtnView = findViewById(R.id.base_dialog_down_btn);//第二个按钮
        mDownDownBtnView = findViewById(R.id.base_dialog_down_down_btn);//第三个按钮
        mTopImageView = findViewById(R.id.base_dialog_top_image);//顶部图片
        mContentLevel2View = findViewById(R.id.base_dialog_content_level2);//二级内容
        mContentLevel3View = findViewById(R.id.base_dialog_content_level3);//二级内容
        mContentLevel4View = findViewById(R.id.base_dialog_content_level4);//二级内容
        mContentLevel5View = findViewById(R.id.base_dialog_content_level5);//二级内容
        mBgImageView = findViewById(R.id.base_dialog_bg_image);//背景图叠加图
        mHeadPortraitBgView =findViewById(R.id.base_dialog_headimage_bg);//带头像的背景图之头像背景框
        mHeadPortraitView=findViewById(R.id.base_dialog_headimage);//带头像的背景图之头像
        mSpaceView = findViewById(R.id.base_dialog_space_view);//间距view，用于Constraint的负数margin显示
        mLevelParentView = findViewById(R.id.level_parent);//二级内容的parentView
        mHeadPortraitBgView.setVisibility(View.GONE);
        mBgImageView.setVisibility(View.GONE);
        if(mBaseDialogListen==null) {
            mBaseDialogListen = new BaseDialogListen() {
                @Override public void firstOrLeft() {

                }

                @Override public void secondOrRight() {

                }

                @Override public void third() {

                }
            };
        }
    }

    @Override protected void onStart() {
        super.onStart();
        initData();
    }

    @Override public void show() {
        super.show();
    }

    /**
     * Desc:初始化数据（核心，继承时需要修正哪部分可以直接重写对应部分，一般情况建议通过getview获取对应的view来修正）
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     */
    public void initData() {

        setTitleView();
        setContentView();
        setButton();
        setCloseBtnView();
        setTopImageView();
        setContentLevel2View();

    }

    protected void setContentLevel2View() {

    }

    protected void setTopImageView() {

    }

    /**
     * Desc:设置内容
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     */
    protected void setContentView() {
        if (!TextUtils.isEmpty(mContent)) {
            mContentView.setVisibility(View.VISIBLE);
            mContentView.setText(mContent);
        }else
        {
            mContentView.setVisibility(View.GONE);
        }
    }

    /**
     * Desc:设置关闭按钮
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     */
    protected void setCloseBtnView() {
        if (isHavaClose) {
            mCloseBtnView.setVisibility(View.VISIBLE);
            mCloseBtnView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    dismiss();
                }
            });
        } else {
            mCloseBtnView.setVisibility(View.GONE);
        }
    }

    public ImageView getmCloseBtnView(){
        return mCloseBtnView;
    }

    /**
     * Desc:设置标题
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     */
    protected void setTitleView() {
        if (setTitle()) {
            mTitleView.setText(mTitle);
        } else {
            //无标题，单行/多行区分
            mTitleView.setVisibility(View.GONE);
            setContent();
        }
    }

    /**
     * 按钮判定
     */
    protected void setButton() {
        if (isVip) {
            setVipBg(mUpBtnView);
            setVipBg(mRightBtnView);
        }
        if (!TextUtils.isEmpty(mThirdlyButton)) {
            setButtonStyle(UP_MIDDLE, mFirstButton, mSecondButton, mThirdlyButton);
        } else if (TextUtils.isEmpty(mSecondButton)) {
            if(mFirstButton.length()>8){//新增，兼容“钻石会员半价（原价12元）”显示样式
                ConstraintLayout.LayoutParams mDialogUpText =
                        (ConstraintLayout.LayoutParams) this.mUpBtnView.getLayoutParams();
                mDialogUpText.width= dipToPx(mContext, 213);
                this.mUpBtnView.setLayoutParams(mDialogUpText);
            }
            setButtonStyle(SINGLE, mFirstButton, mSecondButton, mThirdlyButton);
        } else {
            if (mFirstButton.length() <= 5 && mSecondButton.length() <= 5) {
                setButtonStyle(LEFT_RIGHT, mFirstButton, mSecondButton, mThirdlyButton);
            } else if ((mFirstButton.length() > 5) ||
                    (mSecondButton.length() > 5)) {//这里暂时注掉了>9的弹窗
                //使用单行按钮样式
                setButtonStyle(UP_DOWN, mFirstButton, mSecondButton, mThirdlyButton);
            } else {
                setButtonStyle(1, mFirstButton, mSecondButton, mThirdlyButton);
                Log.i("@@@:::", "最大长度>9，出错，需要使用定制弹窗");
            }
        }
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
        getThirdButton().setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                mBaseDialogListen.third();
            }
        });
    }

    /**
     * 标题
     */
    private boolean setTitle() {
        if (TextUtils.isEmpty(mTitle)) {
            return false;
        }
        return true;
    }

    /**
     * 无标题内容格式判定
     */
    protected void setContent() {
        //设置文本格式
        if (TextUtils.isEmpty(mContent)) {
            mContentView.setVisibility(View.GONE);
            ConstraintLayout.LayoutParams mTitleParam =
                    (ConstraintLayout.LayoutParams) mTitleView.getLayoutParams();
            mTitleParam.topMargin = dipToPx(mContext, 26);
            mTitleView.setLayoutParams(mTitleParam);
            return;
        }
        if (mContent.length() <= 16) {
            TextPaint mTextPaint = mContentView.getPaint();
            mTextPaint.setTextSize(mContentView.getTextSize());
            int mTextViewWidth = (int) mTextPaint.measureText(mContent);
            if (mTextViewWidth < mContentView.getWidth()) {//未超出一行
                //单行样式
                mContentView.setGravity(Gravity.CENTER_HORIZONTAL);
                mContentView.setTextColor(mContext.getResources().getColor(R.color.color_ff333333));
                mTextPaint.setFakeBoldText(true);
                mContentView.setSingleLine(true);
                mContentView.setEllipsize(TextUtils.TruncateAt.END);
            }
        }
    }

    protected int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * Desc:处理不同button样式时候不同的展示style
     *
     * Author: [liumingxin]
     * Date: 2019-11-14
     */
    protected void setButtonStyle(int mButtonType, String mFirst, String mSecond, String mThird) {
        switch (mButtonType) {
            case 0:
                drawBtnCase2LeftAndRight();
                this.mLeftBtnView.setText(mFirst);
                this.mRightBtnView.setText(mSecond);
                break;
            case 1:
                drawBtnCase2UpAndDown();
                this.mUpBtnView.setText(mFirst);
                this.mDownBtnView.setText(mSecond);
                break;
            case 2:
                drawBtnCase3();
                this.mUpBtnView.setText(mFirst);
                this.mDownDownBtnView.setText(mThird);
                this.mDownBtnView.setText(mSecond);
                break;
            case 3:
                drawBtnCase1();
                this.mUpBtnView.setText(mFirst);
                break;
            default:
        }
    }
    /**
     * Desc:btn绘制（2个按钮情况-左右）
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return
     */
    protected void drawBtnCase2LeftAndRight()
    {
        this.mUpBtnView.setVisibility(View.GONE);
        this.mDownDownBtnView.setVisibility(View.GONE);
        this.mDownBtnView.setVisibility(View.GONE);
        this.mLeftBtnView.setVisibility(View.VISIBLE);
        this.mRightBtnView.setVisibility(View.VISIBLE);

    }

    /**
     * Desc:btn绘制（1个按钮情况）
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return
     */
    protected void drawBtnCase1()
    {
        this.mUpBtnView.setVisibility(View.VISIBLE);
        this.mDownDownBtnView.setVisibility(View.GONE);
        this.mDownBtnView.setVisibility(View.GONE);
        this.mLeftBtnView.setVisibility(View.GONE);
        this.mRightBtnView.setVisibility(View.GONE);
    }

    /**
     * Desc:btn绘制（2个按钮情况-上下）
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return
     */
    protected void drawBtnCase2UpAndDown()
    {
        this.mUpBtnView.setVisibility(View.VISIBLE);
        this.mDownDownBtnView.setVisibility(View.GONE);
        this.mDownBtnView.setVisibility(View.VISIBLE);
        this.mLeftBtnView.setVisibility(View.GONE);
        this.mRightBtnView.setVisibility(View.GONE);
        if(isVip)
        {
            this.mDownBtnView.setBackground(mContext.getResources().getDrawable(R.drawable.base_dialog_btn_left_vip));
        }
    }

    /**
     * Desc:btn绘制（3个按钮情况）
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return
     */
    protected void drawBtnCase3()
    {
        this.mUpBtnView.setVisibility(View.VISIBLE);
        this.mDownDownBtnView.setVisibility(View.VISIBLE);
        this.mDownBtnView.setVisibility(View.VISIBLE);
        this.mLeftBtnView.setVisibility(View.GONE);
        this.mRightBtnView.setVisibility(View.GONE);
        if (isVip) {
            this.mDownBtnView.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.base_dialog_btn_vip_outline));
            this.mDownDownBtnView.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.base_dialog_btn_vip_outline));
            this.mDownDownBtnView.setTextColor(
                    mContext.getResources().getColor(R.color.color_FFC4852F));
            this.mDownBtnView.setTextColor(
                    mContext.getResources().getColor(R.color.color_FFC4852F));
        } else {
            this.mDownBtnView.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.base_dialog_btn_outline));
            this.mDownDownBtnView.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.base_dialog_btn_outline));
            this.mDownDownBtnView.setTextColor(
                    mContext.getResources().getColor(R.color.color_FF0AACFF));
            this.mDownBtnView.setTextColor(
                    mContext.getResources().getColor(R.color.color_FF0AACFF));
        }
    }

    /**
     * Desc:内容
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getContentTextView() {
        return mContentView;
    }

    /**
     * Desc:内容
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public void setContentTextView(Spanned mHtml) {
        mContentView.setText(mHtml);
        mContentView.setVisibility(View.VISIBLE);
    }

    /**
     * Desc:标题
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getTitleTextView() {
        return mTitleView;
    }

    /**
     * Desc:顶部图片
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return mg simple drawee view
     */
    public SimpleDraweeView getTopImageView() {
        return mTopImageView;
    }

    /**
     * Desc:获取第一个按钮
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getFirstButton() {
        if (mLeftBtnView.getVisibility() == View.VISIBLE) {
            return mLeftBtnView;
        } else {
            return mUpBtnView;
        }
    }

    /**
     * Desc:获取第二个按钮
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getSecondButton() {
        if (mRightBtnView.getVisibility() == View.VISIBLE) {
            return mRightBtnView;
        } else {
            return mDownBtnView;
        }
    }

    public TextView getLeftButton() {
        if (mLeftBtnView.getVisibility() == View.VISIBLE) {
            return mLeftBtnView;
        } else {
            return mUpBtnView;
        }
    }

    public TextView getRightButton() {
        if (mRightBtnView.getVisibility() == View.VISIBLE) {
            return mRightBtnView;
        } else {
            return mDownBtnView;
        }
    }

    /**
     * Desc:如果有三个按钮，获取最下面的按钮
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getThirdButton() {
        return mDownDownBtnView;
    }

    /**
     * Desc:内容
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public ConstraintLayout getContentLevelParentView() {
        return mLevelParentView;
    }

    /**
     * Desc:contentlevel2(就是二级内容的第一个textview，例如文字：该节目需要通看券0张...)
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getContentLevel2() {
        return mContentLevel2View;
    }

    /**
     * Desc:contentlevel3(就是二级内容的第2个textview，例如文字：该节目需要通看券0张...)
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getContentLevel3() {
        return mContentLevel3View;
    }

    /**
     * Desc:contentlevel4(就是二级内容的第3个textview，例如文字：该节目需要通看券0张...)
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getContentLevel4() {
        return mContentLevel4View;
    }

    /**
     * Desc:contentlevel5(就是二级内容的第4个textview，例如文字：该节目需要通看券0张...)
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     *
     * @return text view
     */
    public TextView getContentLevel5() {
        return mContentLevel5View;
    }

    /**
     * Desc:设置btn事件
     *
     * Author: [liumingxin]
     * Date: 2019-11-07
     */
    public void setOnClickListen(final BaseDialogListen mbaseDialogListen) {
        this.mBaseDialogListen = mbaseDialogListen;
        if (getFirstButton() != null) {
            getFirstButton().setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    mbaseDialogListen.firstOrLeft();
                }
            });
        }
        if (getSecondButton() != null) {
            getSecondButton().setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    mbaseDialogListen.secondOrRight();
                }
            });
        }
        if (getThirdButton() != null) {
            getThirdButton().setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View view) {
                    mbaseDialogListen.third();
                }
            });
        }
    }

    public void refrashUI() {
        //show();
    }

    /**
     * Desc:
     * firstOrLeft ：左或者上按下
     * secondOrRight ： （有两个按钮时表示右面的或者下面的，有三个按钮时表示中间的）
     * third ： 如果有三个按钮则写，否则不用写内容
     *
     * Date: 2019-11-07
     * Company: @咪咕视讯
     *
     * @author [liumingxin]
     */
    public interface BaseDialogListen {

        void firstOrLeft();

        void secondOrRight();

        void third();
    }

    public interface BaseDialogListenTwo {

        void firstOrLeft();

        void secondOrRight();
    }

    public interface BaseDialogListenOne {

        void firstOrLeft();
    }

    //屏幕改变监听回调
    public interface OrientationChange{
        void orientationChange();
    }

    private OrientationChange orientationChange;

    public OrientationChange getOrientationChange() {
        return orientationChange;
    }

    public void setOrientationChange(OrientationChange orientationChange) {
        this.orientationChange = orientationChange;
    }
}