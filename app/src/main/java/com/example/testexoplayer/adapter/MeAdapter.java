package com.example.testexoplayer.adapter;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.testexoplayer.R;

import java.util.List;

public class MeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MeAdapter(@LayoutRes int layoutResId, @Nullable List<String> data) {
        super(layoutResId, data);
    }

    public MeAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        //赋值
        viewHolder.setText(R.id.item_tv_name,item.substring(0,item.indexOf(",")));
    }
}
