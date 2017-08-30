package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;

/**
 * Created by lanxiaobin on 2017/8/30.
 */

public class LiveFragmentVM extends BaseViewModel{

    private static final String TAG = "LiveFragmentVM";


    private String []names = new String[]{
            "香港电影","综艺频道","高清音乐","动作电影","电影","周星驰","成龙","喜剧","儿歌","LIVE生活"
    };

    private String []urls = new String[]{
            "http://live.gslb.letv.com/gslb?stream_id=lb_hkmovie_1300&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=letv&expect=1",
            "http://live.gslb.letv.com/gslb?stream_id=lb_ent_1300&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=letv&expect=1",
            "http://live.gslb.letv.com/gslb?stream_id=lb_music_1300&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=letv&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_dzdy_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_movie_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_zxc_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_cl_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_comedy_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_erge_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1",
            "http://live.gslb.letv.com/gslb?tag=live&stream_id=lb_livemusic_720p&tag=live&ext=m3u8&sign=live_tv&platid=10&splatid=1009&format=C1S&expect=1"
    };



    public LiveFragmentVM(Context context) {
        super(context);

        initData();
    }

    private void initData() {
        for (int i = 0; i < names.length; i++) {
            String name = names[i];
            String url = urls[i];
            observableList.add(new LiveItemVM(context,new LiveBean(name,url)));
        }
    }


    //给RecyclerView添加ObservableList
    public final ObservableList<LiveItemVM> observableList = new ObservableArrayList<>();

    public ItemViewSelector<LiveItemVM> itemView = new ItemViewSelector<LiveItemVM>() {

        @Override
        public void select(ItemView itemView, int i, LiveItemVM liveItemVM) {
            itemView.set(BR.viewmodel,R.layout.item_live_list);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };




   public static class LiveBean{

        public String name;
        public String url;
        public LiveBean(String name, String url) {
            this.name = name;
            this.url = url;
        }

    }
}
