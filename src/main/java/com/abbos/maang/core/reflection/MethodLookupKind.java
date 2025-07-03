package com.abbos.maang.core.reflection;

import java.lang.invoke.MethodType;

/**
 * Defines method lookup types for reflection utilities using {@link java.lang.invoke.MethodHandles}.
 *
 * @author Aliabbos Ashurov
 * @since 2025-06-08
 */
public enum MethodLookupKind {
    /**
     * Virtual method lookup for instance methods, dispatched by runtime type.
     *
     * @see java.lang.invoke.MethodHandles.Lookup#findVirtual(Class, String, MethodType)
     */
    VIRTUAL,

    /**
     * Static method lookup for non-instance methods.
     *
     * @see java.lang.invoke.MethodHandles.Lookup#findStatic(Class, String, MethodType)
     */
    STATIC,

    /**
     * Special method lookup for super method calls.
     *
     * @see java.lang.invoke.MethodHandles.Lookup#findSpecial(Class, String, MethodType, Class)
     */
    SPECIAL,

    /**
     * Constructor lookup for creating class instances.
     *
     * @see java.lang.invoke.MethodHandles.Lookup#findConstructor(Class, MethodType)
     */
    CONSTRUCTOR
}