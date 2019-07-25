package com.example.testexoplayer;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;

import com.example.testexoplayer.data.ConstantData;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Format;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.source.SingleSampleMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.util.MimeTypes;

import java.io.IOException;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ONE;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

public class TestMergeMediaSouce extends BaseActivity {
    SimpleExoPlayer player;
    PlayerView playView;
    MediaSource mediaSource ,mediaSource2,localAudiomediaSource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShowTitle(false);
        setShowStatusBar(false);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_test_merge_media_souce;
    }

    @Override
    protected void initView() {
        playView = findViewById(R.id.exo_play);
        playView.setResizeMode(RESIZE_MODE_FILL);
    }

    @Override
    protected void initData() {
        player = ExoPlayerFactory.newSimpleInstance(this);

        playView.setPlayer(player);

        mediaSource = ConstantData.buildMediaSource(Uri.parse(ConstantData.wuxing_m3u8),this);
        localAudiomediaSource = ConstantData.buildMediaSource(Uri.parse(ConstantData.localAudio),this);


//        // 创建一个字幕的 MediaSource.
        Format subtitleFormat = Format.createTextSampleFormat(
                null,
                MimeTypes.TEXT_SSA,
                C.SELECTION_FLAG_DEFAULT,
                null);
        MediaSource subtitleSource =
                new SingleSampleMediaSource.Factory(dataSourceFactory)
         .createMediaSource(Uri.parse(ConstantData.localZiMu), subtitleFormat, C.TIME_UNSET);
        // 播放带有字幕的视频
        MergingMediaSource mergedSource =
//                new MergingMediaSource(mediaSource);
//                new MergingMediaSource(mediaSource, localAudiomediaSource);
                new MergingMediaSource(subtitleSource,mediaSource,localAudiomediaSource);


//        LoopingMediaSource loopMediaSource = new LoopingMediaSource(mergedSource);


        player.prepare(mergedSource);
        player.seekTo(0);
        player.setRepeatMode(REPEAT_MODE_ONE);
        player.setPlayWhenReady(true);


    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (instance.isPortrait()) {
            print("竖屏");
            // 切换成竖屏
        } else {
            // 切换成横屏
            print("横屏");
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

}
