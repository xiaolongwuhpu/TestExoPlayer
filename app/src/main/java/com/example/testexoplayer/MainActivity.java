package com.example.testexoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.testexoplayer.data.ConstantData;
import com.example.testexoplayer.mediacodec.TestMediaCodec;
import com.example.testexoplayer.util.ToNewPageUtil;

import java.io.Serializable;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;


public class MainActivity extends BaseActivity {

    @BindView(R.id.merge)
    Button merge;
    @BindView(R.id.tv)
    Button tv;

    @BindView(R.id.speed)
    Button speed;
    @BindView(R.id.media_codec)
    Button mediaCodec;
    @BindView(R.id.jap)
    Button Jap;
    @BindView(R.id.other)
    Button other;

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
    }

    @Override
    protected void initData() {
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "RESUME");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "PAUSE");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.media_codec, R.id.merge, R.id.speed, R.id.tv,R.id.jap,R.id.other})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.media_codec:
                ToNewPageUtil.intentToActivity(this, TestMediaCodec.class);
                break;
            case R.id.merge:
                ToNewPageUtil.intentToActivity(this, TestMergeMediaSouce.class);
                break;
            case R.id.tv:
                intentWithMoves("cctvs.txt");
                break;
            case R.id.jap:
                intentWithMoves("new18.txt");
                break;
            case R.id.other:
                intentWithMoves("others.txt");
                break;
            case R.id.speed:
                ToNewPageUtil.intentToActivity(this, TestPlaybackActivity.class);
                break;
        }
    }

    private void intentWithMoves(String move) {
        List<String> list = ConstantData.loadAssetData(this, move);
        if (list.size() > 0) {
            Intent intent = new Intent(this, TestSimpleExoPlayer.class);
            intent.putExtra("position", 0);
            intent.putExtra("list", (Serializable) list);
            startActivity(intent);
        } else {
            showToast("数据加载失败,请重试");
        }
    }
}
