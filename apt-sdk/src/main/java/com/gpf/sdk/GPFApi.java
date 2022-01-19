package com.gpf.sdk;

import android.app.Activity;
import android.util.Log;

import java.lang.reflect.Method;

public class GPFApi {

    public static void bindLayout(Activity activity) {
        Class clazz = activity.getClass();
        try {
            Log.e("GPF", clazz.getName());
            Class bindLayoutClass = Class.forName(clazz.getName() + "_ViewBindLayout");
            Method method = bindLayoutClass.getMethod("setContentView");
            method.invoke(bindLayoutClass.newInstance());
            Log.e("GPF", "方法执行成功");
        } catch (Exception e) {
            Log.e("GPF", "方法执行失败 [ " + e.getMessage() + " ]");
            e.printStackTrace();
        }
    }

}
