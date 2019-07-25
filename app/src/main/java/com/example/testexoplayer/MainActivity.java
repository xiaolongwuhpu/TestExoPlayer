package com.example.testexoplayer;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.testexoplayer.data.ConstantData;
import com.example.testexoplayer.util.ToNewPageUtil;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

import static com.example.testexoplayer.data.ConstantData.H264_MANIFEST;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;


public class MainActivity extends BaseActivity {
    SimpleExoPlayer player;
    PlayerView playView;
    MediaSource mediaSource, mediaSource2;
    @BindView(R.id.example_exo_player)
    Button exampleExoPlayer;
    @BindView(R.id.merge)
    Button merge;
    @BindView(R.id.tv)
    Button tv;
    ConcatenatingMediaSource concatenatedSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        playView = findViewById(R.id.exo_play);
    }

    @Override
    protected void initData() {
        playView.setResizeMode(RESIZE_MODE_FILL);

//      player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),new DefaultTrackSelector(), new DefaultLoadControl());
        player = ExoPlayerFactory.newSimpleInstance(this);

        playView.setPlayer(player);

        mediaSource2 = ConstantData.buildMediaSource(Uri.parse(ConstantData.SmoothStreaming_ism01), this);
//        mediaSource2 = ConstantData.buildOkHttpMediaSource(Uri.parse(ConstantData.wuxing_m3u8), this);
//        LoopingMediaSource firstSourceTwice = new LoopingMediaSource(mediaSource, 1);
//        concatenatedSource = new ConcatenatingMediaSource(mediaSource, mediaSource2);
        player.prepare(mediaSource2);
        player.seekTo(currentPosition);
        player.addListener(listener);
    }


    private Player.DefaultEventListener listener = new Player.DefaultEventListener() {
        @Override
        public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
            // 视频播放状态
            Log.d(TAG, "playbackState = " + playbackState + " playWhenReady = " + playWhenReady);
            switch (playbackState) {
                case Player.STATE_IDLE:
                    //1 空闲
                    break;
                case Player.STATE_BUFFERING:
                    //2 缓冲中
                    break;
                case Player.STATE_READY:
                    //3 准备好
                    break;
                case Player.STATE_ENDED:
                    //4 结束
                    break;
                default:
                    break;
            }
        }

        @Override
        public void onPlayerError(ExoPlaybackException error) {
            // 报错
            switch (error.type) {
                case ExoPlaybackException.TYPE_SOURCE:
                    // 加载资源时出错
                    break;
                case ExoPlaybackException.TYPE_RENDERER:
                    // 渲染时出错
                    break;
                case ExoPlaybackException.TYPE_UNEXPECTED:
                    // 意外的错误
                    break;
            }
        }
    };


    @OnClick({R.id.example_exo_player, R.id.merge, R.id.tv})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.example_exo_player:
                ToNewPageUtil.intentToActivity(this, TestSimpleExoPlayer.class);
                break;
            case R.id.merge:
                ToNewPageUtil.intentToActivity(this, TestMergeMediaSouce.class);
                break;
            case R.id.tv:
                List<String> list = ConstantData.loadAssetData(this);
                if (list.size() > 0) {
                    Intent intent = new Intent(this, TestSimpleExoPlayer.class);
                    intent.putExtra("position", 0);
                    intent.putExtra("list", (Serializable) list);
                    startActivity(intent);
                } else {
                    showToast("数据加载失败,请重试");
                }
                break;
        }
    }

    static long currentPosition = 0;

    @Override
    protected void onResume() {
        super.onResume();
        player.setPlayWhenReady(true);
        Log.d(TAG, "RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        currentPosition = player.getCurrentPosition();
        Log.d(TAG, "PAUSE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.stop();
        player.release();
        Log.d(TAG, "DESTROY");
    }
}
