package com.lanshifu.androidnews_mvvm.ui.activity;

import android.databinding.Observable;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.lanshifu.androidnews_mvvm.R;
import com.lanshifu.androidnews_mvvm.databinding.ActivityLivePlayBinding;
import com.lanshifu.androidnews_mvvm.ui.vm.LivePlayVM;
import com.lanshifu.androidnews_mvvm.widget.MediaController;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import me.goldze.mvvmhabit.base.BaseActivity;
import me.goldze.mvvmhabit.utils.ToastUtils;

public class LivePlayActivity extends BaseActivity<ActivityLivePlayBinding, LivePlayVM> {

    private static final String TAG = "LivePlayActivity";
    private boolean isVideoPrepared = false;         //Video加载标志位，用于显示隐藏ProgreeBar
    private boolean isPause = false;         //直播暂停标志位
    private boolean isFullscreen = false;   //全屏标志位
    private String live_url;


    private PLVideoTextureView mVideoView;
    private int mRotation = 0;
    private int mDisplayAspectRatio = PLVideoTextureView.ASPECT_RATIO_FIT_PARENT; //default
    public static final String SDCARD_DIR = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DEFAULT_CACHE_DIR = SDCARD_DIR + "/PLDroidPlayer";

    private boolean isLiveStreaming = true;

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
    public void initParam() {
        live_url = getIntent().getStringExtra("url");
    }

    @Override
    public void initData() {

        initPlayer();

    }

    private void initPlayer() {
        // 1 -> hw codec enable, 0 -> disable [recommended]
        int codec = getIntent().getIntExtra("mediaCodec", AVOptions.MEDIA_CODEC_SW_DECODE);
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, codec);
        boolean cache = getIntent().getBooleanExtra("cache", false);
        if (!isLiveStreaming && cache) {
            options.setString(AVOptions.KEY_CACHE_DIR, DEFAULT_CACHE_DIR);
        }
        mVideoView = binding.plVideoTextureView;
        mVideoView.setAVOptions(options);
        mVideoView.setDebugLoggingEnabled(true);

        // You can mirror the display
        // mVideoView.setMirror(true);

        // You can also use a custom `MediaController` widget
        MediaController mediaController = new MediaController(this, !isLiveStreaming, isLiveStreaming);
        mediaController.setOnClickSpeedAdjustListener(mOnClickSpeedAdjustListener);
        mVideoView.setMediaController(mediaController);

        mVideoView.setOnInfoListener(mOnInfoListener);
        mVideoView.setOnVideoSizeChangedListener(mOnVideoSizeChangedListener);
        mVideoView.setOnBufferingUpdateListener(mOnBufferingUpdateListener);
        mVideoView.setOnCompletionListener(mOnCompletionListener);
        mVideoView.setOnErrorListener(mOnErrorListener);

        mVideoView.setVideoPath(live_url);
        mVideoView.start();
    }

    private PLMediaPlayer.OnInfoListener mOnInfoListener = new PLMediaPlayer.OnInfoListener() {
        @Override
        public boolean onInfo(PLMediaPlayer plMediaPlayer, int what, int extra) {
            Log.i(TAG, "OnInfo, what = " + what + ", extra = " + extra);
            switch (what) {
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_START:
                    break;
                case PLMediaPlayer.MEDIA_INFO_BUFFERING_END:
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START:
                    ToastUtils.showShort("First video render time: " + extra + "ms");
                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_RENDERING_START:
                    Log.i(TAG, "First audio render time: " + extra + "ms");
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_FRAME_RENDERING:
                    Log.i(TAG, "video frame rendering, ts = " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_AUDIO_FRAME_RENDERING:
                    Log.i(TAG, "audio frame rendering, ts = " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_GOP_TIME:
                    Log.i(TAG, "Gop Time: " + extra);
                    break;
                case PLMediaPlayer.MEDIA_INFO_SWITCHING_SW_DECODE:
                    Log.i(TAG, "Hardware decoding failure, switching software decoding!");
                    break;
                case PLMediaPlayer.MEDIA_INFO_METADATA:
                    Log.i(TAG, mVideoView.getMetadata().toString());
                    break;
                case PLMediaPlayer.MEDIA_INFO_VIDEO_BITRATE:
                case PLMediaPlayer.MEDIA_INFO_VIDEO_FPS:
                    Log.i(TAG, "MEDIA_INFO_VIDEO_FPS");
                    break;
                case PLMediaPlayer.MEDIA_INFO_CONNECTED:
                    Log.i(TAG, "Connected !");
                    break;
                default:
                    break;
            }
            return true;
        }
    };


    private PLMediaPlayer.OnErrorListener mOnErrorListener = new PLMediaPlayer.OnErrorListener() {
        @Override
        public boolean onError(PLMediaPlayer mp, int errorCode) {
            Log.e(TAG, "Error happened, errorCode = " + errorCode);
            switch (errorCode) {
                case PLMediaPlayer.ERROR_CODE_IO_ERROR:
                    /**
                     * SDK will do reconnecting automatically
                     */
                    ToastUtils.showShort("IO Error !");
                    return false;
                case PLMediaPlayer.ERROR_CODE_OPEN_FAILED:
                    ToastUtils.showShort("failed to open player !");
                    break;
                case PLMediaPlayer.ERROR_CODE_SEEK_FAILED:
                    ToastUtils.showShort("failed to seek !");
                    break;
                default:
                    ToastUtils.showShort("unknown error !");
                    break;
            }
            finish();
            return true;
        }
    };


    private PLMediaPlayer.OnCompletionListener mOnCompletionListener = new PLMediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(PLMediaPlayer plMediaPlayer) {
            Log.i(TAG, "Play Completed !");
            ToastUtils.showShort("Play Completed !");
            finish();
        }
    };

    private PLMediaPlayer.OnBufferingUpdateListener mOnBufferingUpdateListener = new PLMediaPlayer.OnBufferingUpdateListener() {
        @Override
        public void onBufferingUpdate(PLMediaPlayer plMediaPlayer, int precent) {
            Log.i(TAG, "onBufferingUpdate: " + precent);
        }
    };

    private PLMediaPlayer.OnVideoSizeChangedListener mOnVideoSizeChangedListener = new PLMediaPlayer.OnVideoSizeChangedListener() {
        @Override
        public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int width, int height) {
            Log.i(TAG, "onVideoSizeChanged: width = " + width + ", height = " + height);
        }
    };

    private MediaController.OnClickSpeedAdjustListener mOnClickSpeedAdjustListener = new MediaController.OnClickSpeedAdjustListener() {
        @Override
        public void onClickNormal() {
            // 0x0001/0x0001 = 2
            mVideoView.setPlaySpeed(0X00010001);
        }

        @Override
        public void onClickFaster() {
            // 0x0002/0x0001 = 2
            mVideoView.setPlaySpeed(0X00020001);
        }

        @Override
        public void onClickSlower() {
            // 0x0001/0x0002 = 0.5
            mVideoView.setPlaySpeed(0X00010002);
        }
    };


    @Override
    protected void onResume() {
        super.onResume();

        mVideoView.start();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mVideoView.stopPlayback();
    }


    @Override
    public void initViewObservable() {
        viewModel.isClickFullScreen.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                mDisplayAspectRatio = (mDisplayAspectRatio + 1) % 5;
                mVideoView.setDisplayAspectRatio(mDisplayAspectRatio);
                isFullscreen = !isFullscreen;

                switch (mVideoView.getDisplayAspectRatio()) {
                    case PLVideoTextureView.ASPECT_RATIO_ORIGIN:
                        ToastUtils.showShort("Origin mode");
                        break;
                    case PLVideoTextureView.ASPECT_RATIO_FIT_PARENT:
                        ToastUtils.showShort("Fit parent !");
                        break;
                    case PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT:
                        ToastUtils.showShort("Paved parent !");
                        break;
                    case PLVideoTextureView.ASPECT_RATIO_16_9:
                        ToastUtils.showShort("16 : 9 !");
                        break;
                    case PLVideoTextureView.ASPECT_RATIO_4_3:
                        ToastUtils.showShort("4 : 3 !");
                        break;
                    default:
                        break;
                }
            }
        });

        viewModel.isScreenClick.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable sender, int propertyId) {
                if (binding.layoutPortrait.getVisibility() == View.GONE) {
                    binding.layoutPortrait.setVisibility(View.VISIBLE);
                } else {
                    binding.layoutPortrait.setVisibility(View.GONE);
                }

            }
        });

        viewModel.isRotateClick.addOnPropertyChangedCallback(new Observable.OnPropertyChangedCallback() {
            @Override
            public void onPropertyChanged(Observable observable, int i) {
                mRotation = (mRotation + 90) % 360;
                mVideoView.setDisplayOrientation(mRotation);
                ToastUtils.showShort("rotate " + mRotation);
            }
        });
    }


}
