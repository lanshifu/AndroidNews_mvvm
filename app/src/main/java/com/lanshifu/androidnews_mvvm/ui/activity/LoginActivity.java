package com.lanshifu.androidnews_mvvm.ui.activity;

import android.util.Log;

import com.lanshifu.androidnews_mvvm.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.ActivityLoginBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.LoginViewModel;

import me.goldze.mvvmhabit.base.BaseActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding,LoginViewModel> {
    private final static String TAG = "LoginActivity";

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_login);
//    }

    @Override
    public int initContentView() {
        return R.layout.activity_login;
    }

    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }

    @Override
    public LoginViewModel initViewModel() {
        //View持有ViewModel的引用 (这里暂时没有用Dagger2解耦)
        return new LoginViewModel(this);
    }

    @Override
    public void initViewObservable() {
        String s = viewModel.userName.get();
        Log.e(TAG, "initViewObservable: "+s );
    }
}
