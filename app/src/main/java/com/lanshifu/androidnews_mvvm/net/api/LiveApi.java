package com.lanshifu.androidnews_mvvm.net.api;

import com.lanshifu.androidnews_mvvm.bean.Recommend;
import com.lanshifu.androidnews_mvvm.bean.Room;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by Administrator on 2017/8/19.
 */

public interface LiveApi {

    /**
     * 获取分类列表
     * @return
     *
     * categories/list.json
     */
//    @GET("json/app/index/category/info-android.json?v=3.0.1&os=1&ver=4")
//    Observable<List<LiveCategory>> getAllCategories();

    /**
     * 获取推荐列表
     * @return
     */
    @GET("json/app/index/recommend/list-android.json?v=3.0.1&os=1&ver=4")
    Observable<Recommend> getRecommend();

    /**
     * 进入房间
     * @param uid
     * @return
     */
    @GET("json/rooms/{uid}/info.json?v=3.0.1&os=1&ver=4")
    Observable<Room> enterRoom(@Path("uid") String uid);

}
