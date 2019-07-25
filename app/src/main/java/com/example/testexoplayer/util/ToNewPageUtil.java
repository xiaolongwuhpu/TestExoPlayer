package com.example.testexoplayer.util;

import android.app.Activity;
import android.content.Intent;

public class ToNewPageUtil {

    //跳转到相应的界面
    public static void intentToActivity(Activity activity, Class cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

}
