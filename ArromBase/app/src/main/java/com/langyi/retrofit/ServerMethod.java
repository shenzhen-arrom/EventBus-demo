package com.langyi.retrofit;

import android.util.Log;

import com.google.gson.Gson;
import com.langyi.retrofit.http.GET;
import com.langyi.retrofit.http.POST;
import com.langyi.retrofit.http.Query;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import okhttp3.OkHttp;
import okhttp3.Request;
import okhttp3.ResponseBody;

/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：解析参数的类
 */

public class ServerMethod {

    final  Retrofit retrofit;
    final Method method;
    String httpMethod;
    String relativeUrl;
    final ParameterHandle<?>[] parameterHandlers;

    public ServerMethod(Builder builder) {
        this.retrofit = builder.retrofit;
        this.method = builder.method;
        this.httpMethod = builder.httpMethod;
        this.relativeUrl = builder.relativeUrl;
        this.parameterHandlers = builder.parameterHandlers;
    }

    public okhttp3.Call createNewCall(Object[] args) {
        //还需要一个对象，专门用来添加参数
        RequestBuilder requestBuilder = new RequestBuilder(retrofit.baseUrl,relativeUrl,httpMethod,parameterHandlers,args);
//        Request.Builder builder = new Request.Builder();
//        //添加参数
//        String url = retrofit.baseUrl+relativeUrl;
//        //参数存在parameterHandlers里面
//        for (ParameterHandle<?> parameterHandler : parameterHandlers) {
//            parameterHandler.apply();
//        }
//        builder.url(url).
        return retrofit.callFactory.newCall(requestBuilder.build());
    }

    public <T> T parseBody(ResponseBody responseBody) {
        //获取解析类型T （获取方法返回值的类型）
        Type type = method.getGenericReturnType();//返回值对象的所有泛型
        Class<T> dataClass= (Class<T>) ((ParameterizedType) type).getActualTypeArguments()[0];
        //解析工厂去转换
        Gson gson = new Gson();
        T body=gson.fromJson(responseBody.charStream(),dataClass);
        return  body;
    }

    public  static class Builder{
        final  Retrofit retrofit;
        final Method method;
        final Annotation[] methodAnnotations;
         String httpMethod;
         String relativeUrl;
        Annotation[][] parameterAnnotations;
        final ParameterHandle<?>[] parameterHandlers;

        public Builder(Retrofit retrofit,Method method){
            this.retrofit =retrofit;
            this.method = method;
            methodAnnotations = method.getAnnotations();
            //二维数组
            parameterAnnotations = method.getParameterAnnotations();
            parameterHandlers = new ParameterHandle[parameterAnnotations.length];
        }

        public ServerMethod build(){
            //解析
            for (Annotation methodAnnotation : methodAnnotations) {
                //解析method，header等
                parseAnnotation(methodAnnotation);
            }
            //解析参数注解
            int count = parameterHandlers.length;
            for (int i = 0; i < count; i++) {
                Annotation parameter = parameterAnnotations[i][0];//Query ..
                Log.d("arrom","parameter = " + parameter.annotationType().getName());
                //可能会涉及到模版和策略的设计模式
                if(parameter instanceof Query){
                  //一个封装成ParameterHandle，不同的参数注解选择不同的策略
                    parameterHandlers[i] = new ParameterHandle.Query<>(((Query)parameter).value());
                }
            }


            return new ServerMethod(this);
        }

        private void parseAnnotation(Annotation methodAnnotation) {
            //value 请求方法
            if(methodAnnotation instanceof GET){
                parseMethodAnnotation("GET",((GET) methodAnnotation).value());
            }else if(methodAnnotation instanceof POST){
                parseMethodAnnotation("POST",((POST) methodAnnotation).value());
            }
        }

        private void parseMethodAnnotation(String method, String value) {
            this.httpMethod = method;
            this.relativeUrl = value;
        }
    }
}
