package com.lanshifu.androidnews_mvvm.net.api;


import com.lanshifu.androidnews_mvvm.bean.WechatItem;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/*******************************************************************
 * * * * *   * * * *   *     *       Created by OCN.Yang
 * *     *   *         * *   *       Time:2017/2/24 14:53.
 * *     *   *         *   * *       Email address:ocnyang@gmail.com
 * * * * *   * * * *   *     *.Yang  Web site:www.ocnyang.com
 *******************************************************************/


public interface WechatApi {
    @GET("weixin/query")
    Observable<WechatItem> getWechat(@Query("key") String appkey,
                                     @Query("pno") int pno,
                                     @Query("ps") int ps);
}
