package com.example.andpermissiondemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.example.andpermissiondemo.R;
import com.example.andpermissiondemo.manager.AppManager;
import com.example.andpermissiondemo.util.Utils;

/**
 * Copyright © 2017 回收哥. All rights reserved.
 *
 * @author wujm
 * @Description 引导页Activity, 使用viewpager显示4个fragment切换引导页（app引导页,三张APP引导帮助，首次登录APP需展示，非第一次登录可不提示）
 * @CreateDate 2017-02-06 上午00:00:00
 * @ModifiedBy 修改人中文名或拼音缩写
 * @ModifiedDate 修改日期格式YYYY-MM-DD
 * @WhyModified 改原因描述
 */
public class GuidePagesActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        AppManager.getAppManager().addActivity(this);

        setContentView(R.layout.fg_guide_pages_f3);


        Button bn = (Button) findViewById(R.id.bn);
        bn.setOnClickListener(this);
        Button bn_all = (Button) findViewById(R.id.bn_all);
        bn_all.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.bn:
                isLoging();
                break;
            case R.id.bn_all:
                isLoging();
                break;

            default:
                break;
        }
    }
    /**
     * 是否登录过
     */
    private void isLoging() {
        Utils.isLoging(this);
    }
}
