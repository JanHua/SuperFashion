package com.sf.widget.recycler;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Recycle基类
 */
public abstract class RecyclerBaseView extends FrameLayout {
    public static final int STATE_NULL = -1;
    public static final int STATE_EMPTY = 0;
    public static final int STATE_LOADING = 1;
    public static final int STATE_CONTENT = 2;
    public static final int STATE_MSG = 3;
    public static int STATE = STATE_NULL;

    public Context context;
    protected RecyclerMoreImp recyclerMoreImp;
    protected RecyclerRefreshImp recyclerRefreshImp;
    protected LayoutInflater inflater;

    public RecyclerBaseView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public RecyclerBaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public RecyclerBaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        inflater = LayoutInflater.from(context);
        setState(STATE_CONTENT);
    }

    public abstract void showEmptyView();

    public abstract void hideEmptyView();

    public abstract void showLoadingView();

    public abstract void hideLoadingView();

    public abstract void showContentView();

    public abstract void hideContentView();

    public abstract void showMsgView();

    public abstract void hideMsgView();

    /**
     * 加载更多
     */
    public void onLoadMore() {
        if (recyclerMoreImp != null) {
            setState(STATE_LOADING);
            recyclerMoreImp.onLoadMore();
        }
    }

    /**
     * 下拉刷新
     */
    public void onRefresh() {
        if (recyclerRefreshImp != null) {
            recyclerRefreshImp.onRefresh();
        }
    }

    public void stopMore() {
        setState(STATE_CONTENT);
    }

    public void stopRefresh() {
        setState(STATE_CONTENT);
    }

    /**
     * 设置状态
     *
     * @param state
     */
    public void setState(int state) {
        RecyclerBaseView.STATE = state;
        updateState();
    }

    /**
     * 更新当前显示状态
     */
    public void updateState() {
        if (STATE == STATE_NULL) {
            return;
        }

        switch (STATE) {
            case STATE_LOADING:
                showContentView();
                showLoadingView();
                hideEmptyView();
                hideMsgView();
                break;

            case STATE_CONTENT:
                showContentView();
                hideLoadingView();
                hideEmptyView();
                hideMsgView();
                break;

            case STATE_EMPTY:
                showEmptyView();
                hideLoadingView();
                hideMsgView();
                break;

            case STATE_MSG:
                showMsgView();
                showContentView();
                hideEmptyView();
                hideLoadingView();
                break;

            default:
                break;
        }
    }

    /**
     * 添加子View
     *
     * @param childView
     */
    protected void addChildView(View childView) {
        addView(childView, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
    }

    public interface RecyclerMoreImp {
        public void onLoadMore();
    }

    public interface RecyclerRefreshImp {
        public void onRefresh();
    }

    /**
     * 刷新回调接口
     *
     * @param recyclerMoreImp
     * @param recyclerRefreshImp
     */
    public void setRecyclerBackImp(RecyclerMoreImp recyclerMoreImp, RecyclerRefreshImp recyclerRefreshImp) {
        this.recyclerMoreImp = recyclerMoreImp;
        this.recyclerRefreshImp = recyclerRefreshImp;
    }

}
