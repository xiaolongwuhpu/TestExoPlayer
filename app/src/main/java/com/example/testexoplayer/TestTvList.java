package com.example.testexoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.testexoplayer.adapter.MeAdapter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class TestTvList extends BaseActivity {
    MeAdapter mAdapter;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected int initLayout() {
        return R.layout.activity_test_tv_list;
    }

    @Override
    protected void initView() {

    }

    List<String> tvList;

    @Override
    protected void initData() {
        tvList = new ArrayList<String>();
        try {
            InputStream instream = getAssets().open("cctvs.txt");
            if (instream != null) {
                InputStreamReader inputreader = new InputStreamReader(instream);
                BufferedReader buffreader = new BufferedReader(inputreader);
                String line;
                while ((line = buffreader.readLine()) != null) {
                    tvList.add(line + "\n");
                }
                instream.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            initAdapter();
            initRecyclerView();
        }
    }

    private void initAdapter() {
        mAdapter = new MeAdapter(R.layout.item_tv,tvList);
        mAdapter.openLoadAnimation();
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setNewData(tvList);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Toast.makeText(TestTvList.this, "点击了第" + (position + 1) + "条条目", Toast.LENGTH_SHORT).show();
                String str = tvList.get(position);
                String url = str.substring(str.indexOf(",")+1,str.length()-1);
                print(url);
                Intent intent = new Intent(TestTvList.this,TestSimpleExoPlayer.class);
                intent.putExtra("position",position);
                intent.putExtra("list", (Serializable) tvList);
                startActivity(intent);
            }
        });

    }

    private void initRecyclerView() {
        //设置布局方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        //解决数据加载不完的问题
        mRecyclerView.setNestedScrollingEnabled(false);
        //当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        mRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        mRecyclerView.setFocusable(false);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL));
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }
    }
