package com.example.andpermissiondemo.util;

import android.content.Context;
import android.widget.Toast;

import com.example.andpermissiondemo.R;


/**
 * Copyright © 2017 回收哥. All rights reserved.
 *
 * @author wujm
 * @Description 土丝工具类, intervalTime为土丝提示间隔时间
 * @CreateDate 2017-02-06 上午00:00:00
 * @ModifiedBy 修改人中文名或拼音缩写
 * @ModifiedDate 修改日期格式YYYY-MM-DD
 * @WhyModified 改原因描述
 */
public class ToastUtils {

    /**
     * 土丝显示间隔
     */
    private static long intervalTime = 3000;

    /**
     * 上一次显示时间
     */
    private static long lastTime;

    /**
     * 土丝提示
     *
     * @param context
     * @param text
     */
    public static void show(Context context, String text) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < intervalTime) {
            return;
        }
        lastTime = nowTime;
        if (context != null) {
            Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 土丝提示
     *
     * @param context
     * @param id
     */
    public static void show(Context context, int id) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < intervalTime) {
            return;
        }
        lastTime = nowTime;
        Toast.makeText(context, context.getResources().getString(id),
                Toast.LENGTH_SHORT).show();
    }

    /**
     * 土丝提示：不支持的操作
     *
     * @param context
     */
    public static void show(Context context) {
        long nowTime = System.currentTimeMillis();
        if (nowTime - lastTime < intervalTime) {
            return;
        }
        lastTime = nowTime;
        Toast.makeText(context, context.getResources().getString(R.string.unsupported_operation),
                Toast.LENGTH_SHORT).show();
    }

}
