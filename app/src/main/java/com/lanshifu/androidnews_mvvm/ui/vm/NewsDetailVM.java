package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableBoolean;
import android.databinding.ObservableField;
import android.databinding.ObservableList;
import android.util.Log;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.bean.WechatItem;
import com.lanshifu.androidnews_mvvm.net.RetrofitHelper;
import com.lanshifu.androidnews_mvvm.net.RxSchedulerHelper;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.Subscriber;
import rx.functions.Action0;

/**
 * Created by lanxiaobin on 2017/8/30.
 */

public class NewsDetailVM extends BaseViewModel {



    public NewsDetailVM(Context context) {
        super(context);
    }


    private static final String TAG = "NewsDetailVM";



    public ObservableBoolean isRequestSuccess = new ObservableBoolean(false);

    public ObservableField<String> uri = new ObservableField<>();
    public ObservableField<String> imageUrl = new ObservableField<>();


}
