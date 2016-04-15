package com.sf.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.sf.R;


/**
 * 缓冲圈
 *
 * @author wjh
 */
public class IProgressBar extends FrameLayout {

    public IProgressBar(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public IProgressBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public IProgressBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.custom_view_widget, null);
        view.findViewById(R.id.progressbar_layout).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        addView(view);
    }
}
