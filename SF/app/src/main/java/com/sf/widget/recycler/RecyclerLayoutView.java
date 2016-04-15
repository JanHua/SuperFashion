package com.sf.widget.recycler;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.sf.R;
import com.sf.widget.recycler.decoration.DividerItemDecoration;

/**
 * Recycle 实现类
 */
public class RecyclerLayoutView extends RecyclerBaseView {

    protected SwipeRefreshLayout swipeRefreshLayout;
    protected IRecyclerView recyclerView;
    protected View emptyView;

    public RecyclerLayoutView(Context context) {
        super(context);
    }

    public RecyclerLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RecyclerLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void showEmptyView() {
        if (emptyView == null) {
            emptyView = inflater.inflate(R.layout.widget_recycle_empty, null);
            addChildView(emptyView);
        } else {
            emptyView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideEmptyView() {
        if (emptyView != null) {
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void showLoadingView() {

    }

    @Override
    public void hideLoadingView() {

    }

    @Override
    public void showContentView() {
        if (swipeRefreshLayout == null) {
            swipeRefreshLayout = new SwipeRefreshLayout(context);
            swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                    android.R.color.holo_green_light, android.R.color.holo_orange_light,
                    android.R.color.holo_red_light);

            recyclerView = new IRecyclerView(context);
            recyclerView.setHasFixedSize(true);
            recyclerView.setVerticalScrollBarEnabled(true);
            recyclerView.setRecyclerBaseView(this);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL_LIST));

            swipeRefreshLayout.addView(recyclerView, new SwipeRefreshLayout.LayoutParams(SwipeRefreshLayout.LayoutParams.MATCH_PARENT,
                    SwipeRefreshLayout.LayoutParams.MATCH_PARENT));
            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

                @Override
                public void onRefresh() {
                    RecyclerLayoutView.this.onRefresh();
                }
            });
            addChildView(swipeRefreshLayout);
        } else {
            swipeRefreshLayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void hideContentView() {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.setVisibility(View.GONE);
    }

    @Override
    public void showMsgView() {

    }

    @Override
    public void hideMsgView() {

    }

    /**
     * 添加适配器
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        if (recyclerView == null || adapter == null) {
            return;
        }

        recyclerView.setAdapter(adapter);
    }

    @Override
    public void stopRefresh() {
        super.stopRefresh();
        if (swipeRefreshLayout == null) {
            return;
        }

        swipeRefreshLayout.setRefreshing(false);
    }

    /**
     * 下拉刷洗状态 false:禁止刷新
     *
     * @param enabled
     */
    public void setRefreshEnabled(boolean enabled) {
        if (swipeRefreshLayout == null) {
            return;
        }
        swipeRefreshLayout.setEnabled(enabled);
    }
}
