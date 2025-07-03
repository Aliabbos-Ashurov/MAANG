package com.abbos.maang.core.reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.Objects;

/**
 * Fluent builder for finding method handles using Java's MethodHandles API.
 *
 * @author Aliabbos Ashurov
 * @since 2025-06-08
 */
final class MethodHandleFinder {
    private final Class<?> targetClass;
    private String methodName;
    private MethodLookupKind lookupKind = MethodLookupKind.VIRTUAL;
    private Class<?> returnType;
    private Class<?>[] parameterTypes = new Class<?>[0];

    private MethodHandleFinder(Class<?> targetClass) {
        this.targetClass = Objects.requireNonNull(targetClass, Messages.Reflection.TARGET_CLASS_NULL);
    }

    /**
     * Creates a new {@code MethodHandleFinder} for the specified class.
     *
     * @param targetClass the class containing the method or constructor
     * @return a new {@code MethodHandleFinder} instance
     * @throws NullPointerException if {@code targetClass} is null
     */
    public static MethodHandleFinder create(Class<?> targetClass) {
        return new MethodHandleFinder(targetClass);
    }

    /**
     * Sets the method name for the lookup.
     * <p>
     * Ignored for {@link MethodLookupKind#CONSTRUCTOR} lookups, as constructors do not have names.
     * </p>
     *
     * @param methodName the method name
     * @return this builder
     */
    public MethodHandleFinder withMethodName(String methodName) {
        this.methodName = methodName;
        return this;
    }

    /**
     * Sets the lookup kind (e.g., VIRTUAL, STATIC).
     *
     * @param lookupKind the lookup kind
     * @return this builder
     */
    public MethodHandleFinder withLookupKind(MethodLookupKind lookupKind) {
        this.lookupKind = lookupKind;
        return this;
    }

    /**
     * Sets the return type of the method.
     *
     * @param returnType the return type (use {@code void.class} for constructors)
     * @return this builder
     */
    public MethodHandleFinder withReturnType(Class<?> returnType) {
        this.returnType = returnType;
        return this;
    }

    /**
     * Sets the parameter types of the method.
     *
     * @param parameterTypes the parameter types
     * @return this builder
     */
    public MethodHandleFinder withParameterTypes(Class<?>... parameterTypes) {
        this.parameterTypes = parameterTypes;
        return this;
    }

    /**
     * Finds the method handle based on the configured parameters.
     *
     * @return the method handle
     * @throws ReflectionException  if the method or constructor cannot be found or accessed
     * @throws NullPointerException if required fields (methodName, returnType, lookupKind) are null for non-constructor lookups
     */
    public MethodHandle find() {
        Objects.requireNonNull(methodName, Messages.Reflection.METHOD_NAME_NULL);
        Objects.requireNonNull(returnType, "Return type must not be null");
        Objects.requireNonNull(lookupKind, "MethodLookupKind must not be null");

        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(targetClass, MethodHandles.lookup());
            MethodType methodType = MethodType.methodType(returnType, parameterTypes);

            return switch (lookupKind) {
                case VIRTUAL -> lookup.findVirtual(targetClass, methodName, methodType);
                case STATIC -> lookup.findStatic(targetClass, methodName, methodType);
                case SPECIAL -> lookup.findSpecial(targetClass, methodName, methodType, targetClass);
                case CONSTRUCTOR -> lookup.findConstructor(targetClass, methodType);
            };
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new ReflectionException("Failed to find method handle for: " + (lookupKind == MethodLookupKind.CONSTRUCTOR ? targetClass.getName() : methodName), e);
        }
    }
}
