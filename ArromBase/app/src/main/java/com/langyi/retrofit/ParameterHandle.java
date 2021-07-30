package com.langyi.retrofit;

/**
 * 作者：Arrom
 * 日期： 2021/7/30
 * 描述：
 */

public interface ParameterHandle<T> {
    public  void   apply(RequestBuilder requestBuilder,T value);
    //策略 Query Part Field 等

    class  Query<T> implements  ParameterHandle<T>{

        private String key; //保存参数的key

        public Query(String key){
            this.key = key;
        }

        @Override
        public void apply(RequestBuilder requestBuilder, T value) {
            //添加到requst中 value 变成string要经过一个工厂
            requestBuilder.addQueryName(key,value.toString());
        }

    }

}
