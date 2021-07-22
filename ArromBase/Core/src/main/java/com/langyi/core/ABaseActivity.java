package com.langyi.core;

import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * 基础的baseActivity
 */
public abstract class ABaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置layout
        setContentView();
        //初始化控件
        initView();
        //初始化数据
        initData();
        //事件监听
        initMonitor();
    }

    protected abstract void initMonitor();

    protected abstract void initData();

    protected abstract void initView();

    protected abstract void setContentView();
}