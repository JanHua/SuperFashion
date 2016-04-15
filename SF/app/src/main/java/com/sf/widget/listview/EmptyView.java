package com.sf.widget.listview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sf.R;

/**
 * 顶部布局
 *
 * @author WJH
 */
public class EmptyView extends FrameLayout {

    private View view;
    public ImageView emptyIm;
    public TextView emptyIV;

    public EmptyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // TODO Auto-generated constructor stub
        initView(context);
    }

    @SuppressLint("InflateParams")
    private void initView(Context context) {
        view = LayoutInflater.from(context).inflate(
                R.layout.empty_view_listview, null);
        emptyIm = (ImageView) view.findViewById(R.id.empty_image);
        emptyIV = (TextView) view.findViewById(R.id.empty_name);
        addView(view, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT));
    }

}
