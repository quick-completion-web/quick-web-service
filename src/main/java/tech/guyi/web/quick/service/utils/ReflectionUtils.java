package tech.guyi.web.quick.service.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ReflectionUtils {

    public static String getUpFieldName(String fieldName){
        return String.valueOf(fieldName.charAt(0)).toUpperCase() + fieldName.substring(1);
    }

    public static List<String> getFieldNames(Class<?> classes){
        return Arrays.stream(classes.getMethods())
                .filter(method -> method.getName().startsWith("get") || method.getName().startsWith("is"))
                .map(method -> {
                    String name = method.getName();
                    name = name.startsWith("get") ? name.substring(3) : name.startsWith("is") ? name.substring(2) : name;
                    name = String.valueOf(name.charAt(0)).toLowerCase() + name.substring(1);
                    return name;
                })
                .collect(Collectors.toList());
    }

    public static Class<?> getFieldType(String fieldName, Class<?> classes){
        return Optional.ofNullable(getFieldGetMethod(fieldName,classes))
                .map(Method::getReturnType)
                .orElse(null);
    }

    public static Method getFieldGetMethod(String fieldName,Class<?> classes){
        String methodName = "get" + getUpFieldName(fieldName);
        Method method = null;
        try {
            method = classes.getMethod(methodName);
        } catch (NoSuchMethodException e) {
            methodName = "is" + getUpFieldName(fieldName);
            try {
                method = classes.getMethod(methodName);
            } catch (NoSuchMethodException ex) {
                ex.printStackTrace();
            }
        }
        return method;
    }

    public static Object getFieldValue(String fieldName,Object entity){
        Method method = getFieldGetMethod(fieldName,entity.getClass());
        if (method != null){
            try {
                return method.invoke(entity);
            } catch (IllegalAccessException | InvocationTargetException e) {}
        }

        try {
            Field field = entity.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(entity);
        } catch (NoSuchFieldException | IllegalAccessException exc) {}

        return null;
    }

    public static boolean hasAnnotationInField(Class<? extends Annotation> annotationClass,Class<?> classes,String fieldName){
        return getAnnotationInField(annotationClass,classes,fieldName).isPresent();
    }

    public static <A extends Annotation> Optional<A> getAnnotationInField(Class<A> annotationClass, Class<?> classes, String fieldName){
        Field field = null;
        try {
            field = classes.getField(fieldName);
        } catch (NoSuchFieldException e) {
            try {
                field = classes.getDeclaredField(fieldName);
            } catch (NoSuchFieldException ex) {}
        }

        if (field != null){
            A annotation = field.getAnnotation(annotationClass);
            if (annotation != null){
                return Optional.of(annotation);
            }
        }

        Method method = getFieldGetMethod(fieldName,classes);
        if (method != null){
            A annotation = method.getAnnotation(annotationClass);
            if (annotation != null){
                return Optional.of(annotation);
            }
        }

        return Optional.empty();
    }



}
