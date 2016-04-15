package com.sf.base;

import android.app.Activity;
import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.sf.fresco.FrescoConfigHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Application-基类
 */
public class IApplication extends Application {

    /**
     * 全局Context
     */
    private static IApplication iApplication;
    /**
     * activity集合
     */
    private List<Activity> activityList = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        iApplication = this;
        FrescoConfigHelper.initBuild();
    }

    /**
     * 全局上下文对象
     *
     * @return
     */
    public static IApplication getIApplication() {
        return iApplication;
    }

    /**
     * 当前Activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return activityList.get(activityList.size() - 1);
    }

    /**
     * App是否存活
     *
     * @return
     */
    public boolean isActivation() {
        return activityList == null || activityList.size() == 0 ? false : true;
    }

    /**
     * 增加一个Activity
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 移除一个Activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 判断项目是否已经开启，防止点击桌面icon后重启应用
     */
    public final boolean isAlreadyOpen() {
        return activityList.size() > 1;
    }

    /**
     * 退出应用
     */
    public void exit() {
        for (Activity activity : activityList) {
            activity.finish();
        }
        System.exit(0);
    }
}
