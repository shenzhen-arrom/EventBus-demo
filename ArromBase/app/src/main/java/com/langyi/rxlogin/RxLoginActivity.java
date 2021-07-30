package com.langyi.rxlogin;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Map;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;

/**
 * 作者：Arrom
 * 日期： 2021/7/28
 * 描述：
 */

public class RxLoginActivity extends Activity implements UMAuthListener {

    public static final String PLATFORM_KEY = "PLATFORM_KEY";

    private UMShareAPI umShareAPI;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        umShareAPI = UMShareAPI.get(this);
        LoginPlatform platform= (LoginPlatform) getIntent().getSerializableExtra(PLATFORM_KEY);
        umShareAPI.deleteOauth(this,platformChange(platform),this);
        umShareAPI.getPlatformInfo(this, platformChange(platform),this);





    }

    /**
     * 平台转换
     * @param platform
     * @return
     */
    private SHARE_MEDIA platformChange(LoginPlatform platform) {
        switch (platform){
            case WX:
                return SHARE_MEDIA.WEIXIN;
            case QQ:
                return SHARE_MEDIA.QQ;
        }
        return SHARE_MEDIA.QQ;
    }

    @Override
    public void onStart(SHARE_MEDIA share_media) {
        RxLogin.mUMAuthListener.onStart(share_media);
    }

    @Override
    public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
       //结果回传
        RxLogin.mUMAuthListener.onComplete(share_media,i,map);
        finish();
        overridePendingTransition(0,0);
    }

    @Override
    public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {
        RxLogin.mUMAuthListener.onError(share_media,i,throwable);
    }

    @Override
    public void onCancel(SHARE_MEDIA share_media, int i) {
        RxLogin.mUMAuthListener.onCancel(share_media,i);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }
}
