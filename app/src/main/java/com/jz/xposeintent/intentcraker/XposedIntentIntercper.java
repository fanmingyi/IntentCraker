package com.jz.xposeintent.intentcraker;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresPermission;
import android.util.Log;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class XposedIntentIntercper implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        XposedHelpers.findAndHookMethod(Activity.class, "startActivityForResult", Intent.class, int.class, Bundle.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                /**
                 * 意图拦截器
                 */
                Intent intent = ((Intent) param.args[0]);

                StringBuilder sb = new StringBuilder();
                sb.append("包名:" + ((Activity) param.thisObject).getPackageName());
                sb.append("\n");
                sb.append("当前跳转到的activity:" + param.thisObject.getClass().getName());
                sb.append("\n");
                sb.append("action:" + intent.getAction());
                sb.append("\n");
                sb.append("scheme:" + intent.getScheme());
                sb.append("\n");
                sb.append("跳转到:" + intent.getComponent().getClassName());
                sb.append("\n");
                sb.append("key-value:\n");
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    for (String key : bundle.keySet()) {
                        sb.append(" key: " + key + " value: " + bundle.get(key));
                        sb.append("\n");
                    }
                }


                String s = sb.toString();
                Log.e("意图拦截", s);
                XposedBridge.log(s);
            }

            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);


            }
        });
    }
}
