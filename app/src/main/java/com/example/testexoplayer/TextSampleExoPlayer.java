package com.example.testexoplayer;

import android.net.Uri;
import android.os.Bundle;

import com.example.testexoplayer.data.ConstantData;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.LoopingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.MergingMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import static com.google.android.exoplayer2.Player.REPEAT_MODE_ALL;
import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

public class TextSampleExoPlayer extends BaseActivity {

    SimpleExoPlayer player;
    PlayerView playView;
    MediaSource mediaSource ,mediaSource2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setAllowScreenRoate(false);
        super.onCreate(savedInstanceState);
//        setContentView();
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_test_simple_exo_player;
    }

    @Override
    protected void initView() {
        playView = findViewById(R.id.exo_play);
        playView.setResizeMode(RESIZE_MODE_FILL);
    }

    @Override
    protected void initData() {
//      player = ExoPlayerFactory.newSimpleInstance(new DefaultRenderersFactory(this),new DefaultTrackSelector(), new DefaultLoadControl());
        player = ExoPlayerFactory.newSimpleInstance(this);


        playView.setPlayer(player);

        mediaSource = ConstantData.buildMediaSource(Uri.parse(ConstantData.MP4url),this);
        mediaSource2 = ConstantData.buildOkHttpMediaSource(Uri.parse(ConstantData.MP4url),this);


        // Plays the first video twice.
        LoopingMediaSource firstSourceTwice = new LoopingMediaSource(mediaSource, 1);


        ConcatenatingMediaSource concatenatedSource = new ConcatenatingMediaSource(firstSourceTwice, mediaSource2);
        MergingMediaSource mergingMediaSource = new MergingMediaSource(mediaSource,mediaSource2);

        player.prepare(mergingMediaSource);
        player.seekTo(0);
        player.setRepeatMode(REPEAT_MODE_ALL);
        player.setPlayWhenReady(true);
    }
}
