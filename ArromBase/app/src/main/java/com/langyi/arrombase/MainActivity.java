package com.langyi.arrombase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.langyi.eventbus.EventBus;
import com.langyi.eventbus.Subscribe;
import com.langyi.eventbus.ThreadMode;


public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);
    }

    public void sendMsg(View view){
        Intent intent = new Intent(MainActivity.this,TestActivity.class);
        startActivity(intent);

    }

    @Subscribe(threadMode = ThreadMode.MAIN,priority = 100,sticky = true)
    public void recMessage(String msg){
        Log.d("xujie","recMessage>>>>>>>>>"+msg);
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }
}