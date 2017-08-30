package com.lanshifu.androidnews_mvvm.ui.fragment;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.FragmentLiveBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.LiveFragmentVM;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by lanxiaobin on 2017/8/30.
 */

public class LiveFragment extends BaseFragment<FragmentLiveBinding,LiveFragmentVM> {
    @Override
    public int initContentView() {
        return R.layout.fragment_live;
    }

    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }

    @Override
    public LiveFragmentVM initViewModel() {
        return new LiveFragmentVM(getContext());
    }
}
