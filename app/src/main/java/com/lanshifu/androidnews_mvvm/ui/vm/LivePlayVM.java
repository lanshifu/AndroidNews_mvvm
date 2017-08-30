package com.lanshifu.androidnews_mvvm.ui.vm;

import android.app.Activity;
import android.content.Context;
import android.databinding.ObservableBoolean;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/8/30.
 */

public class LivePlayVM extends BaseViewModel {

    public LivePlayVM(Context context) {
        super(context);
    }


    public ObservableBoolean isClickFullScreen = new ObservableBoolean(false);
    public ObservableBoolean isScreenClick = new ObservableBoolean(false);

    public BindingCommand<Boolean> onFullScreenClickCommand = new BindingCommand<Boolean>(new Action1<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
            isClickFullScreen.set(!isClickFullScreen.get());
        }
    });

    public BindingCommand onBackClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            ((Activity)context).finish();
        }
    });
    public BindingCommand onScreenClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            isScreenClick.set(!isScreenClick.get());
        }
    });
}
