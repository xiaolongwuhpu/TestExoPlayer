package com.example.testexoplayer;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.testexoplayer.data.ConstantData;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

public class TestPlaybackActivity extends BaseActivity {
    SimpleExoPlayer player;
    PlayerView playView;
    MediaSource mediaSource;
    @BindView(R.id.exo_play)
    PlayerView exoPlay;
    @BindView(R.id.normal)
    Button normal;
    @BindView(R.id.fast)
    Button fast;
    float speed = 1.0f;
    @BindView(R.id.slow)
    Button slow;

    @Override
    protected int initLayout() {
        return R.layout.activity_test_playback;
    }

    @Override
    protected void initView() {
        playView = findViewById(R.id.exo_play);
    }

    @Override
    protected void initData() {
        playView.setResizeMode(RESIZE_MODE_FILL);
        player = ExoPlayerFactory.newSimpleInstance(this);

        playView.setPlayer(player);

        mediaSource = ConstantData.buildMediaSource(Uri.parse(ConstantData.MP4url2), this);
//        LoopingMediaSource firstSourceTwice = new LoopingMediaSource(mediaSource);
        player.setRepeatMode(player.REPEAT_MODE_ONE);
        player.prepare(mediaSource);
        player.seekTo(currentPosition);
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

    @OnClick({R.id.normal, R.id.slow, R.id.fast})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.normal:
                speed = 1.0f;
                PlaybackParameters playbackParameters1 = new PlaybackParameters(speed);
                player.setPlaybackParameters(playbackParameters1);
                break;
            case R.id.fast:
                speed += 0.5;
                PlaybackParameters playbackParameters2 = new PlaybackParameters(speed);
                player.setPlaybackParameters(playbackParameters2);
                break;
            case R.id.slow:
                speed -= 0.1;
                if (speed <= 0) {
                    speed = 1.0f;
                }
                PlaybackParameters playbackParameters3 = new PlaybackParameters(speed);
                player.setPlaybackParameters(playbackParameters3);
                break;

        }
        normal.setText(speed + "倍速");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

    @OnClick(R.id.slow)
    public void onViewClicked() {
    }
}

