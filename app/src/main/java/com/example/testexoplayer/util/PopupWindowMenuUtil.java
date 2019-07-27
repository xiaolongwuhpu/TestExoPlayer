package com.example.testexoplayer.util;


import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.testexoplayer.R;
import com.example.testexoplayer.adapter.MeAdapter;

import java.util.List;


public class PopupWindowMenuUtil {
    private static final String TAG = PopupWindowMenuUtil.class.getSimpleName();
    /**
     * 菜单的弹出窗口
     */
    private static PopupWindow popupWindow = null;
    static Context ctx;

    /**
     * 显示popupWindow弹出框
     */
    public static void showPopupWindows(final Context context, final View spinnerview, List<String> list, final OnListItemClickLitener mOnListItemClickLitener) {
        ctx = context;
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }
        //一个自定义的布局，作为显示的内容
        View popupWindowView = LayoutInflater.from(context).inflate(R.layout.activity_test_tv_list, null);

        /**在初始化contentView的时候，强制绘制contentView，并且马上初始化contentView的尺寸。
         * 另外一个点需要注意：popwindow_layout.xml的根Layout必须为LinearLayout；如果为RelativeLayout的话，会导致程序崩溃。*/
        popupWindowView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //用于获取单个列表项的高度【用于计算大于n个列表项的时候，列表的总高度值n * listitemView.getMeasuredHeight()】
        View listitemView = LayoutInflater.from(context).inflate(R.layout.activity_test_tv_list, null);
        listitemView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);

        //列表
        mRecyclerView = (RecyclerView) popupWindowView.findViewById(R.id.recycler);
        initAdapter(list, mOnListItemClickLitener);
        initRecyclerView();

        popupWindow = new PopupWindow(popupWindowView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);

        popupWindow.setTouchable(true);//设置可以触摸
        popupWindow.setFocusable(true);//代表可以允许获取焦点的，如果有输入框的话，可以聚焦

        //监听popWindow的隐藏时执行的操作--这个不错
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                //执行还原原始状态的操作，比如选中状态颜色高亮显示[去除],不能使用notifyDataSetInvalidated()，否则会出现popwindow显示错位的情况
                Log.e(TAG, "onDismiss");
            }
        });

        //下面两个参数是实现点击点击外面隐藏popupwindow的
        //这里设置显示PopuWindow之后在外面点击是否有效。如果为false的话，那么点击PopuWindow外面并不会关闭PopuWindow。当然这里很明显只能在Touchable下才能使用。不设置此项则下面的捕获window外touch事件就无法触发。
        popupWindow.setOutsideTouchable(true);

        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        // 我觉得这里是API的一个bug
        //方式一
        ColorDrawable dw = new ColorDrawable(0x88000000);//设置背景为透明
        popupWindow.setBackgroundDrawable(dw);

        //int xPos = - popupWindow.getWidth() / 2 + view.getWidth() / 2;//X轴的偏移值:xoff表示x轴的偏移，正值表示向右，负值表示向左；
        int xPos = 0;//X轴的偏移值:xoff表示x轴的偏移，正值表示向左，负值表示向右；
        int yPos = 0;//Y轴的偏移值相对某个控件的位置，有偏移;yoff表示相对y轴的偏移，正值是向下，负值是向上；

        popupWindow.showAtLocation(spinnerview, Gravity.LEFT,xPos, yPos);
//        popupWindow.showAsDropDown(spinnerview, xPos, yPos);
    }

    /**
     * 关闭菜单弹出框
     */
    public static void closePopupWindows() {
        if (popupWindow != null) {
            if (popupWindow.isShowing()) {
                popupWindow.dismiss();
                popupWindow = null;
            }
        }
    }

    /*=====================添加OnListItemClickLitener回调================================*/
    public interface OnListItemClickLitener {
        void onListItemClick(int position);
    }

    private OnListItemClickLitener mOnListItemClickLitener;

    public void setOnItemClickLitener(OnListItemClickLitener mOnListItemClickLitener) {
        this.mOnListItemClickLitener = mOnListItemClickLitener;
    }

    /**Android中Popwindow使用以及背景色变灰【不好用】
     * 【有问题，首先在DialogFragment中无法实现这个效果，只能在activity和fragment中调用
     * 其次，调用这个代码后，打开其他的对话框的时候灰色背景会出现突然闪现的问题】
     * https://blog.csdn.net/zz6880817/article/details/52189699
     * https://blog.csdn.net/chenbing81/article/details/52059979*/

    /**
     * 改变背景颜色
     *
     * @param bgAlpha 屏幕透明度0.0-1.0 1表示完全不透明
     */
    private static void setBackgroundAlpha(Context context, Float bgAlpha) {
        WindowManager.LayoutParams lp = ((Activity) context).getWindow().getAttributes();
        lp.alpha = bgAlpha;

        ((Activity) context).getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        ((Activity) context).getWindow().setAttributes(lp);

    }

    static RecyclerView mRecyclerView;
    static MeAdapter mAdapter;

    private static void initAdapter(List<String> tvList, OnListItemClickLitener mOnListItemClickLitener) {
        mAdapter = new MeAdapter(R.layout.item_tv, tvList);
        //开启动画效果
        mAdapter.openLoadAnimation();
        //设置动画效果
        /**
         * 渐显 ALPHAIN
         * 缩放 SCALEIN
         * 从下到上 SLIDEIN_BOTTOM
         * 从左到右 SLIDEIN_LEFT
         * 从右到左 SLIDEIN_RIGHT
         */
        mAdapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        mAdapter.setNewData(tvList);

        /** --------------------------- 点击事件 --------------------------- **/

        //设置Item点击事件
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                PopupWindowMenuUtil.closePopupWindows();//关闭列表对话框，注意会执行onDismiss()方法
                mOnListItemClickLitener.onListItemClick(position);
            }
        });
    }

    private static void initRecyclerView() {
        //设置布局方式
        mRecyclerView.setLayoutManager(new LinearLayoutManager(ctx));
        //解决数据加载不完的问题
        mRecyclerView.setNestedScrollingEnabled(false);
        //当知道Adapter内Item的改变不会影响RecyclerView宽高的时候，可以设置为true让RecyclerView避免重新计算大小
        mRecyclerView.setHasFixedSize(true);
        //解决数据加载完成后, 没有停留在顶部的问题
        mRecyclerView.setFocusable(false);
        //添加分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(ctx, DividerItemDecoration.HORIZONTAL));
        //设置适配器
        mRecyclerView.setAdapter(mAdapter);
    }
}
