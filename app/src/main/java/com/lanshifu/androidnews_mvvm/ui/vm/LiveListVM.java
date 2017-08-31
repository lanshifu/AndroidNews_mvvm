package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableList;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.bean.Recommend;
import com.lanshifu.androidnews_mvvm.net.MyObserver;
import com.lanshifu.androidnews_mvvm.net.RetrofitHelper;
import com.lanshifu.androidnews_mvvm.net.RxSchedulerHelper;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by lanxiaobin on 2017/8/31.
 */

public class LiveListVM extends BaseViewModel {

    private static final String TAG = "LiveListVM";
    public LiveListVM(Context context) {
        super(context);

        requestListInfos(true);
    }


    public BindingCommand onLoadMoreCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            ToastUtils.showShort("加载更多");
        }
    });



    //下拉刷新
    public BindingCommand onRefreshCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            ToastUtils.showShort("下拉刷新");
            requestListInfos(true);
        }
    });


    public ObservableBoolean isRequestSuccess = new ObservableBoolean(false);


    //给RecyclerView添加ObservableList
    public final ObservableList<LiveListInerVM> observableList = new ObservableArrayList<>();

    public ItemViewSelector<LiveListInerVM> itemView = new ItemViewSelector<LiveListInerVM>() {

        @Override
        public void select(ItemView itemView, int i, LiveListInerVM liveListInerVM) {
            itemView.set(BR.viewmodel, R.layout.list_remmend_item);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };


    private void requestListInfos(final boolean isRefresh) {

        RetrofitHelper.getLiveApi().getRecommend()
                .compose(RxSchedulerHelper.<Recommend>io_main())
                .subscribe(new MyObserver<Recommend>() {
                    @Override
                    public void _onNext(Recommend recommend) {

                        if(isRefresh){
                            observableList.clear();
                        }
                        for (Recommend.RoomBean roomBean : recommend.getRoom()) {
                            observableList.add(new LiveListInerVM(context,roomBean));
                        }

                    }

                    @Override
                    public void _onError(String e) {
                       ToastUtils.showShort(e);
                    }
                });
    }
}
