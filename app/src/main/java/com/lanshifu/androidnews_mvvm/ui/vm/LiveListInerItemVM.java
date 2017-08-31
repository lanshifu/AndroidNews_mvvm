package com.lanshifu.androidnews_mvvm.ui.vm;

import android.content.Context;
import android.content.Intent;
import android.databinding.ObservableField;

import com.lanshifu.androidnews_mvvm.bean.Recommend;
import com.lanshifu.androidnews_mvvm.bean.Room;
import com.lanshifu.androidnews_mvvm.bean.RoomLine;
import com.lanshifu.androidnews_mvvm.net.MyObserver;
import com.lanshifu.androidnews_mvvm.net.RetrofitHelper;
import com.lanshifu.androidnews_mvvm.net.RxSchedulerHelper;
import com.lanshifu.androidnews_mvvm.ui.activity.LivePlayActivity;

import me.goldze.mvvmhabit.base.BaseViewModel;
import me.goldze.mvvmhabit.binding.command.BindingCommand;
import me.goldze.mvvmhabit.utils.ToastUtils;
import rx.functions.Action0;

/**
 * Created by lanxiaobin on 2017/8/31.
 */

public class LiveListInerItemVM extends BaseViewModel {

   private Recommend.RoomBean.ListBean listBean;

    public LiveListInerItemVM(Context context, Recommend.RoomBean.ListBean listBean) {
        super(context);
        this.listBean = listBean;
        icon.set(listBean.getThumb());
        title.set(listBean.getTitle());
        name.set(listBean.getNick());
        viewer.set(listBean.getView());
    }


    public ObservableField<String> icon = new ObservableField<>();
    public ObservableField<String> title = new ObservableField<>();
    public ObservableField<String> name = new ObservableField<>();
    public ObservableField<String> viewer = new ObservableField<>();

    public BindingCommand onClickCommand = new BindingCommand(new Action0() {
        @Override
        public void call() {
             String uid = String.valueOf(listBean.getUid());
            enterRoom(uid);

        }
    });



    private void enterRoom(String uid){
        RetrofitHelper.getLiveApi().enterRoom(uid)
                .compose(RxSchedulerHelper.<Room>io_main())
                .subscribe(new MyObserver<Room>() {
                    @Override
                    public void _onNext(Room room) {
                        RoomLine ws = room.getLive().getWs();
//                        mView.playUrl();
                        RoomLine roomLine = room.getLive().getWs();

                        RoomLine.FlvBean flv = roomLine.getFlv();
                        String url = "";
                        if(flv!=null){
                            url = flv.getValue(false).getSrc();
                        }else{
                            url = roomLine.getHls().getValue(false).getSrc();
                        }

                        Intent intent = new Intent(context, LivePlayActivity.class);
                        intent.putExtra("url",url);
                        context.startActivity(intent);
                    }

                    @Override
                    public void _onError(String e) {
                       ToastUtils.showShort("进入房间失败");

                    }
                });
    }

}
