package com.enation.javashop.android.lib.core.framework;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import java.lang.reflect.Method;

/**
 * @author  LDD
 * @Data   2017/12/26 上午8:38
 * @From   com.enation.javashop.android.lib.core.framework
 * @Note   Instrumentation 的自定义Hook
 */
public class JavaShopInstrumentationHook extends Instrumentation {

    /**
     * @Name  oldInstrumentation
     * @Type  android.app.Instrumentation
     * @Note  系统原始Instrumentation
     */
    public Instrumentation oldInstrumentation;

    /**
     * @Name  EXEC_START_ACTIVITY
     * @Type  java.lang.String
     * @Note  execStartActivity方法名 反射时使用
     */
    public static final String EXEC_START_ACTIVITY = "execStartActivity";

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework JavaShopInstrumentationHook
     * @Data   2017/12/26 上午8:43
     * @Note   构造方法 传入原始 Instrumentation
     * @param  oldInstrumentation 原始 Instrumentation
     */
    public JavaShopInstrumentationHook(Instrumentation oldInstrumentation) {
        this.oldInstrumentation = oldInstrumentation;
    }

    /**
     * @author  LDD
     * @From   com.enation.javashop.android.lib.core.framework
     * @Data   2017/12/26 上午8:42
     * @Note   这个方法是由于原始方法里面的Instrumentation有execStartActivity方法来定的
     */
    public ActivityResult execStartActivity(Context who, IBinder contextThread, IBinder token, Activity target,
                                            Intent intent, int requestCode, Bundle options) {
        Method execStartActivity = null;
        //由于这个方法是隐藏的，所以需要反射来调用，先找到这方法
        try {
            execStartActivity = Instrumentation.class.getDeclaredMethod(
                    EXEC_START_ACTIVITY,
                    Context.class, IBinder.class, IBinder.class, Activity.class,
                    Intent.class, int.class, Bundle.class);
            //因为该属性为私有 设置权限
            execStartActivity.setAccessible(true);
            return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                    contextThread, token, target, intent, requestCode, options);
        } catch (Exception e) {

            try {
                return (ActivityResult) execStartActivity.invoke(oldInstrumentation, who,
                        contextThread, token, target, Framework.Companion.getClassNotFoundInterceptor().call(target,intent), requestCode, options);
            } catch (Exception e1) {
                Log.e("StartActivityError",e1.getMessage());
                return null;
            }
        }
    }
}
