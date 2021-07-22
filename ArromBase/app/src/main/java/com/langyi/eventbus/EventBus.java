package com.langyi.eventbus;

import android.os.Handler;
import android.os.Looper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 作者：Arrom
 * 日期： 2021/7/22
 * 描述：
 */

public class EventBus {

    private final Map<Class<?>, CopyOnWriteArrayList<Subscription>> subscriptionsByEventType;
    private final Map<Object, List<Class<?>>> typesBySubscriber;

    static volatile  EventBus defaultInstance;

    private EventBus(){
        typesBySubscriber = new HashMap<>();
        subscriptionsByEventType = new HashMap<>();
    }

    public static EventBus getDefault(){
        if(defaultInstance==null){
            synchronized (EventBus.class){
                if(defaultInstance==null){
                    defaultInstance =new EventBus();
                }
            }
        }
        return  defaultInstance;
    }

    public void register(Object object){
        //解析所有方法，封装成SubscriberMethod的集合
        List<SubscriberMethod> subscriberMethods = new ArrayList<>();
        Class<?>  objClass=object.getClass();
        //获取所有的方法
        Method[] methods = objClass.getDeclaredMethods();
        for (Method method:methods) {
            //找到有Subscribe注解的方法
            Subscribe subscribe = method.getAnnotation(Subscribe.class);
            if(subscribe!=null){
                  //解析所有的属性
                Class<?>[] parameterTypes = method.getParameterTypes();
//                if(parameterTypes!=null){
                    SubscriberMethod subscriberMethod = new SubscriberMethod(method,parameterTypes[0],
                            subscribe.threadMode(),subscribe.priority(),subscribe.sticky());
                    subscriberMethods.add(subscriberMethod);
//                }
            }
        }
        //按照规则存放到subscriptionsByEventType里面中去
        for (SubscriberMethod subscriberMethod:subscriberMethods ) {
             subscriber(object,subscriberMethod);
        }
    }

    /**
     * 按照规则存放到subscriptionsByEventType里面中去
     * @param object
     * @param subscriberMethod
     */
    private void subscriber(Object object, SubscriberMethod subscriberMethod) {
        Class<?> eventType = subscriberMethod.eventType;
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if(subscriptions==null){
            subscriptions =new  CopyOnWriteArrayList<>();
            subscriptionsByEventType.put(eventType,subscriptions);
        }
        //判断优先级添加（暂时不写）
        subscriptions.add(new Subscription(object,subscriberMethod));
        //typesBySubscriber 是为了方便移除
        List<Class<?>> classes = typesBySubscriber.get(object);
        if(classes==null){
           classes = new ArrayList<>();
           typesBySubscriber.put(object,classes);
        }
        if(!classes.contains(eventType)){
            classes.add(eventType);
        }
    }

    public void unregister(Object object){

        List<Class<?>> eventTypes = typesBySubscriber.get(object);
        if(eventTypes!=null){
            for (Class<?> eventType: eventTypes) {
                 removeObject(eventType,object);
            }
        }
    }

    private void removeObject(Class<?> eventType, Object object) {
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if(subscriptions!=null){
            int size = subscriptions.size();
            for (int i = 0; i < size; i++) {
                Subscription subscription = subscriptions.get(i);
                if(subscription.subscriber==object){
                    subscriptions.remove(i);
                    i--;
                    size--;
                }
            }
        }
    }

    public void post(Object event){
        //遍历subscriptionsByEventType，，需要判断到底在那个线程
        Class<?> eventType = event.getClass();
        //找到符合的方法调用method.invoke()
        CopyOnWriteArrayList<Subscription> subscriptions = subscriptionsByEventType.get(eventType);
        if(subscriptions!=null){
            for (Subscription subscription:subscriptions) {
                 executeMethod(subscription,event);
            }
        }
    }

    private void executeMethod(Subscription subscription, Object event) {
        ThreadMode threadMode = subscription.subscriberMethod.threadMode;
        boolean isMainThread = Looper.getMainLooper()==Looper.myLooper();

        switch (threadMode){
            case POSTING:
                invokeMethod(subscription,event);
                break;
            case MAIN:
                if(isMainThread){
                    invokeMethod(subscription,event);
                }else{
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            invokeMethod(subscription,event);
                        }
                    });
                }
                break;
            case ASYNC:
                AsyncPoster.enqueue(subscription,event);
                break;
            case BACKGROUND:
                 if(!isMainThread){
                     invokeMethod(subscription,event);
                 }else{
                     AsyncPoster.enqueue(subscription,event);
                 }
                break;

        }


    }

    private void invokeMethod(Subscription subscription, Object event) {
        try {
            subscription.subscriberMethod.method.invoke(subscription.subscriber,event);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


}
