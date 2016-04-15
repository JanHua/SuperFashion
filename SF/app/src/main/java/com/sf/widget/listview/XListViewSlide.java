package com.sf.widget.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Scroller;

import com.sf.constants.EnumConst.HeadShowState;

/**
 * 上拉加载-下拉刷新
 */
@SuppressLint("ClickableViewAccessibility")
public class XListViewSlide extends ListView implements OnScrollListener {

    private Scroller mScroller; // 快速滑动效果
    private final int durationTime = 400; // 滑动时间

    private HeadView headView; // 头部View
    private boolean mEnablePullRefresh = true; // 是否开启头部滑动操作
    private boolean mPullRefreshing = false; // 是否正在刷新

    private OnScrollListener mScrollListener; // user's scroll listener
    private XListViewListener mListViewListener; // 操作层回调刷新使用

    // 滑动列表时记忆第一次的位置值
    private float lastY = -1;
    // 抵消滑动间距比例值
    private final static float OFFSET_RADIO = 1.8f;

    private FootViewSlide footerView;
    private boolean mEnablePullLoad;
    private boolean mPullLoading;
    private boolean mIsFooterReady = false;
    private int mTotalItemCount;

    private final static int SCROLLBACK_HEADER = 0;
    private final static int SCROLLBACK_FOOTER = 1;
    // when pull up >= 50px at bottom, trigger load more.
    private final static int PULL_LOAD_MORE_DELTA = 50;
    private final static int SCROLL_DURATION = 400;
    private int mScrollBack;

    private ListAdapter adapter; // 数据适配器做刷新使用

    // 空数据时温馨提示
    private EmptyView emptyView;

    public XListViewSlide(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public XListViewSlide(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public XListViewSlide(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    /**
     * 初始化
     *
     * @param context
     */
    private void initView(Context context) {
        mScroller = new Scroller(context, new DecelerateInterpolator());
        super.setOnScrollListener(this);
        headView = new HeadView(context);
        headView.onDefaultState();
        addHeaderView(headView);

        // init footer view
        footerView = new FootViewSlide(context);

    }

    /**
     * 空数据的反馈
     */
    public void setEmptyDefaultView(Context context, String empStr) {
        if (this.getEmptyView() == null && !TextUtils.isEmpty(empStr)) {
            emptyView = new EmptyView(context);
            emptyView.emptyIV.setText(empStr);
            emptyView.emptyIV.setGravity(Gravity.CENTER);
            ((ViewGroup) this.getParent()).addView(emptyView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            this.setEmptyView(emptyView);
        }
    }

    /**
     * 空数据的反馈
     */
    public void setEmptyDefaultView(Context context, String empStr, int empImage) {
        if (this.getEmptyView() == null && !TextUtils.isEmpty(empStr)) {
            emptyView = new EmptyView(context);
            emptyView.emptyIV.setText(empStr);
            emptyView.emptyIV.setGravity(Gravity.CENTER);
            emptyView.emptyIm.setImageResource(empImage);
            ((ViewGroup) this.getParent()).addView(emptyView,
                    new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));
            this.setEmptyView(emptyView);
        }
    }

    /**
     * 更新Empty的数据
     *
     * @param empStr
     * @param empImage
     */
    public void setEmptyData(String empStr, int empImage) {
        if (emptyView == null) {
            return;
        }

        emptyView.emptyIV.setText(empStr);
        emptyView.emptyIV.setGravity(Gravity.CENTER);
        emptyView.emptyIm.setImageResource(empImage);
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(footerView);
        }

        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    /**
     * 启用或禁用拉载更多
     *
     * @param enable
     */
    public void setPullLoadEnable(boolean enable) {
        mEnablePullLoad = enable;
        if (!mEnablePullLoad) {
            footerView.hide();
            footerView.setOnClickListener(null);
            setFooterDividersEnabled(false);
        } else {
            mPullLoading = false;
            footerView.show();
            footerView.setState(FootViewSlide.STATE_NORMAL);
            setFooterDividersEnabled(true);
            footerView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    startLoadMore();
                }
            });
        }
    }

    /*
     * 更新底部View内边距
     */
    private void updateFooterHeight(float delta) {
        int height = footerView.getBottomMargin() + (int) delta;
        if (mEnablePullLoad && !mPullLoading) {
            if (height > PULL_LOAD_MORE_DELTA) {
                footerView.setState(FootViewSlide.STATE_READY);
            } else {
                footerView.setState(FootViewSlide.STATE_NORMAL);
            }
        }
        footerView.setBottomMargin(height);
    }

    /*
     * 重置底部高度
     */
    private void resetFooterHeight() {
        int bottomMargin = footerView.getBottomMargin();
        if (bottomMargin > 0) {
            mScrollBack = SCROLLBACK_FOOTER;
            mScroller.startScroll(0, bottomMargin, 0, -bottomMargin,
                    SCROLL_DURATION);
            invalidate();
        }
    }

    /*
     * 开始加载底部
     */
    private void startLoadMore() {
        mPullLoading = true;
        footerView.setState(FootViewSlide.STATE_LOADING);
        if (mListViewListener != null) {
            mListViewListener.onLoadMore();
        }
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // send to user's listener
        mTotalItemCount = totalItemCount;
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {

        if (mListViewListener == null) {
            return super.onTouchEvent(ev);
        }

        if (lastY == -1) {
            lastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                lastY = ev.getRawY();
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                float slidingDistance = ev.getRawY() - lastY; // 滑动的间距
                lastY = ev.getRawY();
                if (getFirstVisiblePosition() == 0 && slidingDistance > 0
                        || headView.getLayoutHeight() > 0) {
                    headView.updateVisiableHeight(headView.getLayoutHeight()
                                    + slidingDistance / OFFSET_RADIO, mEnablePullRefresh,
                            mPullRefreshing);
                    setSelection(0);
                } else if (getLastVisiblePosition() == mTotalItemCount - 1
                        && (footerView.getBottomMargin() > 0 || slidingDistance < 0)) {
                    // last item, already pulled up or want to pull up.
                    updateFooterHeight(-slidingDistance / OFFSET_RADIO);
                }
                break;
            }
            default: {
                lastY = -1;
                if (getFirstVisiblePosition() == 0) {
                    if (mEnablePullRefresh
                            && headView.getLayoutHeight() > headView
                            .getDefaultHeight()) {
                        mPullRefreshing = true;
                        headView.updataViewShowState(HeadShowState.loadingState);
                        if (mListViewListener != null) {
                            mListViewListener.onReresh();
                        }
                    }
                    reseltHeadHeight(); // 重置头部高度
                } else if (getLastVisiblePosition() == mTotalItemCount - 1) {
                    // invoke load more.
                    if (mEnablePullLoad
                            && footerView.getBottomMargin() > PULL_LOAD_MORE_DELTA
                            && !mPullLoading) {
                        startLoadMore();
                    }
                    resetFooterHeight();
                }
                break;
            }
        }

        return super.onTouchEvent(ev);
    }

    /*
     * 重新设置头部的高度
     */
    private void reseltHeadHeight() {
        int height = headView.getLayoutHeight();
        if (height <= 0) {
            return;
        }
        if (mPullRefreshing && height <= headView.getDefaultHeight()) {
            return;
        }

        int finalHeight = 0;
        if (mPullRefreshing && height > headView.getDefaultHeight()) { // 刷新状态中时将高度直接设置为默认高度
            // 1.先到默认高度缓冲
            // 2.关闭时回复0高度；
            finalHeight = headView.getDefaultHeight();
        }
        mScrollBack = SCROLLBACK_HEADER;
        mScroller.startScroll(0, height, 0, finalHeight - height, durationTime);
        invalidate();
    }

    /**
     * 头部刷新显示状态值
     */
    public void setPullRefreshEnable(boolean enableState) {
        mEnablePullRefresh = enableState;
        if (headView == null) {
            return;
        }

        if (mEnablePullRefresh) {
            headView.setVisibility(View.VISIBLE);
        } else {
            headView.setVisibility(View.GONE);
        }
    }

    /**
     * 关闭头部的刷新
     */
    public void stopRefresh() {
        headView.onLoadingFinishState();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                onRefreshAdapterItem();

                if (mPullRefreshing == true) {
                    mPullRefreshing = false;
                    reseltHeadHeight();
                }
            }

        }, 400);
    }

    /**
     * 关闭底部的加载
     */
    public void stopLoad() {
        onRefreshAdapterItem();
        if (mPullLoading == true) {
            mPullLoading = false;
            footerView.setState(FootViewSlide.STATE_NORMAL);
        }
    }

    /**
     * item 置顶部
     */
    public void setItemTop() {
        this.setSelection(0);
    }

    /**
     * item 尾部
     */
    public void setItemBottoms() {
        this.setSelection(this.getCount());
    }

    /**
     * 设置适配器
     */
    public void setAdapter(BaseAdapter adapter) {
        if (mIsFooterReady == false) {
            mIsFooterReady = true;
            addFooterView(footerView);
        }

        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    /*
     * 刷新列表item
     */
    private void onRefreshAdapterItem() {
        if (this.adapter != null && this.adapter instanceof BaseAdapter) {
            ((BaseAdapter) this.adapter).notifyDataSetChanged();
        }
    }

    @Override
    public void computeScroll() { // 滚动条滚动来更新顶部的高度 mPullRefreshing
        if (mScroller.computeScrollOffset()) {
            if (mScrollBack == SCROLLBACK_HEADER) {
                headView.updateVisiableHeight(mScroller.getCurrY(),
                        mEnablePullRefresh, true);
            } else {
                footerView.setBottomMargin(mScroller.getCurrY());
            }

        }
        postInvalidate();
        super.computeScroll();
    }

    /**
     * 回调接口
     */
    public void setListViewListener(XListViewListener mListViewListener) {
        this.mListViewListener = mListViewListener;
    }

    public interface XListViewListener {
        void onReresh();

        void onLoadMore();
    }

    /**
     * 获取多余Item的个数 只需要头部VIEW量数
     *
     * @return
     */
    public int getExcessCount() {
        return this.getHeaderViewsCount();
    }

}
