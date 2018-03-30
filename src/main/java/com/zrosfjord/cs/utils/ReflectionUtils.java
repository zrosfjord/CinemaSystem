package com.zrosfjord.cs.utils;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static Method getMethod(Class<?> clazz, String methodName, Class<?> ... params) {
        Method m = null;
        try {
            m = clazz.getDeclaredMethod(methodName, params);
            m.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return m;
    }

    public static Object invokeMethod(Method m, Object o, Object ... params) {
        Object result = null;

        if(m.getReturnType() != null) {
            try {
                result = m.invoke(o, params);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }

        return result;
    }

    public static List<Field> getAnnotatedFields(Class<?> c, Class<? extends Annotation> aClass) {
        boolean targetsFields = aClass.getAnnotation(Target.class).value()[0] == ElementType.FIELD;


        if(!targetsFields)
            return null;

        return Arrays.stream(c.getDeclaredFields())
                .filter(e -> e.isAnnotationPresent(aClass))
                .map(e -> {e.setAccessible(true); return e;})
                .collect(Collectors.toList());
    }

}
