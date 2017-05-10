package com.example.andpermissiondemo.activity;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.andpermissiondemo.R;
import com.example.andpermissiondemo.config.Constants;
import com.example.andpermissiondemo.manager.AppManager;
import com.example.andpermissiondemo.util.Utils;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.PermissionListener;

import java.util.ArrayList;
import java.util.List;


/**
 * Copyright © 2017 回收哥. All rights reserved.
 *
 * @author wujm
 * @Description 启动页SplashActivity（闪屏界面-应用入口）
 * @CreateDate 2017-02-06 上午00:00:00
 * @ModifiedBy 修改人中文名或拼音缩写
 * @ModifiedDate 修改日期格式YYYY-MM-DD
 * @WhyModified 改原因描述
 */
public class SplashActivity extends AppCompatActivity implements View.OnClickListener {

    private Handler mHandler = new Handler();

    /**
     * 第一次使用去引导页，否则去首页>>false第一次使用去引导页，true已使用过 去首页
     */
    private boolean firstRun;
    private Editor editor;
    private View view = null;

    private ImageView iv_start;
    private TextView tv_right_count;

    String type = "10";
    String url = "";
    String detail = "";
    private Context TAG;


    /**
     * 必须的权限是否都获取到了，没获取不不让使用APP
     */
    private boolean permissionFlag;
    /**
     * 请求应用必须权限
     */
    private void andPermissionStart() {
        // 在Activity：
        /*
        写入外部存储
        WRITE_EXTERNAL_STORAGE
        从SDCard读取数据权限
                READ_EXTERNAL_STORAGE
                往SDCard写入数据权限  注意MOUNT_UNMOUNT_FILESYSTEMS存储权限在代码检查是否有此权限时，可以不写，但一写要在清单中注册,否则在该应用权限设置中没有这个权限选项就无法去开关授权
                MOUNT_UNMOUNT_FILESYSTEMS
        ACCESS_FINE_LOCATION
                ACCESS_COARSE_LOCATION*/
        AndPermission.with(TAG)
                .requestCode(Constants.OCRFinal.REQUEST_CODE_SETTING)
                .permission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.MOUNT_UNMOUNT_FILESYSTEMS,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                )
                .callback(listener)
                .start();
    }
    /**
     * 请求权限回调
     */
    private PermissionListener listener = new PermissionListener() {
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onSucceed(int requestCode, List<String> grantedPermissions) {
            // 权限申请成功回调。
            // 这里的requestCode就是申请时设置的requestCode。
            // 和onActivityResult()的requestCode一样，用来区分多个不同的请求。
            if (requestCode == Constants.OCRFinal.REQUEST_CODE_SETTING) {
                onFailedCheckSelfPermission();
            }
        }
        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onFailed(int requestCode, List<String> deniedPermissions) {
            // 权限申请失败回调。
            if (requestCode == Constants.OCRFinal.REQUEST_CODE_SETTING) {
                permissionFlag = false;
                // 是否有不再提示并拒绝的权限。
                onFailedCheckSelfPermission();
            }
        }
    };
    private String weizhiStr = "位置信息";
    private String cunchuStr = "存储空间";
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void onFailedCheckSelfPermission() {
        List<String> permissions = new ArrayList<>();
        if (!AndPermission.hasPermission(TAG, Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissions.add(weizhiStr);
        }
        if (!AndPermission.hasPermission(TAG, Manifest.permission.ACCESS_COARSE_LOCATION)) {
            if (permissions.size() == 0) {
                permissions.add(weizhiStr);
            }
        }
        if (!AndPermission.hasPermission(TAG, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            permissions.add(cunchuStr);
        }
        if (!AndPermission.hasPermission(TAG, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            if (permissions.size() != 2 && null != permissions && !cunchuStr.equals(permissions.get(0))) {
                permissions.add(cunchuStr);
            }
        }
        StringBuilder sb = new StringBuilder();
        if (permissions.size() == 2) {
            sb.append(permissions.get(0) + "、" + permissions.get(1));
        } else if (permissions.size() == 1) {
            sb.append(permissions.get(0));
        } else if (permissions.size() == 0) {
            permissionFlag = true;
            animationEndToStart();
            return;
        }
        // 第二种：用自定义的提示语。
        AndPermission.defaultSettingDialog(SplashActivity.this, Constants.OCRFinal.REQUEST_CODE_SETTING)
                .setTitle(R.string.permission_apply_failed)
                .setMessage(String.format(getString(R.string.we_need_d_permission_for_failed), null != sb ? sb.toString() + "" : ""))
                .setPositiveButton(getString(R.string.ok_set_up))
                .setNegativeButton("退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //退出应用
                        AppManager.getAppManager().appExit(SplashActivity.this);
                    }
                })
                .show();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case Constants.OCRFinal.REQUEST_CODE_SETTING: { // 这个400就是你上面传入的数字。
                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
                onFailedCheckSelfPermission();
                break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        TAG = SplashActivity.this;

        //请求应用必须权限
        andPermissionStart();

        view = View.inflate(this, R.layout.ac_splash, null);
        setContentView(view);

        iv_start = (ImageView) findViewById(R.id.iv_start);
        tv_right_count = (TextView) findViewById(R.id.tv_right_count);

        iv_start.setOnClickListener(this);
        tv_right_count.setOnClickListener(this);
        iv_start.setClickable(false);


        //下载下来图片缓存在硬盘，再把图片的URL保存到SP下次直接显示
        //把启动页图片下载下来存在用户缓存文件夹中,而不是存在sp中
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(SplashActivity.this);
        // 默认是首次使用
        boolean SPLASH_BG_NAME_FLAG = sharedPreferences.getBoolean(Constants.OCRFinal.SPLASH_BG_NAME_FLAG, false);
        if (SPLASH_BG_NAME_FLAG) {//上次下载图片成功，去显示上一次的图片，虽然是URL但是缓存在本地的
            img = sharedPreferences.getString(Constants.OCRFinal.SPLASH_BG_NAME, img);

//            Glide.with(this)
//                    .load(img)
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)//这是是禁用了缓存，所以显示的是上次保存在硬盘的网络图片
//                    .placeholder(R.mipmap.start_page)
//                    .into(iv_start);

//            App.getInstance().getImageLoader().displayImage(this, img, iv_start, R.mipmap.start_page, false);
            iv_start.setImageResource(R.mipmap.start_page);

        } else {//没有下载过启动页的缓存，再去加载新的启动页缓存
            getStartPage();
        }

        startAnimition();

    }


    /**
     * 继承 CountDownTimer 防范
     * <p>
     * 重写 父类的方法 onTick() 、 onFinish()
     */
    class MyCountDownTimer extends CountDownTimer {
        /**
         * @param millisInFuture    表示以毫秒为单位 倒计时的总数
         *                          <p>
         *                          例如 millisInFuture=1000 表示1秒
         * @param countDownInterval 表示 间隔 多少微秒 调用一次 onTick 方法
         *                          <p>
         *                          例如: countDownInterval =1000 ; 表示每1000毫秒调用一次onTick()
         */
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        public void onFinish() {
            tv_right_count.setVisibility(View.GONE);
            animationEndToStart();
        }

        public void onTick(long millisUntilFinished) {
//            tv.setText("倒计时(" + millisUntilFinished / 1000 + ")");
            long count = millisUntilFinished / 1000;
            if (count > 3) count = 3;
            else if (count < 1) count = 1;
            tv_right_count.setText("跳过 " + count);  //设置倒计时时间
        }
    }

    private String img;

    private void getStartPage() {

        //解决 图片过大，不显示图片的问题
        new Thread(new Runnable() {
            @Override
            public void run() {

                SharedPreferences sharedPreferences = PreferenceManager
                        .getDefaultSharedPreferences(SplashActivity.this);
                // 默认是首次使用

                editor = sharedPreferences.edit();

//                OkHttpUtils
//                        .post()
//                        .url(Constants.URL.GET_START_PAGE)//http://192.168.80.36:8080/BFC_QT/v1/version/getStartpage
//                        .build()
//                        .execute(new StringCallback() {
//                            @Override
//                            public void onError(Call call, Exception e, int id) {
////                        ToastUtils.show(SplashActivity.this, R.string.network_busy_please_try_again_later);
////                        startAnimition();
//                                editor.putBoolean(Constants.UserCacheFinal.SPLASH_BG_NAME_FLAG, false);
//                                editor.commit();
//                            }
//
//                            @Override
//                            public void onResponse(String response, int id) {
//                                JSONObject jsonObject = null;
//                                try {
//                                    jsonObject = new JSONObject(response);
//                                    int code = jsonObject.optInt(Constants.LoginResponseJsonKey.CODE);
//                                    switch (code) {
//                                        case Constants.InternetConfig.SERVER_RESPONSE_OK://100请求成功
//                                            iv_start.setClickable(true);
//                                            JSONObject obj = jsonObject.optJSONObject("content");
//                                            img = obj.optString("guidePic");
//                                            type = obj.optString("guideType");
//                                            url = obj.optString("guideUrl");
//                                            detail = obj.optString("guideDetail");
//                                            if ("".equals(Utils.isEmptyText(img))) {
//                                                img = "1111";
//                                            }
////                                    Glide.with(SplashActivity.this).load(img).error(R.mipmap.start_page).placeholder(R.mipmap.start_page).into(iv_start);
//
//
////                                    startAnimition();
//
//
//                                            //
//                                            try {
//                                                if (!TextUtils.isEmpty(img)) {
////                                                      Glide.with(SplashActivity.this)
////                                                            .load(img)
////                                                            .asBitmap()
////                                                            .centerCrop()
////                                                            .into(100, 100)
////                                                            .get();
//                                                    FutureTarget<File> future = Glide.with(getApplicationContext())
//                                                            .load(img)
//                                                            .downloadOnly(500, 500);
////                                                    File cacheFile = future.get();
//                                                    //下载下来图片缓存在硬盘，再把图片的URL保存到SP下次直接显示
//                                                    //把启动页图片下载下来存在用户缓存文件夹中,而不是存在sp中
//
//                                                    editor.putString(Constants.UserCacheFinal.SPLASH_BG_NAME, img);
//                                                    editor.putBoolean(Constants.UserCacheFinal.SPLASH_BG_NAME_FLAG, true);
//                                                    editor.commit();
//                                                }
//                                            } catch (Exception e) {
//                                                e.printStackTrace();
//                                                editor.putBoolean(Constants.UserCacheFinal.SPLASH_BG_NAME_FLAG, false);
//                                                editor.commit();
//                                            }
//                                            //
//
//
//                                            break;
//                                        case Constants.InternetConfig.SERVER_RESPONSE_FAILED:
////                                    startAnimition();
//                                            editor.putBoolean(Constants.UserCacheFinal.SPLASH_BG_NAME_FLAG, false);
//                                            editor.commit();
//                                            break;
//                                    }
//                                } catch (JSONException e) {
//                                    editor.putBoolean(Constants.UserCacheFinal.SPLASH_BG_NAME_FLAG, false);
//                                    editor.commit();
//                                    e.printStackTrace();
////                            ToastUtils.show(SplashActivity.this, R.string.railed_parse_server_data);
////                            startAnimition();
//                                }
//                            }
//                        });


            }
        }).start();


    }


    private Animation animation;
    private MyCountDownTimer myCountDownTimer;

    private void startAnimition() {
        animation = AnimationUtils.loadAnimation(this, R.anim.alpha);
        iv_start.startAnimation(animation);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation arg0) {
                myCountDownTimer = new MyCountDownTimer(4000, 1000);
                myCountDownTimer.start();
            }

            @Override
            public void onAnimationRepeat(Animation arg0) {
            }

            @Override
            public void onAnimationEnd(Animation arg0) {
            }
        });
    }

    private void animationEndToStart() {
        //                 第一次使用去引导页，否则去首页
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(SplashActivity.this);
        // 默认是首次使用
        firstRun = sharedPreferences.getBoolean(Constants.OCRFinal.FIRST_RUN, false);
        editor = sharedPreferences.edit();
        // 过几秒去跳转
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (null != myCountDownTimer) {
                    myCountDownTimer.cancel();
                    myCountDownTimer = null;
                }
                if (permissionFlag) {
                    if (firstRun) {// 已使用过，不是第一次打开app，直接跳转到首页
                        isLoging();
                    } else {// ,第一次启动，去引导页,保存true到偏好，跳转到引导页
                        editor.putBoolean(Constants.OCRFinal.FIRST_RUN, true);
                        editor.commit();
                        // 过几秒去引导页
                        toGuide();
                    }
                }
            }
        }, Constants.OCRFinal.SPLASH_ANIM_DELAYMILLIS);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.tv_right_count:
                animationEndToStart();
                break;
//            case R.id.iv_start:
//                if (!"10".equals(type.trim())) {
////                ToastUtils.show(getApplicationContext(), "点击");
//                    intent = new Intent(SplashActivity.this, WebViewActivity.class);
//                    intent.putExtra("type", type);
//                    if ("20".equals(type)) {
//                        intent.putExtra("content", url);
//                    } else if ("30".equals(type)) {
//                        intent.putExtra("content", detail);
//                    }
//                    startActivity(intent);
//                }
//                break;
        }
    }

    /**
     * 第一次启动，去引导页
     */
    private void toGuide() {
        startActivity(new Intent(this, GuidePagesActivity.class));
        finish();
    }

    /**
     * 是否登录过
     */
    private void isLoging() {
        Utils.isLoging(SplashActivity.this);
    }

    @Override
    protected void onDestroy() {
        if (null != mHandler) {
            mHandler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }
}