package com.langyi.retrofit;


import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.OkHttp;
import okhttp3.Response;

/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：
 */

public class OkHttpCall<T> implements Call<T> {
    final ServerMethod serverMethod;
    final Object[] args;
    public OkHttpCall(ServerMethod serverMethod, Object[] args) {
        this.serverMethod = serverMethod;
        this.args =args;
    }

    @Override
    public void enqueue(Callback<T> callback) {
        //发起一个请求，给一个回调
        okhttp3.Call call = serverMethod.createNewCall(args);
        call.enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(@NotNull okhttp3.Call call, @NotNull IOException e) {
                 if(callback!=null){
                     callback.onFailure(OkHttpCall.this,e);
                 }
            }

            @Override
            public void onResponse(@NotNull okhttp3.Call call, @NotNull Response response) throws IOException {
                //解析数据 Response -> Response<T> 回调
                Log.e("Arrom",response.body().string());
                //涉及到解析
                com.langyi.retrofit.Response mResponse = new com.langyi.retrofit.Response();
                mResponse.body = serverMethod.parseBody(response.body());
                if(callback!=null){
                    callback.onResponse(OkHttpCall.this,mResponse);
                }

            }
        });
    }
}
