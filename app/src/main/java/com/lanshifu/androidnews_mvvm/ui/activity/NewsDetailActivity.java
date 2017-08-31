package com.lanshifu.androidnews_mvvm.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.lanshifu.androidnews_mvvm.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.bean.WechatItem;
import com.lanshifu.androidnews_mvvm.databinding.ActivityNewsDetailBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.NewsDetailVM;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.base.BaseViewModel;

public class NewsDetailActivity extends BaseActivity<ActivityNewsDetailBinding,NewsDetailVM> {

    private WechatItem.ResultBean.ListBean listBean;

    @Override
    public int initContentView() {
        return R.layout.activity_news_detail;
    }

    @Override
    public int initVariableId() {
        return BR.viewmodel;
    }

    @Override
    public NewsDetailVM initViewModel() {
        return new NewsDetailVM(this);
    }

    @Override
    public void initParam() {
        super.initParam();
        listBean = getIntent().getParcelableExtra("listBean");
    }

    @Override
    public void initViewObservable() {
        super.initViewObservable();

        initToolbar();
//        initDataByGetIntent();

        binding.webviewWechat.loadUrl(listBean.getUrl());

        viewModel.imageUrl.set(listBean.getFirstImg());

    }

    private void initToolbar() {
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(listBean.getTitle());
    }

    private void initDataByGetIntent() {
//        boolean isNotLoad = (boolean) PrefUtil.getInstance().getBoolean("notload", false);
        boolean isNotLoad = false;
        if (!isNotLoad) {
            Glide.with(this)
                    .load(listBean.getFirstImg())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .crossFade(1000)
                    .into(binding.ivImage);
        }


    }
}
