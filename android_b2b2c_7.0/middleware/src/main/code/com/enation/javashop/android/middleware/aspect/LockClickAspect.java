package com.enation.javashop.android.middleware.aspect;

import com.enation.javashop.android.lib.utils.ExtendMethodsKt;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * 锁定点击事件只响应一次
 */
@Aspect
public class LockClickAspect {

        private final int SPACE_TIME = 500;

        private long  last_time = -1;

        @Pointcut("execution(@LockClick * *(..))")//方法切入点
        public void methodAnnotated() {
        }

        @Around("methodAnnotated()")//在连接点进行方法替换
        public void aroundJoinPoint(ProceedingJoinPoint joinPoint) throws Throwable {
            ExtendMethodsKt.errorLog("LOG",joinPoint.toString());
            if (last_time == -1){
                joinPoint.proceed();
                last_time = System.currentTimeMillis();
            }else if(System.currentTimeMillis() - last_time >SPACE_TIME){
                joinPoint.proceed();
                last_time = System.currentTimeMillis();
            }
        }

}
