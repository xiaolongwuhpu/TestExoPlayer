package com.example.testexoplayer.adapter;

import android.text.TextUtils;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.testexoplayer.R;

import java.util.ArrayList;
import java.util.List;

public class MeAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private List<TextView> tvs = new ArrayList<>();
    public  int selectPosition=0;
    public MeAdapter(@LayoutRes int layoutResId, @Nullable List<String> data,int position) {
        super(layoutResId, data);
        selectPosition = position;
    }

    public MeAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }


    @Override
    protected void convert(BaseViewHolder viewHolder, String item) {
        try {
            if(!TextUtils.isEmpty(item) && item.contains(",")){
                TextView textView = viewHolder.getView(R.id.item_tv_name);
                textView.setText(item.substring(0,item.indexOf(",")));
                //将左侧item中的TextView添加到集合中
//                tvs.add(textView);
                //设置进入页面之后,左边列表的初始状态
//                if (tvs != null && getData() != null && tvs.size() == getData().size()) {
//                    selectItem(selectPosition);
//                }
                if(selectPosition==viewHolder.getAdapterPosition()){
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.color_holo_red));
                }else{
                    textView.setTextColor(ContextCompat.getColor(mContext, R.color.white));
                }
                viewHolder.getView(R.id.item_tv_name).setSelected(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        //赋值
    }

    public void selectItem(int position){
        selectPosition = position;
        notifyDataSetChanged();
    }

}
