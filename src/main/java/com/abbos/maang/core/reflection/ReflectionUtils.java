package com.abbos.maang.core.reflection;

import com.abbos.maang.annotation.Tag;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.function.Predicate;

/**
 * @author Aliabbos Ashurov
 * @since 2025-06-07
 */
@Tag({ "Reflection", "Custom" })
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ReflectionUtils {

    public static Field findField(Class<?> targetClass, String fieldName) {
        return FieldFinder.findField(targetClass, fieldName);
    }


    public static Field findField(Class<?> targetClass, String fieldName, Predicate<Field> filter) {
        return FieldFinder.findField(targetClass, fieldName, filter);
    }

    public static VarHandle findVarField(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        return FieldFinder.findVarHandle(targetClass, fieldName, fieldType);
    }

    public static VarHandle findVarField(Class<?> targetClass, String fieldName, Class<?> fieldType, Predicate<VarHandle> filter) {
        return FieldFinder.findVarHandle(targetClass, fieldName, fieldType, filter);
    }

    public static MethodHandle findMethodHandle(Class<?> targetClass, String methodName, MethodLookupKind kind,
                                                Class<?> rType,
                                                Class<?>... paramTypes) {
        return MethodHandleUtils.findMethodHandle(targetClass, methodName, kind, rType, paramTypes);
    }
}
