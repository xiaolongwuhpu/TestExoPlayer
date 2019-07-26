package com.example.testexoplayer;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.example.testexoplayer.data.ConstantData;
import com.example.testexoplayer.util.PopupWindowMenuUtil;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.ui.PlayerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import static com.google.android.exoplayer2.ui.AspectRatioFrameLayout.RESIZE_MODE_FILL;

public class TestSimpleExoPlayer extends BaseActivity {
    SimpleExoPlayer player;
    PlayerView playView;
    MediaSource mediaSource;

    String url = ConstantData.MP4url;
    List<String> list;
    int position;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setShowTitle(false);
        setShowStatusBar(false);
        int temPosition = getIntent().getIntExtra("position", 0);
        List<String> tempList = getIntent().getStringArrayListExtra("list");
        if (tempList != null && tempList.size() > temPosition) {
            list = tempList;
            position = temPosition;
            url = tempList.get(position);
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

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
        mediaSource = ConstantData.buildMediaSource(Uri.parse(url), this);
        player = ExoPlayerFactory.newSimpleInstance(this);
        mediaPlayerConfiguration();
        //触摸播放界面事件
        playView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, @NonNull MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_UP:
                        print(event.toString());
                        if (showing) {
                            dissmissMenu();
                            playView.hideController();
                        } else {
                            showMenu();
                            playView.showController();
                        }
                        showing = !showing;
                        break;
                }
                return true;
            }
        });
    }

    boolean showing = false;

    private void mediaPlayerConfiguration() {
//        DynamicConcatenatingMediaSource dynamicConcatenatingMediaSource = new DynamicConcatenatingMediaSource();
        Uri uri = getUri(position);
        mediaSource = ConstantData.buildMediaSource(uri, this);
        playView.setPlayer(player);
        player.prepare(mediaSource);
        player.setPlayWhenReady(true);
    }

    @NotNull
    private Uri getUri(int i) {
        String str = list.get(i);
        String url = str.substring(str.indexOf(",") + 1, str.length() - 1);
        return Uri.parse(url);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }

    void dissmissMenu() {
        PopupWindowMenuUtil.closePopupWindows();
    }

    private void showMenu() {
        PopupWindowMenuUtil.showPopupWindows(this, playView, list, new PopupWindowMenuUtil.OnListItemClickLitener() {
            @Override
            public void onListItemClick(int mPosition) {
                if (mPosition != -1) {
                    position = mPosition;
                    mediaPlayerConfiguration();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT://竖屏
                Log.i(TAG, "竖屏");
                break;
            case Configuration.ORIENTATION_LANDSCAPE://横屏
                Log.i(TAG, "横屏");
            default:
                break;
        }
    }
}

