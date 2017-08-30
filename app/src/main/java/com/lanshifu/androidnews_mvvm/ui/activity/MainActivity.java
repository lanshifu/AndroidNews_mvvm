package com.lanshifu.androidnews_mvvm.ui.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.ActivityMainBinding;
import com.lanshifu.androidnews_mvvm.ui.fragment.DemoFragment;
import com.lanshifu.androidnews_mvvm.ui.fragment.LiveFragment;
import com.lanshifu.androidnews_mvvm.ui.fragment.MainFragment;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;


    private Class[] fragments = new Class[]{
            MainFragment.class, LiveFragment.class,
            DemoFragment.class, DemoFragment.class};


    private String[] titles = new String[]{"主页", "周边", "我的", "更多"};

    private int[] icons = new int[]{
            R.drawable.tab_home_selector, R.drawable.tab_around_selector,
            R.drawable.tab_me_selector, R.drawable.tab_more_selector};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //隐藏系统自带的状态栏
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //隐藏掉整个ActionBar
        getSupportActionBar().hide();

//        setContentView(R.layout.activity_main);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        initView();
    }


    private void initView() {
        binding.mainTabHost.setup(this, getSupportFragmentManager(), android.R.id.tabcontent);
        for (int i = 0; i < fragments.length; i++) {
            View view = getLayoutInflater().inflate(R.layout.item_tab, null);
            ImageView tabIcon = (ImageView) view.findViewById(R.id.item_tab_iv);
            TextView tabTitle = (TextView) view.findViewById(R.id.item_tab_tv);
            tabIcon.setImageResource(icons[i]);
            tabTitle.setText(titles[i]);
            binding.mainTabHost.addTab(binding.mainTabHost.newTabSpec("" + i).setIndicator(view),
                    fragments[i], null);
        }

    }

}
