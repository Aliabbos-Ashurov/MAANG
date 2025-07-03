package com.abbos.maang.core.reflection;

import com.google.errorprone.annotations.Immutable;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.lang.invoke.MethodHandle;

/**
 * Utility class for method handle operations.
 *
 * @author Aliabbos Ashurov
 * @since 2025-06-08
 */
@Immutable
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodHandleUtils {

    /**
     * Finds a method handle for the specified method or constructor.
     * <p>
     * For {@link MethodLookupKind#CONSTRUCTOR}, the {@code methodName} parameter is ignored, as constructors
     * are identified by their parameter types.
     * </p>
     *
     * @param targetClass    the class containing the method or constructor
     * @param methodName     the name of the method (ignored for constructors)
     * @param lookupKind     the lookup kind (e.g., VIRTUAL, STATIC, CONSTRUCTOR, SPECIAL)
     * @param returnType     the method's return type (use {@code void.class} for constructors)
     * @param parameterTypes the method's parameter types
     * @return the method handle
     * @throws ReflectionException if the method or constructor cannot be found or accessed
     */
    public static MethodHandle findMethodHandle(Class<?> targetClass, String methodName, MethodLookupKind lookupKind,
                                                Class<?> returnType,
                                                Class<?>... parameterTypes) {
        return MethodHandleFinder.create(targetClass)
                .withMethodName(lookupKind == MethodLookupKind.CONSTRUCTOR ? "" : methodName)
                .withLookupKind(lookupKind)
                .withReturnType(returnType)
                .withParameterTypes(parameterTypes == null ? new Class<?>[0] : parameterTypes)
                .find();

    }
}
