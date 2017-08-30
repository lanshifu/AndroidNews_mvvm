package me.goldze.mvvmhabit.base;

/**
 * Created by goldze on 2017/6/15.
 */

public interface IBaseViewModel {
//    void initData();

    /**
     * View的界面创建时回调
     */
    void onCreateView();

    /**
     * View的界面销毁时回调
     */
    void onDestroyView();

    /**
     * 注册RxBus
     */
    void registerRxBus();
    /**
     * 移除RxBus
     */
    void removeRxBus();
}
