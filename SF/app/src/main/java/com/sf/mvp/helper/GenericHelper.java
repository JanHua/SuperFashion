package com.sf.mvp.helper;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 属性帮助类
 */
public class GenericHelper {

    /**
     * 通过Java反射机制获取一个类中第一个泛型
     *
     * @param tClass
     * @param <T>
     * @return
     */
    public static <T> Class<T> getViewClass(Class<?> tClass) {
        Type type = tClass.getGenericSuperclass();
        if (type == null || !(type instanceof ParameterizedType)) return null;
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] types = parameterizedType.getActualTypeArguments();
        if (types == null || types.length == 0) return null;
        return (Class<T>) types[0];
    }
}
