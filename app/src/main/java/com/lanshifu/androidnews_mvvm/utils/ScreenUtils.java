package com.lanshifu.androidnews_mvvm.utils;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import me.goldze.mvvmhabit.utils.Utils;

/**
 * Created by Administrator on 2017/8/30.
 */

public class ScreenUtils {


    public static int getScreenWidth() {
        WindowManager windowManager = (WindowManager) Utils.getContext().getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight() {
        WindowManager windowManager = (WindowManager)Utils.getContext().getSystemService("window");
        DisplayMetrics dm = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static void setPortrait(Activity activity) {
        activity.setRequestedOrientation(1);
    }

    public static void setLandscape(Activity activity) {
        activity.setRequestedOrientation(0);
    }
}



