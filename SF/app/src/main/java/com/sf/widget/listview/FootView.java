package com.sf.widget.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sf.R;

/**
 * 上拉加载 底部布局
 *
 * @author wjh
 */
@SuppressLint("InflateParams")
public class FootView extends LinearLayout {

    private ProgressBar mProgressBar;
    private RelativeLayout messageLayout;
    private TextView tNotData;

    @SuppressLint("NewApi")
    public FootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public FootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public FootView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    @SuppressWarnings("deprecation")
    private void initView(Context context) {
        RelativeLayout layout = (RelativeLayout) LayoutInflater.from(context)
                .inflate(R.layout.custom_view_foot, null);
        mProgressBar = (ProgressBar) layout.findViewById(R.id.foot_progress_bar);
        messageLayout = (RelativeLayout) layout
                .findViewById(R.id.foot_message_layout);
        tNotData = (TextView) layout
                .findViewById(R.id.foot_message_text);
        addView(layout, new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
    }

    public ProgressBar getMProgressBar() {
        return mProgressBar;
    }

    public RelativeLayout getMessageLayout() {
        return messageLayout;
    }

    /**
     * 设置空数据提示文案
     *
     * @param notDataInfo
     */
    public void setNotData(String notDataInfo) {
        if (tNotData == null || TextUtils.isEmpty(notDataInfo)) {
            return;
        }

        tNotData.setText(notDataInfo);
    }

}
