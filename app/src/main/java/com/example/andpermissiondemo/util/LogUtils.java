package com.example.andpermissiondemo.util;

import android.content.Context;
import android.util.Log;

/**
 * Copyright © 2017 回收哥. All rights reserved.
 *
 * @author wujm
 * @Description 日志工具类
 * @CreateDate 2017-02-06 上午00:00:00
 * @ModifiedBy 修改人中文名或拼音缩写
 * @ModifiedDate 修改日期格式YYYY-MM-DD
 * @WhyModified 改原因描述
 */
public class LogUtils {

    private static final String TAG = "LogUtils";
    private static boolean debugFlag = true;

    public static void d(String tag, String msg) {
        if (debugFlag) {
            Log.d(tag, msg);
        }
    }

    public static void d(Context context, String tag, int resId) {
        if (debugFlag) {
            Log.d(tag, context.getResources().getString(resId));
        }
    }

    public static void d(String msg) {
        if (debugFlag) {
            Log.d(TAG, msg);
        }
    }

    public static void e(String msg) {
        if (debugFlag) {
            Log.e(TAG, msg);
        }
    }

    public static void d(Context context, int resId) {
        if (debugFlag) {
            Log.d(TAG, context.getResources().getString(resId));
        }
    }
    public static void i(String msg) {
        if (debugFlag) {
            Log.i(TAG, msg);
        }
    }

}
