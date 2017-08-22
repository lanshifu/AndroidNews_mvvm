package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.ObservableField;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import java.util.Observable;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import rx.functions.Action0;
import rx.functions.Action1;

/**
 * Created by Administrator on 2017/8/22.
 */

public class LoginViewModel extends BaseViewModel {
    private static final String TAG = "LoginViewModel" ;

    public LoginViewModel(Context context) {
        super(context);
    }

    //用户名/密码 的绑定
    public ObservableField<String> userName = new ObservableField<>("");
    public ObservableField<String> password = new ObservableField<>("");

    public BindingCommand<Boolean> onFocusChangeCommand = new BindingCommand<Boolean>(new Action1<Boolean>() {
        @Override
        public void call(Boolean aBoolean) {
                String username = userName.get();
                if (aBoolean){
                    Log.e(TAG, "焦点: "+username );
                }
        }
    });

    //登录按钮的点击事件
    public BindingCommand loginOnClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            Log.e(TAG, "login");
            login();
        }
    });

    /**
     * 网络模拟一个登陆操作
     **/
    private void login() {
        if (TextUtils.isEmpty(userName.get())) {
            Log.e(TAG, "userName: =null");
            ToastUtils.showShort("请输入账号！");
            return;
        }
        if (TextUtils.isEmpty(password.get())) {
            ToastUtils.showShort("请输入密码！");
            return;
        }
        Log.e(TAG, "login: user="+userName.get());
        Log.e(TAG, "login: pass="+password.get());
        Toast.makeText(context,userName.get()+","+password.get(),Toast.LENGTH_SHORT).show();
//        ToastUtils.showShort(userName.get()+","+password.get());
        showDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dismissDialog();
            }
        }, 3 * 1000);
    }

}
