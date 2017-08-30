package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.ObservableField;

import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by lanxiaobin on 2017/8/30.
 */

public class LiveItemVM extends BaseViewModel {

    private LiveFragmentVM.LiveBean liveBean;

    public LiveItemVM(Context context, LiveFragmentVM.LiveBean liveBean) {
        super(context);
        this.liveBean = liveBean;

        title.set(liveBean.name);
    }

    public ObservableField<String> title = new ObservableField<>();

}
