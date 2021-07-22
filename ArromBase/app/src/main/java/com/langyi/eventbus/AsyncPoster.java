package com.langyi.eventbus;

import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 作者：Arrom
 * 日期： 2021/7/22
 * 描述：
 */

class AsyncPoster implements Runnable{
    Subscription mSubscription;
    Object mEvent;

    private final  static ExecutorService executorService = Executors.newCachedThreadPool();

    public AsyncPoster(Subscription subscription,Object event){
        this.mSubscription = subscription;
        this.mEvent = event;
    }

    public static void  enqueue(Subscription subscription,Object event){
        AsyncPoster asyncPoster = new AsyncPoster(subscription,event);
        // 用线程池
        executorService.execute(asyncPoster);
    }

    @Override
    public void run() {
        try {
            mSubscription.subscriberMethod.method.invoke(mSubscription.subscriber,mEvent);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}
