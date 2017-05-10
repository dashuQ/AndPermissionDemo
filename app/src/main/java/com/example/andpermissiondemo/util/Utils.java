package com.example.andpermissiondemo.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.example.andpermissiondemo.activity.LoginActivity;

/**
 * Created by lenovo on 2017/5/5.
 */

public class Utils {


    /**
     * 是否登录过
     */
    public static void isLoging(Context context) {
        Intent intent = new Intent();
        //测试111
        if (false) {//获取到用户登录信息
//            intent.setClass(context, MainActivity.class);
            intent.setClass(context, LoginActivity.class);
        } else {//没有登录数据，去登录
            intent.setClass(context, LoginActivity.class);
        }
        context.startActivity(intent);//选择登录方式
        Activity a = (Activity) context;
        a.finish();
    }
}
