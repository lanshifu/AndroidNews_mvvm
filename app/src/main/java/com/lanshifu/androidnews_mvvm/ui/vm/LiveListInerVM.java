package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.databinding.ObservableList;

import com.android.databinding.library.baseAdapters.BR;
import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.bean.Recommend;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import me.tatarka.bindingcollectionadapter.ItemView;
import me.tatarka.bindingcollectionadapter.ItemViewSelector;
import rx.functions.Action0;

/**
 * Created by lanxiaobin on 2017/8/31.
 */

public class LiveListInerVM extends BaseViewModel {

    private Recommend.RoomBean roomBean ;

    public LiveListInerVM(Context context, Recommend.RoomBean roomBean) {
        super(context);
        this.roomBean = roomBean;

        title.set(roomBean.getName());
        icon.set(roomBean.getIcon());

        initItems();

    }

    private void initItems() {

        for (Recommend.RoomBean.ListBean listBean : this.roomBean.getList()) {
            observableList.add(new LiveListInerItemVM(context,listBean));
        }
    }

    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> icon = new ObservableField<>();

    public BindingCommand onMoreClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
            ToastUtils.showShort("more" +title.get());
        }
    });


    //给RecyclerView添加ObservableList
    public final ObservableList<LiveListInerItemVM> observableList = new ObservableArrayList<>();

    public ItemViewSelector<LiveListInerItemVM> itemView = new ItemViewSelector<LiveListInerItemVM>() {

        @Override
        public void select(ItemView itemView, int i, LiveListInerItemVM liveListInerItemVM) {
            itemView.set(BR.viewmodel, R.layout.item_live_list_iner_item);
        }

        @Override
        public int viewTypeCount() {
            return 1;
        }
    };



}
