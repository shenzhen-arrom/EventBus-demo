package com.langyi.rxlogin;

import android.app.Activity;
import android.content.Intent;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.subjects.PublishSubject;


/**
 * 作者：Arrom
 * 日期：2021/7/28
 * 描述：第三方登录（基于事件流）
 * 需要在配置注册文件注册一下activity，并且设置是透明的
 *
 */

public class RxLogin implements UMAuthListener {

    private Activity activity;

    static UMAuthListener mUMAuthListener;

    private RxLoginResult loginResult;

    private PublishSubject<RxLoginResult> mEmitter;


    private RxLogin(Activity activity){
        this.activity =activity;
        mUMAuthListener = this;
        loginResult = new RxLoginResult();
        mEmitter = PublishSubject.create();
    }

    public static RxLogin create(Activity activity){
        return new RxLogin(activity);
    }

    public Observable<RxLoginResult> doOauthVerify(LoginPlatform platform){
        Intent  intent = new Intent(activity,RxLoginActivity.class);
        intent.putExtra(RxLoginActivity.PLATFORM_KEY,platform);
        activity.startActivity(intent);
        activity.overridePendingTransition(0,0);
        //设置平台
        loginResult.setPlatform(platform);
        List<Observable<RxLoginResult>> list = new ArrayList<>();
        list.add(mEmitter);
        return  Observable.concat(Observable.fromIterable(list));
    }
    @Override
    public void onStart(SHARE_MEDIA share_media) {

    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
         loginResult.setSuccess(true);
         loginResult.setUserInfoMap(map);
         loginResult.setMsg("获取用户信息成功");
         mEmitter.onNext(loginResult);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        loginResult.setSuccess(false);
        loginResult.setMsg("获取用户信息失败");
        mEmitter.onNext(loginResult);
        throwable.printStackTrace();
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        loginResult.setSuccess(false);
        loginResult.setMsg("用户取消第三方登录");
        mEmitter.onNext(loginResult);
    }





}
