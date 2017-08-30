package com.lanshifu.androidnews_mvvm.ui.fragment;

import android.databinding.Observable;

import com.lanshifu.androidnews_mvvm.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.FragmentMainBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.MainFragmentVM;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.utils.ToastUtils;

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
        return new MainFragmentVM(this.getContext());
    }

    @Override
    public void initViewObservable() {
        viewModel.isRequestSuccess.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                binding.smartRefreshLayout.finishRefresh();
                binding.smartRefreshLayout.finishLoadmore();
            }
        });

        binding.smartRefreshLayout.autoRefresh();
    }
}
