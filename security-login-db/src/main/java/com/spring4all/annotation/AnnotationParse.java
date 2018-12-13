package com.spring4all.annotation;

import java.lang.reflect.Method;

public class AnnotationParse {
    /***
     * 解析权限注解
     * @return 返回注解的authorities值
     * @throws Exception
     */
    public static String[] privilegeParse(Method method) throws Exception {
        //获取该方法
        if(method.isAnnotationPresent(Permission.class)){
            Permission annotation = method.getAnnotation(Permission.class);
            return annotation.authorities();
        }
        return null;
    }
}