package com.langyi.arrombase;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;


import androidx.appcompat.app.AppCompatActivity;

import com.langyi.eventbus.EventBus;
import com.langyi.eventbus.Subscribe;
import com.langyi.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Notification;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.ObservableSource;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Action;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.functions.Function;


public class MainActivity extends AppCompatActivity {


    Integer i= 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        EventBus.getDefault().register(this);

//       Observable.create(new ObservableOnSubscribe<Integer>() {
//           // 1. 被观察者发送事件 = 参数为整型 = 1、2、3
//           @Override
//           public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
//               emitter.onNext(1);
//               emitter.onNext(2);
//               emitter.onNext(3);
//           }
//       }).map(new Function<Integer, String>() {
//           @Override
//           public String apply(Integer integer) throws Throwable {
//               return "使用 Map变换操作符 将事件" + integer +"的参数从 整型"+integer + " 变换成 字符串类型" + integer ;
//           }
//       }).subscribe(new Consumer<String>() {
//           //观察者接收事件时，是接收到变换后的事件 = 字符串类型
//           @Override
//           public void accept(String s) throws Throwable {
//               Log.d("arrom",s);
//           }
//       });
//
//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//            //采用flatmap（） 变换操作符
//        }).flatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Throwable {
//                final List<String> list = new ArrayList<>();
//                for(int i=0;i<3;i++){
//                    list.add("我是事件 "+ integer + "拆分后的子事件"+i);
//                }
//                return Observable.fromIterable(list);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String o) throws Throwable {
//                Log.d("arrom",o);
//            }
//        });

//        Observable.create(new ObservableOnSubscribe<Integer>() {
//            @Override
//            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
//                emitter.onNext(1);
//                emitter.onNext(2);
//                emitter.onNext(3);
//            }
//        }).concatMap(new Function<Integer, ObservableSource<String>>() {
//            @Override
//            public ObservableSource<String> apply(Integer integer) throws Throwable {
//                final List<String> list = new ArrayList<>();
//                for(int i = 0; i< 3;i++){
//                    list.add("我是事件" + integer + "拆分后的子事件"+i);
//
//                }
//                return Observable.fromIterable(list);
//            }
//        }).subscribe(new Consumer<String>() {
//            @Override
//            public void accept(String s) throws Throwable {
//                Log.d("arrom",s);
//            }
//        });

        //设置缓存区大小 & 步长
        //缓存区大小 = 每次从被观察者中获取的事件数量
        //步长 = 每次获取新事件的数量
//        Observable.just(1,2,3,4,5)
//                .buffer(3,1)
//                .subscribe(new Observer<List<Integer>>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//                    @Override
//                    public void onNext(@NonNull List<Integer> stringList) {
//                        Log.d("arrom", " 缓存区里的事件数量 = " +  stringList.size());
//                        for (Integer value : stringList) {
//                            Log.d("arrom", " 事件 = " + value);
//                        }
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        Observable.concat(Observable.just(1,2,3),
//                Observable.just(4,5,6),
//                Observable.just(7,8,9),
//                Observable.just(10,11,12))
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Integer integer) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        Observable.merge(Observable.intervalRange(0,3,1,1,TimeUnit.SECONDS),
//                Observable.intervalRange(2,3,1,1,TimeUnit.SECONDS))
//                .subscribe(new Observer<Long>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Long aLong) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
//        Observable.just(1,2,3,4)
//                .count()
//                .subscribe(new Consumer<Long>() {
//                    @Override
//                    public void accept(Long aLong) throws Throwable {
//                        Log.d("arrom","发送的事件数量="+ aLong);
//                    }
//                });
//        //延迟3s再发送，由于使用类似，所以此处不作全部展示
//        Observable.just(1,2,3)
//                .delay(3,TimeUnit.SECONDS)
//                .subscribe(new Observer<Integer>() {
//                    @Override
//                    public void onSubscribe(@NonNull Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(@NonNull Integer integer) {
//
//                    }
//
//                    @Override
//                    public void onError(@NonNull Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });
        Observable.create(new ObservableOnSubscribe<Integer>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Integer> emitter) throws Throwable {
                emitter.onNext(1);
                emitter.onNext(1);
                emitter.onNext(1);
                emitter.onError(new Throwable("发生错误了"));
            }
            //当Observable每次发送1次数据事件就会调用1次
        }).doOnEach(new Consumer<Notification<Integer>>() {
            @Override
            public void accept(Notification<Integer> integerNotification) throws Throwable {

            }
            //执行next事件前调用
        }).doOnNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {

            }
            //执行next事件后调用
        }).doAfterNext(new Consumer<Integer>() {
            @Override
            public void accept(Integer integer) throws Throwable {

            }
            //Observable正常发送事件完毕后调用
        }).doOnComplete(new Action() {
            @Override
            public void run() throws Throwable {

            }
            //Observable发送错误事件时调用
        }).doOnError(new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Throwable {

            }
            //观察者订阅时调用
        }).doOnSubscribe(new Consumer<Disposable>() {
            @Override
            public void accept(Disposable disposable) throws Throwable {

            }
            //Observable发送事件完毕后调用，无论正常发送完毕/异常终止
        }).doAfterTerminate(new Action() {
            @Override
            public void run() throws Throwable {

            }
            //最后执行
        }).doFinally(new Action() {
            @Override
            public void run() throws Throwable {

            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Integer integer) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

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