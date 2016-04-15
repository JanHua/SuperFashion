package com.sf.fresco;

import android.content.Context;
import android.util.AttributeSet;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.view.SimpleDraweeView;
import com.sf.R;

/**
 * 默认风格图片View
 */
public class FrescoImageView extends SimpleDraweeView {
    public FrescoImageView(Context context, GenericDraweeHierarchy hierarchy) {
        super(context, hierarchy);
        init();
    }

    public FrescoImageView(Context context) {
        super(context);
        init();
    }

    public FrescoImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public FrescoImageView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.setHierarchy(FrescoConfigHelper.getGenericDrawHierarchy(ScalingUtils.ScaleType.CENTER_CROP, R.drawable.umu_logo, this.getResources().getDrawable(R.drawable.umu_logo), 8));
    }

}
