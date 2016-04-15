package com.sf.fresco;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * 自定义图片加载进度条
 */
public class CustomProgressBar extends Drawable {

    @Override
    public void draw(Canvas canvas) {
    }

    @Override
    public void setAlpha(int alpha) {
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
    }

    @Override
    public int getOpacity() {
        return 0;
    }

    @Override
    protected boolean onLevelChange(int level) {
        Log.e("info", "level" + level);
        return super.onLevelChange(level);
    }
}
