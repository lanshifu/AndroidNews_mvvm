package com.lanshifu.androidnews_mvvm.ui.fragment;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.FragmentLivelistBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.LiveListVM;

import me.goldze.mvvmhabit.base.BaseFragment;
import me.goldze.mvvmhabit.base.BaseViewModel;

/**
 * Created by lanxiaobin on 2017/8/31.
 */

public class LiveListFragment extends BaseFragment<FragmentLivelistBinding,LiveListVM> {
    @Override
    public int initContentView() {
        return R.layout.fragment_livelist;
    }

    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }

    @Override
    public LiveListVM initViewModel() {
        return new LiveListVM(getActivity());
    }
}
