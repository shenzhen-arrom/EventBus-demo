package com.langyi.retrofit;

import android.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Call;
import okhttp3.OkHttpClient;

/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：
 */

public class Retrofit {
    final String baseUrl;
    final Call.Factory callFactory;
    private Map<Method,ServerMethod> serverMethodMap = new ConcurrentHashMap<>();

    public Retrofit(Builder builder) {
       this.baseUrl = builder.baseUrl;
       this.callFactory = builder.callFactory;
    }

    public <T> T create(Class<T> service){

        return (T) Proxy.newProxyInstance(service.getClassLoader(), new Class[]{service}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                Log.d("arrom","methodName:"+method.getName());
                //判断是不是object方法
                if(method.getDeclaringClass() == Object.class){
                    return  method.invoke(this,args);
                }
                //解析参数的注解
                ServerMethod serverMethod = loadServiceMethod(method);
                //封装okhttpcall
                OkHttpCall okHttpCall = new OkHttpCall(serverMethod,args);
                return okHttpCall;
            }
        });
    }

    private ServerMethod loadServiceMethod(Method method) {
       //享元设计模式
        ServerMethod serverMethod = serverMethodMap.get(method);
        if(serverMethod==null){
           serverMethod = new ServerMethod.Builder(this,method).build();
           //缓存数据
           serverMethodMap.put(method,serverMethod);
        }
        return serverMethod;
    }


    public static class Builder{
         String baseUrl;
         Call.Factory callFactory;

        public  Builder baseUrl(String url){
            baseUrl = url;
            return this;
        }
        public Builder client(Call.Factory callFactory){
           this.callFactory = callFactory;
           return this;
        }
        
        public Retrofit build(){
            if(callFactory==null){
                callFactory = new OkHttpClient();
            }
            return new Retrofit(this);
        }

    }

}
