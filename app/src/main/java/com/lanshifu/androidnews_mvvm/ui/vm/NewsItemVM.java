package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.os.Bundle;

import com.lanshifu.androidnews_mvvm.bean.WechatItem;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import rx.functions.Action0;

/**
 * Created by lanxiaobin on 2017/8/29.
 */

public class NewsItemVM extends BaseViewModel {

    public NewsItemVM(Context context, WechatItem.ResultBean.ListBean listBean) {
        super(context);
        this.listBean = listBean;
    }

    public WechatItem.ResultBean.ListBean listBean;



    //条目的点击事件
    public BindingCommand itemClick = new BindingCommand(new Action0() {
        @Override
        public void call() {
            //跳转到详情界面,传入条目的实体对象
            Bundle mBundle = new Bundle();
            mBundle.putParcelable("entity", listBean);
            ToastUtils.showShort("点击了"+listBean.getTitle());
//            startContainerActivity(DetailFragment.class.getCanonicalName(), mBundle);
        }
    });


}
