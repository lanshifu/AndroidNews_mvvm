package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import com.lanshifu.androidnews_mvvm.ui.activity.LivePlayActivity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import rx.functions.Action0;

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

    public BindingCommand onClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {

            Intent intent = new Intent(context, LivePlayActivity.class);
            intent.putExtra("url",liveBean.url);
            context.startActivity(intent);
        }
    });

}
