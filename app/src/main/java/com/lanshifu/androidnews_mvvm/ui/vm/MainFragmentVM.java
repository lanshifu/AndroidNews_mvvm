package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.Observable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.bean.WechatItem;
import com.lanshifu.androidnews_mvvm.net.MyObserver;
import com.lanshifu.androidnews_mvvm.net.RetrofitHelper;
import com.lanshifu.androidnews_mvvm.net.RxSchedulerHelper;
import com.lcodecore.tkrefreshlayout.utils.LogUtil;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by lanxiaobin on 2017/8/29.
 */

public class MainFragmentVM extends BaseViewModel {

    public static final String WECHAT_APPKEY = "26ce25ffcfc907a26263e2b0e3e23676";
    //每页请求的 item 数量
    public final int mPs = 21;
    private static final String TAG = "MainFragmentVM";

    public int mPageMark = 1;


    public MainFragmentVM(Context context) {
        super(context);
        requestNetWork();
    }


    public ObservableField<String> text = new ObservableField<>("12346789");

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
        }
    });


    public ObservableBoolean isRequestSuccess = new ObservableBoolean(false);


    //给RecyclerView添加ObservableList
    public final ObservableList<NewsItemVM> observableList = new ObservableArrayList<>();

    public ItemViewSelector<NewsItemVM> itemView = new ItemViewSelector<NewsItemVM>() {
        @Override
        public void select(ItemView itemView, int i, NewsItemVM newsItemVM) {
            Log.e(TAG, "select: " );
            itemView.set(BR.viewModel, R.layout.item_news);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };

    public void requestNetWork() {
        RetrofitHelper.getWechatApi()
                .getWechat(WECHAT_APPKEY, mPageMark, mPs)
                .compose(RxSchedulerHelper.<WechatItem>io_main())
                .subscribe(new Subscriber<WechatItem>() {
                    @Override
                    public void onCompleted() {
//                        ToastUtils.showShort("onCompleted");
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        ToastUtils.showShort("onError:"+throwable.getMessage());

                    }

                    @Override
                    public void onNext(WechatItem wechatItem) {
                        ToastUtils.showShort("onNext"+wechatItem.getResult().getList().size());
                        for (WechatItem.ResultBean.ListBean listBean : wechatItem.getResult().getList()) {
                            observableList.add(new NewsItemVM(context,listBean));
                        }
//                        isRequestSuccess.set(true);

                    }
                });
//                .subscribe(new MyObserver<WechatItem>() {
//                    @Override
//                    public void _onNext(WechatItem wechatItem) {
//                        ToastUtils.showShort(wechatItem.getResult().getList().size());
//                        observableList.addAll(wechatItem.getResult().getList());
//                    }
//
//                    @Override
//                    public void _onError(String e) {
//                        ToastUtils.showShort(e);
//
//                    }
//                });
    }


}
