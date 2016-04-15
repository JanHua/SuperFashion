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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.sf.constants.EnumConst.HeadShowState;

/**
 * 上拉加载-下拉刷新
 */
@SuppressLint("ClickableViewAccessibility")
public class XListView extends ListView implements OnScrollListener {

    private Scroller mScroller; // 快速滑动效果
    private final int durationTime = 400; // 滑动时间

    private HeadView headView; // 头部View
    private boolean mEnablePullRefresh = true; // 是否开启头部滑动操作
    private boolean mPullRefreshing = false; // 是否正在刷新

    private OnScrollListener mScrollListener; // 上下滑动事件 子类可做重写操作
    private XListViewListener mListViewListener; // 操作层回调刷新使用

    // 滑动列表时记忆第一次的位置值
    private float lastY = -1;
    // 抵消滑动间距比例值
    private final static float OFFSET_RADIO = 1.8f;

    /**
     * 底部View
     */
    private FootView mFootView;
    private boolean mEnableLoadMore = true; // 是否开启底部滑动操作
    private boolean mIsLoadingMore = false; // 是否正在加载
    private boolean isColosLoadingMore = false;// 是否关闭下拉加载
    private int mCurrentScrollState; // 滑动状态
    private ProgressBar footProgressBar; // 缓冲bar
    private RelativeLayout footMessageLayout; // 当数据为空时提示信息

    private ListAdapter adapter; // 数据适配器做刷新使用

    // 空数据时温馨提示
    private EmptyView emptyView;

    public XListView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public XListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public XListView(Context context, AttributeSet attrs) {
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

        mFootView = new FootView(context);
        footProgressBar = mFootView.getMProgressBar();
        footMessageLayout = mFootView.getMessageLayout();
        addFooterView(mFootView);
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
     * 设置提示信息的上边距
     *
     * @param pt
     */
    private void addMessageLayoutPt(int pt) {
        if (mFootView == null) {
            return;
        }

        mFootView.setPadding(0, pt, 0, 0);
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
                            mListViewListener.onRefresh();
                        }
                    }
                    reseltHeadHeight(); // 重置头部高度
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
     * 底部加载显示状态值
     */
    public void setLoadMoreEnable(boolean enableState) {
        mEnableLoadMore = enableState;
    }

    /**
     * 关闭头部的刷新
     */
    public void stopRefresh() {
        headView.onLoadingFinishState();
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                if (mPullRefreshing == true) {
                    mPullRefreshing = false;
                    reseltHeadHeight();
                }
            }

        }, 1000);

    }

    /**
     * 关闭底部的加载
     */
    public void stopLoad() {
        if (isColosLoadingMore) {
            return;
        }
        mIsLoadingMore = false;
        footProgressBar.setVisibility(View.GONE);
    }

    /**
     * 设置加载列表加载完成的友好提示
     *
     * @param notDataInfo
     */
    public void setNotDataInfo(String notDataInfo) {
        if (mFootView == null) {
            return;
        }

        mFootView.setNotData(notDataInfo);
    }

    /**
     * 关闭下拉加载 （为空时友好提示）
     */
    public void onLoadEndMessage() {
        isColosLoadingMore = true;
        if (footProgressBar != null) {
            footProgressBar.setVisibility(View.GONE);
        }
        if (footMessageLayout != null) {
            footMessageLayout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 关闭下拉加载 （为空时友好提示）
     *
     * @param paddingTop
     */
    public void onLoadEndMessagePD(int paddingTop) {
        addMessageLayoutPt(paddingTop);
        onLoadEndMessage();
    }

    /**
     * 开启下拉加载
     */
    public void openLoad() {
        addMessageLayoutPt(0);
        isColosLoadingMore = false;
        if (footProgressBar != null) {
            footProgressBar.setVisibility(View.VISIBLE);
        }
        if (footMessageLayout != null) {
            footMessageLayout.setVisibility(View.GONE);
        }
    }

    /**
     * 是否正在上拉加载
     *
     * @return true:是
     * @author wjh
     * @update 2015-6-16 下午9:38:25
     */
    public boolean isLoadState() {
        return mIsLoadingMore;
    }

    /**
     * 是否正在下拉刷新
     *
     * @return true:是
     * @author wjh
     * @update 2015-6-16 下午9:38:25
     */
    public boolean isRefreshState() {
        return mPullRefreshing;
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
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    @Override
    public void setAdapter(ListAdapter adapter) {
        super.setAdapter(adapter);
        this.adapter = adapter;
    }

    @Override
    public void computeScroll() { // 滚动条滚动来更新顶部的高度 mPullRefreshing
        if (mScroller.computeScrollOffset()) {
            headView.updateVisiableHeight(mScroller.getCurrY(),
                    mEnablePullRefresh, true);
        }
        postInvalidate();
        super.computeScroll();
    }

    @Override
    public void setOnScrollListener(OnScrollListener l) {
        mScrollListener = l;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

        if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
                && mEnableLoadMore) {
            view.invalidateViews();
        }
        mCurrentScrollState = scrollState;

        if (mScrollListener != null) {
            mScrollListener.onScrollStateChanged(view, scrollState);
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem,
                         int visibleItemCount, int totalItemCount) {
        // TODO Auto-generated method stu
        if (mScrollListener != null) {
            mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount,
                    totalItemCount);
        }

        /**
         * 底部加载处理
         */
        if (mListViewListener != null && mEnableLoadMore) {
            if (visibleItemCount == totalItemCount) { // 显示条数等于或者 总条数时不执行
                footProgressBar.setVisibility(View.GONE);
                footMessageLayout.setVisibility(View.GONE);
                return;
            }
            boolean loadMore = firstVisibleItem + visibleItemCount >= totalItemCount;

            if (!isColosLoadingMore && !mIsLoadingMore && loadMore
                    && mCurrentScrollState != SCROLL_STATE_IDLE) {
                mIsLoadingMore = true;
                footMessageLayout.setVisibility(View.GONE);
                footProgressBar.setVisibility(View.VISIBLE);
                mListViewListener.onLoadMore();
            }
        }

    }

    /**
     * 回调接口
     */
    public void setListViewListener(XListViewListener mListViewListener) {
        this.mListViewListener = mListViewListener;
    }

    public interface XListViewListener {
        void onRefresh();

        void onLoadMore();
    }

    /**
     * 获取底部提示信息布局
     *
     * @return
     */
    public RelativeLayout getFootMessageLayout() {
        return footMessageLayout;
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
