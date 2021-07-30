package com.langyi.retrofit;



/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：
 */

public interface Callback<T> {

    void onResponse(Call<T> call, Response<T> response);

    void onFailure(Call<T> call,Throwable t);

}
