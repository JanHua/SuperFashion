package com.sf.widget.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sf.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Recycler - 适配器
 */
public abstract class RecyclerBaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    protected static final int NORMAL = 0;
    protected static final int FOOTER = -2;

    static final int FOOTER_HIDE = 3;
    static final int FOOTER_SHOW = 4;
    private int footerState = FOOTER_SHOW;
    protected View footerView;
    private ProgressBar progressBar;
    private TextView textView;

    protected Context context;
    protected List<T> data = new ArrayList<T>();
    protected int spaceFooterHeight;
    protected LayoutInflater inflater;

    public RecyclerBaseAdapter(Context context, List<T> data) {
        this(context, data, 0);
    }

    public RecyclerBaseAdapter(Context context, List<T> data, int spaceFooterHeight) {
        this.context = context;
        this.data = data;
        this.spaceFooterHeight = spaceFooterHeight;
        this.inflater = LayoutInflater.from(context);
        initFooterView();
    }

    /**
     * 取代onBindViewHolder方法
     *
     * @param holder
     * @param position
     */
    public abstract void onBindHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * 取代onCreateViewHolder方法
     *
     * @param parent
     * @param viewType
     */
    public abstract RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType);

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case FOOTER:
                return new FooterViewHolder(footerView);

            default:
                return onCreateHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case FOOTER:
                break;

            default:
                onBindHolder(holder, position);
                break;
        }
    }

    public int getCount() {
        if (data == null) {
            return 0;
        }
        return data.size();
    }

    @Override
    public int getItemCount() {
        int headerOrFooter = 0;
        if (footerView != null && footerState != FOOTER_HIDE)
            headerOrFooter++;
        final int size = getCount();
        return size > 0 ? size + headerOrFooter : 0;
    }

    /**
     * 子类用getViewType方法代替
     */
    @Override
    public int getItemViewType(int position) {
        int headerCount = 0;
        if (position == getItemCount() - 1 && footerView != null && footerState != FOOTER_HIDE) {
            return FOOTER;
        }
        return getViewType(position - headerCount);
    }

    /**
     * 取代getItemViewType方法
     *
     * @param position
     * @return
     */
    public int getViewType(int position) {
        return NORMAL;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        ProgressBar footer_progressbar;
        TextView footer_textView;

        public FooterViewHolder(View itemView) {
            super(itemView);
            footer_progressbar = (ProgressBar) footerView.findViewById(R.id.footer_progressbar);
            footer_textView = (TextView) footerView.findViewById(R.id.recycler_footer_textView);
        }
    }

    /**
     * 显示底部缓冲框
     */
    public void hideFooter() {
        footerState = FOOTER_HIDE;
        notifyDataSetChanged();
    }

    /**
     * 隐藏底部缓冲框
     */
    public void showFooter() {
        footerState = FOOTER_SHOW;
        notifyDataSetChanged();
    }

    /**
     * 获取默认的底部View
     *
     * @return
     */
    private void initFooterView() {
        if (footerView == null) {
            footerView = inflater.inflate(R.layout.widget_footer, null);
            footerView.setMinimumHeight(spaceFooterHeight);
        }

        progressBar = (ProgressBar) footerView.findViewById(R.id.footer_progressbar);
        textView = (TextView) footerView.findViewById(R.id.recycler_footer_textView);
    }

    /**
     * 显示缓冲圈
     */
    public void showProgressBar() {
        if (progressBar == null) {
            return;
        }
        hideTextView();
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏缓冲圈
     */
    public void hideProgressBar() {
        if (progressBar == null) {
            return;
        }
        progressBar.setVisibility(View.GONE);
    }

    /**
     * 设置底部文本信息
     *
     * @param msg
     */
    public void setTextView(String msg) {
        if (TextUtils.isEmpty(msg) || textView == null) {
            return;
        }

        hideProgressBar();
        textView.setVisibility(View.VISIBLE);
        textView.setText(msg);
    }

    /**
     * 隐藏底部文本框
     */
    public void hideTextView() {
        if (textView == null) {
            return;
        }

        textView.setVisibility(View.GONE);
    }

    /**
     * 刷新数据
     *
     * @param data
     */
    public void setData(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }

}
