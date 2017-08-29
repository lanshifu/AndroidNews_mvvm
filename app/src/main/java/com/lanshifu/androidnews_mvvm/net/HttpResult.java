package com.lanshifu.androidnews_mvvm.net;

import com.lanshifu.androidnews_mvvm.net.api.ApiConstant;

/**
 * Created by lWX385269 lanshifu on 2017/4/10.
 */

public class HttpResult<T> {

    public int code;
    public String msg;
    public T data;


    public boolean isSuccess() {
        return code == ApiConstant.SUCCESS;
    }

    public boolean isEmpty() {
        return code == ApiConstant.EMPTY;
    }

    public boolean isNoMore() {
        return code == ApiConstant.NOMORE;
    }
}