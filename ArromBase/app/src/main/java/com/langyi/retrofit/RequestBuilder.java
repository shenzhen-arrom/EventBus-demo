package com.langyi.retrofit;

import java.net.HttpCookie;

import okhttp3.HttpUrl;
import okhttp3.Request;

/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：
 */

public class RequestBuilder {

    ParameterHandle<Object>[] parameterHandlers;
    Object[] args;
    HttpUrl.Builder httpUrl;

    public RequestBuilder(String baseUrl, String relativeUrl, String httpMethod,
                          ParameterHandle<?>[] parameterHandlers, Object[] args) {
        this.parameterHandlers = (ParameterHandle<Object>[]) parameterHandlers;
        this.args =args;
        httpUrl = HttpUrl.parse(baseUrl+relativeUrl).newBuilder();
    }


    public Request build() {
        int count = args.length;
        for (int i = 0; i < count; i++) {
            parameterHandlers[i].apply(this,args[i]);
        }
        Request request = new Request.Builder().url(httpUrl.build())
                .build();
        return request;
    }

    public void addQueryName(String key, String value) {
        //
       httpUrl.addQueryParameter(key,value);
    }
}
