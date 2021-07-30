package com.langyi.retrofit;

/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：
 */

public interface Call<T> {

    void enqueue(Callback<T> callback);

}
