package com.example.testexoplayer.mediacodec;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import com.example.testexoplayer.BaseActivity;
import com.example.testexoplayer.R;
import com.example.testexoplayer.data.ConstantData;

import butterknife.BindView;
import butterknife.OnClick;

public class TestMediaCodec extends BaseActivity {

    @BindView(R.id.surface_view)
    SurfaceView surfaceView;
    @BindView(R.id.tv_play)
    TextView tvPlay;
    @BindView(R.id.tv_pause)
    TextView tvPause;

    private String filePath;
    private MediaCodecPlayer player;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private IPlayStateListener playStateListener = new IPlayStateListener() {
        @Override
        public void videoAspect(int width, int height, float time) {
            Log.d(TAG, "width="+width+" , height="+height+" , tiem="+time);
        }
    };

    private SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            player = new MediaCodecPlayer(holder.getSurface(), filePath);
            player.setPlayStateListener(playStateListener);
            player.play();
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (player != null) {
                player.destroy();
            }
        }
    };


    @Override
    protected int initLayout() {
        return R.layout.activity_media_codec;
    }

    @Override
    protected void initView() {
        filePath = ConstantData.MP4url;
        surfaceView.getHolder().addCallback(callback);
    }

    @Override
    protected void initData() {
    }

    @OnClick({R.id.tv_play, R.id.tv_pause})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_play:
                if (player != null) {
                    player.continuePlay();
                }
                break;
            case R.id.tv_pause:
                if (player != null) {
                    player.pause();
                }
                break;
        }
    }
}
