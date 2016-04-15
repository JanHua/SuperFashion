package com.sf.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;

import com.sf.base.IApplication;
import com.sf.bean.ApkInfoBean;

import java.util.List;

/**
 * App信息帮助类
 */
public class AppInfo {
    private static final Context context = IApplication.getIApplication();

    /**
     * 标示app是否在前台
     */
    public static boolean isActive;

    /**
     * app名字
     */
    private static String appName;

    /**
     * 判断应用是否在前台
     */
    public static boolean isForeground(Context context) {
        List<ActivityManager.RunningTaskInfo> tasks
                = ((ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE)).getRunningTasks(1);
        return !tasks.isEmpty() && tasks.get(0).topActivity.getPackageName().equals(context.getPackageName());
    }

    /**
     * 获取软件版本号
     *
     * @returna
     */
    public static int getVersionCode() {
        int versionCode = 1;
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionCode
            String packageName = context.getPackageName();
            versionCode = context.getPackageManager().getPackageInfo(packageName, 0).versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionCode;
    }

    /**
     * 获取软件版本名
     *
     * @return
     */
    public static String getVersionName() {
        String versionName = "1.0";
        try {
            // 获取软件版本号，对应AndroidManifest.xml下android:versionName
            String packageName = context.getPackageName();
            versionName = context.getPackageManager().getPackageInfo(packageName, 0).versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }

    /**
     * 获取渠道
     *
     * @return
     */
    public static String getChannel() {
        String channel = getMetaValue("UMENG_CHANNEL");
        if (channel == null) {
            channel = "";
        }
        return channel;
    }

    /**
     * 获取metaKey对应的META_DATA的属性值
     *
     * @param metaKey
     * @return
     */
    public static String getMetaValue(String metaKey) {
        String apiKey = null;
        if (metaKey == null) {
            return null;
        }
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle metaData = null;
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                apiKey = metaData.get(metaKey).toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return apiKey;
    }

    /**
     * 获取当前应用剩余可用内存
     *
     * @return 单位byte
     */
    public static long getFreeMemory() {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        // 创建ActivityManager.MemoryInfo对象
        ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(mi);
        return mi.availMem;
    }

    /**
     * 获取有关本程序的信息。
     *
     * @return 有关本程序的信息。
     */
    public static ApkInfoBean getApkInfo(Context mContext) {
        ApkInfoBean apkInfo = new ApkInfoBean();
        ApplicationInfo applicationInfo = mContext.getApplicationInfo();
        apkInfo.packageName = applicationInfo.packageName;
        apkInfo.iconId = applicationInfo.icon;
        apkInfo.iconDrawable = mContext.getResources().getDrawable(apkInfo.iconId);
        apkInfo.programName = mContext.getResources().getText(applicationInfo.labelRes)
                .toString();
        PackageInfo packageInfo = null;
        try {
            packageInfo = mContext.getPackageManager().getPackageInfo(
                    apkInfo.packageName, 0);
            apkInfo.versionCode = packageInfo.versionCode;
            apkInfo.versionName = packageInfo.versionName;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return apkInfo;
    }

    /**
     * 获取App名字
     *
     * @param mContext
     * @return
     */
    public static String getAppName(Context mContext) {
        if (TextUtils.isEmpty(appName)) {
            appName = getApkInfo(mContext).programName;
        }
        return appName == null ? "AppName" : appName;
    }

}
