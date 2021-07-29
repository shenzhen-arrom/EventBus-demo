package com.langyi.rxlogin;

import java.util.Map;

/**
 * 作者：Arrom
 * 日期：2021/7/28
 * 描述：返回的结果信息
 */

public class RxLoginResult {
    private boolean isSuccess;
    private String msg;
    private Map<String, String> userInfoMap;//用户存放用户信息
    private LoginPlatform platform;//平台

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Map<String, String> getUserInfoMap() {
        return userInfoMap;
    }

    public void setUserInfoMap(Map<String, String> userInfoMap) {
        this.userInfoMap = userInfoMap;
    }

    public LoginPlatform getPlatform() {
        return platform;
    }

    public void setPlatform(LoginPlatform platform) {
        this.platform = platform;
    }
}
