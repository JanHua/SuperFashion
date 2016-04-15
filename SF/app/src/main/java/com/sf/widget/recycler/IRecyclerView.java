package com.sf.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Recycler - 继承主要处理了自动加载更多数据、数据为空状态
 */
public class IRecyclerView extends RecyclerView {

    private RecyclerBaseAdapter adapter;
    private RecyclerBaseView recyclerBaseView;

    public IRecyclerView(Context context) {
        super(context);
        init();
    }

    public IRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public IRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    /**
     * 初始信息
     */
    private void init() {
        addOnScrollListener(new OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                // 滚动到底部自动加载更多 dy>0表示向下滑动 & 剩下1个item自动加载
                final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) getLayoutManager();
                if (dy > 0 && recyclerBaseView != null && linearLayoutManager != null && linearLayoutManager.findLastVisibleItemPosition() >= (linearLayoutManager.getItemCount() - 1)) {
                    recyclerBaseView.onLoadMore();
                }
                super.onScrolled(recyclerView, dx, dy);
            }

        });
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);

        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }
        if (adapter instanceof RecyclerBaseAdapter) {
            this.adapter = (RecyclerBaseAdapter) adapter;
        }
    }

    /**
     * 适配器添加监听状态
     *
     * @return
     */
    private final AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            checkIfEmpty();
        }

    };

    /**
     * 空数据处理
     */
    private void checkIfEmpty() {
        if (adapter != null) {
            if (adapter.getCount() == 0) {
                this.recyclerBaseView.setState(RecyclerBaseView.STATE_EMPTY);
            } else {
                this.recyclerBaseView.setState(RecyclerBaseView.STATE_CONTENT);
            }
        }
    }

    /**
     * 父类布局
     *
     * @param recyclerBaseView
     */
    public void setRecyclerBaseView(RecyclerBaseView recyclerBaseView) {
        this.recyclerBaseView = recyclerBaseView;
    }
}
