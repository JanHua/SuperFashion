package com.sf.bean;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

/**
 * App Info
 */
public class ApkInfoBean implements Serializable {

    // 包名
    public String packageName;
    // 应用icon id
    public int iconId;
    // 应用icon 图片
    public Drawable iconDrawable;
    // 应用 名字
    public String programName;
    // 应用版本号
    public int versionCode;
    // 应用版本名
    public String versionName;

}
