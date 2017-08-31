package com.lanshifu.androidnews_mvvm.ui.activity;

import android.databinding.Observable;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.ActivityLivePlayBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.LivePlayVM;
import com.lanshifu.androidnews_mvvm.utils.ScreenUtils;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;

import java.io.IOException;

import me.goldze.mvvmhabit.BR;
import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.ToastUtils;

public class LivePlayActivity extends BaseActivity<ActivityLivePlayBinding,LivePlayVM> implements
        PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnErrorListener,
        Handler.Callback {

    private PLMediaPlayer mediaPlayer;  //媒体控制器
    private boolean isSurfaceViewInit = false;         //SurfaceView初始化标志位
    private int surfacePortraitWidth;
    private int surfacePortraitHeight;
    private int videoWidth;
    private int videoHeight;
    private Handler controllerHandler;
    private static final int HANDLER_HIDE_CONTROLLER = 100;//隐藏MediaController
    private static final int HANDLER_CONTROLLER_DURATION = 5 * 1000;//MediaController显示时间
    private boolean isControllerHiden = false;   //MediaController显示隐藏标志位
    private boolean isVideoPrepared = false;         //Video加载标志位，用于显示隐藏ProgreeBar
    private boolean isPause = false;         //直播暂停标志位
    private boolean isFullscreen = false;   //全屏标志位
    private int playWidth;
    private int playHeight;
    private String live_url;

    @Override
    public int initContentView() {
        return R.layout.activity_live_play;
    }

    @Override
    public int initVariableId() {
        return com.lanshifu.androidnews_mvvm.BR.viewmodel;
    }

    @Override
    public LivePlayVM initViewModel() {
        return new LivePlayVM(LivePlayActivity.this);
    }


    @Override
    public void initData() {

        controllerHandler = new Handler(this);

        initSurfaceView();

        live_url = getIntent().getStringExtra("url");
        if (live_url == null) {
            ToastUtils.showShort("地址null");
            return;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (mediaPlayer != null && isPause && !TextUtils.isEmpty(live_url)) {
            try {
//                AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//                audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

                mediaPlayer.reset();
                mediaPlayer.setDataSource(live_url);
                mediaPlayer.prepareAsync();
                isPause = false;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            isPause = true;
//            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            audioManager.abandonAudioFocus(null);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
//            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            audioManager.abandonAudioFocus(null);
        }

    }

    private void initSurfaceView() {
        binding.surfaceview.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                binding.progressbar.setVisibility(View.VISIBLE);
                prepareMediaPlayer();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                if (!isSurfaceViewInit) {
                    //竖屏
                    surfacePortraitWidth = width;
                    surfacePortraitHeight = height;

                    isSurfaceViewInit = true;
                }
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mediaPlayer != null) {
                    mediaPlayer.setDisplay(null);
                }
            }
        });

        //MediaController
        controllerHandler.removeMessages(HANDLER_HIDE_CONTROLLER);
        controllerHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CONTROLLER, HANDLER_CONTROLLER_DURATION);


    }


    /**
     * 配置MediaPlayer相关参数
     */
    private void prepareMediaPlayer() {

        if (mediaPlayer != null) {
            mediaPlayer.setDisplay(binding.surfaceview.getHolder());
            return;
        }

        try {
            AVOptions avOptions = new AVOptions();
            avOptions.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);  //直播流：1->是 0->否
            avOptions.setInteger(AVOptions.KEY_MEDIACODEC, 0);      //解码类型 1->硬解 0->软解
            avOptions.setInteger(AVOptions.KEY_START_ON_PREPARED, 0);//缓冲结束后自动播放
            avOptions.setInteger(AVOptions.KEY_DELAY_OPTIMIZATION, 1);
            avOptions.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
            avOptions.setInteger(AVOptions.KEY_BUFFER_TIME, 10 * 1000);
            avOptions.setInteger(AVOptions.KEY_GET_AV_FRAME_TIMEOUT, 10 * 1000);
            avOptions.setInteger(AVOptions.KEY_CACHE_BUFFER_DURATION, 10 * 1000);
            avOptions.setInteger(AVOptions.KEY_MAX_CACHE_BUFFER_DURATION, 15 * 1000);

//            AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
//            audioManager.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN);

            mediaPlayer = new PLMediaPlayer(this, avOptions);

            mediaPlayer.setOnPreparedListener(this);
            mediaPlayer.setOnVideoSizeChangedListener(this);
            mediaPlayer.setOnCompletionListener(this);
            mediaPlayer.setOnInfoListener(this);
            mediaPlayer.setOnErrorListener(this);

            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
//            mediaPlayer.setDataSource(live_url);
            mediaPlayer.setDisplay(binding.surfaceview.getHolder());

            mediaPlayer.setDataSource(live_url);//加载直播链接进行播放
            mediaPlayer.prepareAsync();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == HANDLER_HIDE_CONTROLLER) {
            //hide controller
            binding.layoutPortrait.setVisibility(View.GONE);
            isControllerHiden = true;
        }
        return true;
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        ToastUtils.showShort("onError"+i);
        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
        switch (what) {
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_START://开始缓冲
//                isVideoPrepared = false;
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_BUFFERING_START");
                break;
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_END://缓冲结束
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_BUFFERING_END");
                break;
            case PLMediaPlayer.MEDIA_INFO_BUFFERING_BYTES_UPDATE:
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_BUFFERING_BYTES_UPDATE");
                break;
            case PLMediaPlayer.MEDIA_INFO_NOT_SEEKABLE:
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_NOT_SEEKABLE");
                break;
            case PLMediaPlayer.MEDIA_INFO_VIDEO_ROTATION_CHANGED:
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_VIDEO_ROTATION_CHANGED");
                break;
            case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_AUDIO_RENDERING_START");
                break;
            case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START://视频缓冲完成可播放
                binding.progressbar.setVisibility(View.GONE);
                isVideoPrepared = true;
                isPause = false;
                Log.d("PLMediaPlayer", "onInfo: MEDIA_INFO_VIDEO_RENDERING_START");
                break;
            default:
                Log.d("PLMediaPlayer", "onInfo: " + what);
                break;
        }
        return true;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        binding.progressbar.setVisibility(isVideoPrepared ? View.GONE : View.VISIBLE);
        mediaPlayer.start();
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height, int i2, int i3) {
        videoWidth = width;
        videoHeight = height;

        if (videoWidth != 0 && videoHeight != 0) {
            float ratioW = (float) videoWidth / (float) (isFullscreen ? ScreenUtils.getScreenWidth() : surfacePortraitWidth);
            float ratioH = (float) videoHeight / (float) (isFullscreen ? ScreenUtils.getScreenHeight() : surfacePortraitHeight);
            float ratio = Math.max(ratioW, ratioH);
            playWidth = (int) Math.ceil((float) videoWidth / ratio);
            playHeight = (int) Math.ceil((float) videoHeight / ratio);
            FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(playWidth, playHeight);
            lp.gravity = Gravity.CENTER;
            binding.surfaceview.setLayoutParams(lp);
        }
    }

    //进入全屏
    private void enterFullscreen() {

        binding.layoutTop.removeView(binding.surfaceview);
        binding.layoutTop.removeView(binding.progressbar);
        binding.layoutTop.removeView(binding.layoutPortrait);

        ScreenUtils.setLandscape(this);

        isFullscreen = true;
        isControllerHiden = false;
        controllerHandler.removeMessages(HANDLER_HIDE_CONTROLLER);
        controllerHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CONTROLLER, HANDLER_CONTROLLER_DURATION);

        binding.layoutTop.addView(binding.surfaceview, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.layoutTop.addView(binding.progressbar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.layoutTop.addView(binding.layoutPortrait, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //全屏隐藏状态栏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
        getWindow().setAttributes(lp);

        binding.layoutBottom.setVisibility(View.GONE);
//        progressbar.setVisibility(isVideoPrepared == true ? View.GONE : View.VISIBLE);
        binding.layoutPortrait.setVisibility(View.GONE);
//        iv_play_pause_landscape.setImageResource(isPause ? R.drawable.selector_btn_play : R.drawable.selector_btn_pause);

    }

    //退出全屏
    private void exitFullscreen() {

        binding.layoutTop.removeView(binding.surfaceview);
        binding.layoutTop.removeView(binding.progressbar);
        binding.layoutTop.removeView(binding.layoutPortrait);

        ScreenUtils.setPortrait(this);

        isFullscreen = false;
        isControllerHiden = false;
        controllerHandler.removeMessages(HANDLER_HIDE_CONTROLLER);
        controllerHandler.sendEmptyMessageDelayed(HANDLER_HIDE_CONTROLLER, HANDLER_CONTROLLER_DURATION);

        binding.layoutTop.addView(binding.surfaceview, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.layoutTop.addView(binding.progressbar, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        binding.layoutTop.addView(binding.layoutPortrait, new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        onVideoSizeChanged(mediaPlayer, videoWidth, videoHeight, 0, 0);

        //显示状态栏
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setAttributes(lp);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        binding.layoutBottom.setVisibility(View.VISIBLE);
//        progressbar.setVisibility(isVideoPrepared == true ? View.GONE : View.VISIBLE);
//        layout_landscape.setVisibility(View.GONE);
        binding.layoutPortrait.setVisibility(View.VISIBLE);
    }

    @Override
    public void initViewObservable() {
        viewModel.isClickFullScreen.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(isFullscreen){
                    exitFullscreen();
                }else {
                    enterFullscreen();
                }

                isFullscreen = !isFullscreen;
            }
        });

        viewModel.isScreenClick.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if(binding.ivFullscreenPortrait.getVisibility() == View.VISIBLE){
                    binding.ivFullscreenPortrait.setVisibility(View.GONE);
                }else {
                    binding.ivFullscreenPortrait.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
