package com.langyi.arrombase;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.langyi.eventbus.EventBus;

/**
 * 作者：Arrom
 * 日期： 2021/7/22
 * 描述：
 */

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test);

        Log.d("xujie","发送消息》》》》》》》》");
        EventBus.getDefault().post("xujie-------");
    }





}
