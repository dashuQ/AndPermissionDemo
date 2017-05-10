package com.example.andpermissiondemo.manager;

import android.app.Activity;
import android.content.Context;

import com.example.andpermissiondemo.util.LogUtils;

import java.util.Iterator;
import java.util.Stack;

/**
 * Copyright © 2017 回收哥. All rights reserved.
 *
 * @author wujm
 * @Description 应用程序Activity管理类：用于Activity管理和应用程序退出
 * @CreateDate 2017-02-06 上午00:00:00
 * @ModifiedBy 修改人中文名或拼音缩写
 * @ModifiedDate 修改日期格式YYYY-MM-DD
 * @WhyModified 改原因描述
 */
public class AppManager {

    private static Stack<Activity> activityStack;
    private static AppManager instance;

    private AppManager() {
    }

    /**
     * 单一实例
     */
    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Iterator<Activity> it = activityStack.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (activity.getClass().equals(cls)) {
                it.remove();
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 除传进来的Activity外，结束所有Activity
     */
    public void finishOtherActivity(Class<?> cls) {
        Iterator<Activity> it = activityStack.iterator();
        while (it.hasNext()) {
            Activity activity = it.next();
            if (null != activity) {
                if (cls.equals(activity.getClass())) {
                    continue;
                }
                it.remove();
                activity.finish();
                activity = null;
            }
        }
    }

    /**
     * 结束除className(SetActivity)以外的所有Activity
     *
     * @param className
     */
    public void finishOtherActivity(String className) {
        int s = activityStack.size();
        for (int i = 0; i < s; i++) {
            Activity t = activityStack.get(i);
            if (null != t) {
                Class clazz = t.getClass();
                if (null != clazz) {
                    String otherClassName = clazz.getName();
                    if (className.equals(otherClassName)) {
                        continue;
                    }
                }
                t.finish();
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void appExit(Context context) {
        try {
            finishAllActivity();//首先要统一管理应用中的所有Activity，退出应用时遍历一个个关闭掉
//            ActivityManager activityMgr = (ActivityManager) context
//                    .getSystemService(Context.ACTIVITY_SERVICE);
//            activityMgr.restartPackage(context.getPackageName());//<uses-permission android:name="android.permission.RESTART_PACKAGES" />
//            System.exit(0);
//            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            LogUtils.d("AppManager appExit Exception");
        }
    }
}