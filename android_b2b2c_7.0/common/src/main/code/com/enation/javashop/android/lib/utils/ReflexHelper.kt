package com.enation.javashop.android.lib.utils

/**
 * @author LDD
 * @Date   2018/4/18 上午11:13
 * @From   com.enation.javashop.android.lib.utils
 * @Note   反射工具类
 */
class ReflexHelper private constructor(clsName :String){
    
    /**
     * @author LDD
     * @Date   2018/4/18 下午3:47
     * @From   ReflexHelper
     * @Note   用来避免在Kotlin中使用反射 Kotlin基础类型无法对应Java基础类型
     */
    data class ReflexFieldShell<T>(var value: T)

    /**
     * @Name  cls
     * @Type  Class<*>
     * @Note  查找到的Class对象
     */
    private val cls :Class<*> = Class.forName(clsName)

    /**
     * @Name  instance
     * @Type  Any
     * @Note  反射实例化出来的对象
     */
    private lateinit var instance :Any

    /**
     * @author LDD
     * @Date   2018/4/19 下午2:29
     * @From   ReflexHelper
     * @Note   伴生对象用来静态build
     */
    companion object {

        /**
         * @author LDD
         * @From   ReflexHelper
         * @Date   2018/4/19 下午2:30
         * @Note   静态构建
         * @param  clsName 反射类对应全包名
         */
        fun build(clsName: String):ReflexHelper{
            return ReflexHelper(clsName)
        }
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:30
     * @Note   无参实例化
     */
    fun newInstance() :ReflexHelper{
        instance = cls.newInstance()
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:32
     * @Note   1参反射实例化
     * @param  arg0 参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     */
    fun newInstance(arg0 :Any,isPrivate :Boolean = false) :ReflexHelper{
        val constructor = cls.getConstructor(arg0::class.java)
        constructor.isAccessible = isPrivate
        instance =  constructor.newInstance(arg0)
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:32
     * @Note   2参反射实例化
     * @param  arg0 参数
     * @param  arg1 参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     */
    fun newInstance(arg0 :Any ,arg1 :Any,isPrivate :Boolean = false) :ReflexHelper{
        val constructor = cls.getConstructor(arg0::class.java,arg1::class.java)
        constructor.isAccessible = isPrivate
        instance = constructor.newInstance(arg0,arg1)
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:32
     * @Note   3参反射实例化
     * @param  arg0 参数
     * @param  arg1 参数
     * @param  arg2 参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     */
    fun newInstance(arg0 :Any ,arg1 :Any ,arg2 :Any,isPrivate :Boolean = false) :ReflexHelper{
        val constructor = cls.getConstructor(arg0::class.java,arg1::class.java,arg2::class.java)
        constructor.isAccessible = isPrivate
        instance = constructor.newInstance(arg0,arg1,arg2)
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:32
     * @Note   4参反射实例化
     * @param  arg0 参数
     * @param  arg1 参数
     * @param  arg2 参数
     * @param  arg3 参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     */
    fun newInstance(arg0 :Any ,arg1 :Any ,arg2 :Any ,arg3 :Any ,isPrivate :Boolean = false) :ReflexHelper{
        val constructor = cls.getConstructor(arg0::class.java,arg1::class.java,arg2::class.java,arg3::class.java)
        constructor.isAccessible = isPrivate
        instance = constructor.newInstance(arg0,arg1,arg2,arg3)
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:32
     * @Note   4参反射实例化
     * @param  arg0 参数
     * @param  arg1 参数
     * @param  arg2 参数
     * @param  arg3 参数
     * @param  arg4 参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     */
    fun newInstance( arg0 :Any ,arg1 :Any ,arg2 :Any ,arg3 :Any ,arg4 :Any ,isPrivate :Boolean = false) :ReflexHelper{
        val constructor = cls.getConstructor(arg0::class.java,arg1::class.java,arg2::class.java,arg3::class.java,arg4::class.java)
        constructor.isAccessible = isPrivate
        instance = constructor.newInstance(arg0,arg1,arg2,arg3,arg4)
        return this
    }


    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:37
     * @Note   无参数方法反射调用
     * @param  methodName 方法名
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     * @param  call      如果存在返回参数 实现该回调
     */
    fun callMethod(methodName :String ,call :((Any) ->Unit)? = null ,isPrivate :Boolean = false):ReflexHelper{
        val method = cls.getMethod(methodName)
        method.isAccessible = isPrivate
        if (call == null){
            method.invoke(instance)
        }else{
            call.invoke(method.invoke(instance))
        }
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:37
     * @Note   无参数方法反射调用
     * @param  methodName 方法名
     * @param  arg0  参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     * @param  call      如果存在返回参数 实现该回调
     */
    fun callMethod(methodName :String ,arg0: Any ,call :((Any) ->Unit)? = null ,isPrivate :Boolean = false):ReflexHelper{
        val method = cls.getMethod(methodName,arg0::class.java)
        method.isAccessible = isPrivate
        if (call == null){
            method.invoke(instance,arg0)
        }else{
            call.invoke(method.invoke(instance,arg0))
        }
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:37
     * @Note   无参数方法反射调用
     * @param  arg0  参数
     * @param  arg1  参数
     * @param  methodName 方法名
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     * @param  call      如果存在返回参数 实现该回调
     */
    fun callMethod(methodName :String ,arg0: Any ,arg1: Any ,call :((Any) ->Unit)? = null,isPrivate :Boolean = false):ReflexHelper{
        val method = cls.getMethod(methodName,arg0::class.java,arg1::class.java)
        method.isAccessible = isPrivate
        if (call == null){
            method.invoke(instance,arg0,arg1)
        }else{
            call.invoke(method.invoke(instance,arg0,arg1))
        }
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:37
     * @Note   无参数方法反射调用
     * @param  methodName 方法名
     * @param  arg0  参数
     * @param  arg1  参数
     * @param  arg2  参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     * @param  call      如果存在返回参数 实现该回调
     */
    fun callMethod(methodName :String ,arg0: Any ,arg1: Any ,arg2: Any ,call :((Any) ->Unit)? = null,isPrivate :Boolean = false):ReflexHelper{
        val method = cls.getMethod(methodName,arg0::class.java,arg1::class.java,arg2::class.java)
        method.isAccessible = isPrivate
        if (call == null){
            method.invoke(instance,arg0,arg1,arg2)
        }else{
            call.invoke(method.invoke(instance,arg0,arg1,arg2))
        }
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:37
     * @Note   无参数方法反射调用
     * @param  methodName 方法名
     * @param  arg0  参数
     * @param  arg1  参数
     * @param  arg2  参数
     * @param  arg3  参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     * @param  call      如果存在返回参数 实现该回调
     */
    fun callMethod(methodName :String ,arg0: Any ,arg1: Any ,arg2: Any ,arg3: Any ,call :((Any) ->Unit)? = null,isPrivate :Boolean = false):ReflexHelper{
        val method = cls.getMethod(methodName,arg0::class.java,arg1::class.java,arg2::class.java,arg3::class.java)
        method.isAccessible = isPrivate
        if (call == null){
            method.invoke(instance,arg0,arg1,arg2,arg3)
        }else{
            call.invoke(method.invoke(instance,arg0,arg1,arg2,arg3))
        }
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:37
     * @Note   无参数方法反射调用
     * @param  methodName 方法名
     * @param  arg0  参数
     * @param  arg1  参数
     * @param  arg2  参数
     * @param  arg3  参数
     * @param  arg4  参数
     * @param  isPrivate 是否开启私有方法访问 违反封装规范
     * @param  call      如果存在返回参数 实现该回调
     */
    fun callMethod(methodName :String ,arg0: Any ,arg1: Any ,arg2: Any ,arg3: Any ,arg4: Any ,call :((Any) ->Unit)? = null,isPrivate :Boolean = false):ReflexHelper{
        val method = cls.getMethod(methodName,arg0::class.java,arg1::class.java,arg2::class.java,arg3::class.java,arg4::class.java)
        method.isAccessible = isPrivate
        if (call == null){
            method.invoke(instance,arg0,arg1,arg2,arg3,arg4)
        }else{
            call.invoke(method.invoke(instance,arg0,arg1,arg2,arg3,arg4))
        }
        return this
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:41
     * @Note   获取反射成功的Class
     */
    fun <ValueType> getObjectClass() :Class<ValueType>{
        return cls.to()
    }

    /**
     * @author LDD
     * @From   ReflexHelper
     * @Date   2018/4/19 下午2:42
     * @Note   获取反射成功的对象
     */
    fun <ValueType> getInstance() :ValueType{
        return instance.to()
    }

}