package com.sf.widget.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sf.constants.ConstEnum;


import com.sf.R;
import com.sf.constants.ConstEnum.HeadShowState;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 下拉刷新 头部布局
 *
 * @author wjh
 */
@SuppressLint("InflateParams")
public class HeadView extends LinearLayout {

    private LinearLayout linearLayout;
    private TextView stateText, timeText;
    private ProgressBar progressbar;
    private ImageView arrows_img;
    private ImageView state_icon;

    private String lastTime; // 上次加载的时间
    private String hintMassage = "下拉刷新";

    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;

    private int defaultHeight; // 默认高度
    private ConstEnum.HeadShowState defaultHeadState = ConstEnum.HeadShowState.defaultState; // 默认状态

    public HeadView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    @SuppressLint("NewApi")
    public HeadView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public HeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    private void initView(Context context) {
        linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(
                R.layout.custom_view_head, null);
        addView(linearLayout, new LayoutParams(
                LayoutParams.MATCH_PARENT, 0));
        linearLayout.setGravity(Gravity.BOTTOM);

		/* 获取顶部高度值 */
        linearLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {

                        defaultHeight = linearLayout.getChildAt(0).getHeight();
                    }
                });

        state_icon = (ImageView) findViewById(R.id.state_icon);
        stateText = (TextView) findViewById(R.id.state_text);
        timeText = (TextView) findViewById(R.id.time_text);
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        arrows_img = (ImageView) findViewById(R.id.arrows_img);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);

        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    /*
     * 默认状态
     */
    public void onDefaultState() {
        state_icon.setVisibility(View.INVISIBLE);
        setHintMassage("下拉刷新");
        stateText.setText(getHintMassage());
        timeText.setText(getLastTime());
    }

    /*
     * 越界状态
     */
    public void onBeyondState() {
        setHintMassage("松开刷新");
        stateText.setText(getHintMassage());
    }

    /*
     * 加载状态
     */
    public void onLoadingState() {
        setHintMassage("正在刷新");
        stateText.setText(getHintMassage());
    }

    /*
     * 加载完成状态
     */
    public void onLoadingFinishState() {
        setHintMassage("刷新完成");
        state_icon.setVisibility(View.VISIBLE);
        progressbar.setVisibility(View.INVISIBLE);
        // arrows_img.setVisibility(View.INVISIBLE);
        stateText.setText(getHintMassage());
        lastTime = "" + System.currentTimeMillis();
    }

    /*
     * 刷新状态
     */
    public void updataViewShowState(ConstEnum.HeadShowState showState) {
        if (showState == defaultHeadState) { // 只有状态改变时才更新
            return;
        }

        /**
         * 先设置当前状态
         */
        if (showState == ConstEnum.HeadShowState.loadingState) {
            arrows_img.clearAnimation();
            arrows_img.setVisibility(View.INVISIBLE);
            progressbar.setVisibility(View.VISIBLE);
        } else {
            progressbar.setVisibility(View.INVISIBLE);
            arrows_img.setVisibility(View.VISIBLE);
        }

        /**
         * 根据当前状态来设置显示信息
         */
        switch (showState) {
            case defaultState: {
                if (defaultHeadState == ConstEnum.HeadShowState.beyondState) {
                    arrows_img.startAnimation(mRotateDownAnim);
                }
                if (defaultHeadState == ConstEnum.HeadShowState.loadingState) {
                    arrows_img.clearAnimation();
                }

                onDefaultState();
                break;
            }
            case beyondState: {
                if (defaultHeadState != ConstEnum.HeadShowState.beyondState) {
                    arrows_img.clearAnimation();
                    arrows_img.startAnimation(mRotateUpAnim);
                    onBeyondState();
                }
                break;
            }
            case loadingState: {
                onLoadingState();
                break;
            }
        }

        defaultHeadState = showState;
    }

    /*
     * 更新高度
     */
    public void updateVisiableHeight(float height, boolean mEnablePullRefresh,
                                     boolean mPullRefreshing) {
        if (height < 0) {
            height = 0;
        }

        // 刷新头部高度
        LayoutParams layoutParams = (LayoutParams) linearLayout
                .getLayoutParams();
        layoutParams.height = (int) height;
        linearLayout.setLayoutParams(layoutParams);

        // 刷新当前状态
        if (mEnablePullRefresh && !mPullRefreshing) {
            if (height > defaultHeight) {
                updataViewShowState(HeadShowState.beyondState);
            } else {
                updataViewShowState(HeadShowState.defaultState);
            }
        }
    }

    public int getDefaultHeight() {
        return defaultHeight;
    }

    public int getLayoutHeight() {
        return linearLayout.getHeight();
    }

    public String getLastTime() {
        return computeTime();
    }

    public void setLastTime(String lastTime) {
        this.lastTime = lastTime;
    }

    public String getHintMassage() {
        return hintMassage;
    }

    public void setHintMassage(String hintMassage) {
        this.hintMassage = hintMassage;
    }

    /**
     * 时间戳转换
     *
     * @return
     * @author wjh
     * @update 2015-6-8 下午6:45:16
     */
    @SuppressLint("SimpleDateFormat")
    private String computeTime() {
        String second = " 秒前";
        String minute = " 分钟前";
        String hour = " 小时前";
        String timeDesc = "";

        if (lastTime == null)
            return "0" + second;
        long diffTime = (Long.valueOf(System.currentTimeMillis()) - Long
                .valueOf(lastTime)) / 1000;
        if (diffTime < 0)
            return "0";
        if (diffTime < 60)
            timeDesc = diffTime + second;
        else if (diffTime < 3600)
            timeDesc = diffTime / 60 + minute;
        else if (diffTime < 3600 * 24)
            timeDesc = diffTime / 3600 + hour;
        else {
            Date date = new Date(Long.valueOf(lastTime));
            timeDesc = new SimpleDateFormat("yyyy-MM-dd").format(date);
        }
        return timeDesc;
    }
}
