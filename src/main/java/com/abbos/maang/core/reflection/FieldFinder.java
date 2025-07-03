package com.abbos.maang.core.reflection;

import com.google.errorprone.annotations.Immutable;
import com.google.errorprone.annotations.Keep;
import org.jspecify.annotations.Nullable;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Provides reflective access to declared fields and their corresponding {@link VarHandle} representations.
 * <p>
 * Supports traversal of class hierarchies and conditional resolution based on name, type, and user-defined predicates.
 * This utility is designed for performance-sensitive or framework-level use cases where dynamic field access is required
 * without the cost of traditional reflective operations.
 *
 * <p>Example usage:
 * <pre>{@code
 * var handle = FieldFinder.findVarHandle(MyClass.class, "someField", Long.class);
 * handle.set(instance, 42); // Assigns a value reflectively
 * }</pre>
 *
 * <p>This class is stateless, thread-safe, and non-instantiable.
 *
 * @author Aliabbos Ashurov
 * @since 2025-06-08
 */
@Immutable
public final class FieldFinder {

    /**
     * Prevents instantiation.
     *
     * @throws ReflectionException always thrown to enforce non-instantiability
     */
    private FieldFinder() {
        throw new ReflectionException("FieldFinder is a utility class and cannot be instantiated");
    }

    /**
     * Resolves a {@link VarHandle} for a named instance field of the given type.
     *
     * @param targetClass the class in which the field is declared
     * @param fieldName   the name of the field
     * @param fieldType   the expected type of the field
     * @return a {@link VarHandle} for the field, or {@code null} if no matching field is found
     * @throws NullPointerException if any argument is {@code null}
     * @throws ReflectionException  if the {@link VarHandle} could not be created
     */
    public static VarHandle findVarHandle(Class<?> targetClass, String fieldName, Class<?> fieldType) {
        return findVarHandle(targetClass, fieldName, fieldType, vh -> true);
    }

    /**
     * Resolves a {@link VarHandle} for a named field of the given type, applying a filter to the resulting handle.
     *
     * @param targetClass the class in which the field is declared
     * @param fieldName   the name of the field
     * @param fieldType   the expected type of the field
     * @param filter      a predicate applied to the resulting {@link VarHandle}
     * @return a matching {@link VarHandle}, or {@code null} if not found or filtered out
     * @throws NullPointerException if any argument is {@code null}
     * @throws ReflectionException  if the {@link VarHandle} could not be created
     */
    @Nullable
    public static VarHandle findVarHandle(Class<?> targetClass, String fieldName, Class<?> fieldType, Predicate<VarHandle> filter) {
        Objects.requireNonNull(targetClass, Messages.Reflection.TARGET_CLASS_NULL);
        Objects.requireNonNull(fieldName, Messages.Reflection.FIELD_NAME_NULL);
        Objects.requireNonNull(filter, Messages.Reflection.FILTER_NULL);
        Objects.requireNonNull(fieldType, "fieldType must not be null");

        try {
            MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(targetClass, MethodHandles.lookup());
            return Optional.ofNullable(lookup.findVarHandle(targetClass, fieldName, fieldType))
                    .filter(filter)
                    .orElse(null);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new ReflectionException("Failed to obtain VarHandle for field: " + fieldName, e);
        }
    }

    /**
     * Searches for a {@link Field} declared with the given name, traversing the class hierarchy if necessary.
     *
     * @param targetClass the class to inspect
     * @param fieldName   the name of the field
     * @return the resolved {@link Field}, or {@code null} if not found
     * @throws NullPointerException if any argument is {@code null}
     */
    public static Field findField(Class<?> targetClass, String fieldName) {
        return findField(targetClass, fieldName, f -> true);
    }

    /**
     * Searches for a {@link Field} declared with the given name, using a predicate for filtering.
     * <p>
     * The search traverses up the superclass hierarchy until a matching field is found or the root class is reached.
     *
     * @param targetClass the class to inspect
     * @param fieldName   the name of the field
     * @param filter      a predicate to test candidate fields
     * @return the resolved {@link Field}, or {@code null} if no matching field is found
     * @throws NullPointerException if any argument is {@code null}
     * @throws ReflectionException  if a matching field exists but cannot be made accessible
     */
    @Keep
    @Nullable
    public static Field findField(Class<?> targetClass, String fieldName, Predicate<Field> filter) {
        Objects.requireNonNull(targetClass, Messages.Reflection.TARGET_CLASS_NULL);
        Objects.requireNonNull(fieldName, Messages.Reflection.FIELD_NAME_NULL);
        Objects.requireNonNull(filter, Messages.Reflection.FILTER_NULL);

        for (Class<?> current = targetClass; current != null && current != Object.class; current = current.getSuperclass()) {
            try {
                Field field = current.getDeclaredField(fieldName);
                if (filter.test(field)) {
                    if (!field.trySetAccessible()) {
                        throw new ReflectionException("Cannot access field: %s in class: %s"
                                                              .formatted(fieldName, current.getName()));
                    }
                    return field;
                }
            } catch (NoSuchFieldException ignored) {
            }
        }
        return null;
    }
}