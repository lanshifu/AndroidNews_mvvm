package com.lanshifu.androidnews_mvvm.ui.fragment;

import com.lanshifu.androidnews_mvvm.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.FragmentMainBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.MainFragmentVM;

import me.goldze.mvvmhabit.base.BaseFragment;

/**
 * Created by lanxiaobin on 2017/8/29.
 */

public class MainFragment extends BaseFragment<FragmentMainBinding,MainFragmentVM>{


    @Override
    public int initContentView() {
        return R.layout.fragment_main;
    }

    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }

    @Override
    public MainFragmentVM initViewModel() {
        return new MainFragmentVM();
    }

    @Override
    public void initViewObservable() {
        viewModel.requestNetWork();
    }
}
