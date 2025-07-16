package com.abbos.maang.data_structures;

import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * A generic, immutable class that holds two elements of potentially different types.
 * The Pair class provides utility methods for creating, manipulating, and combining
 * the paired elements. It is serializable and supports operations such as swapping,
 * mapping, and combining the elements using provided functions.
 *
 * @param <L> the type of the left element
 * @param <R> the type of the right element
 * @author Aliabbos Ashurov
 * @since 10/March/2025 11:10
 */
public class Pair<L, R> implements java.io.Serializable {

    @java.io.Serial
    private static final long serialVersionUID = -2139667479971038690L;

    private final L left;
    private final R right;
    private boolean lazy;

    /**
     * Constructs a new Pair instance with the specified left and right elements.
     * This constructor is private; use static factory methods {@link #of(Object, Object)}
     * or {@link #ofNullable(Object, Object)} (Object, Object)} to create instances.
     *
     * @param left  the left element
     * @param right the right element
     * @param lazy  flag indicating if the pair is lazily initialized
     */
    private Pair(L left, R right, boolean lazy) {
        this.left = left;
        this.right = right;
        this.lazy = lazy;
    }

    /**
     * Creates a new Pair instance with the specified left and right elements.
     * Both elements can be null.
     *
     * @param <L>   the type of the left element
     * @param <R>   the type of the right element
     * @param left  the left element
     * @param right the right element
     * @return a new Pair instance containing the specified elements
     */
    public static <L, R> Pair<L, R> ofNullable(L left, R right) {
        return new Pair<>(left, right, false);
    }

    /**
     * Creates a new Pair instance with the specified left and right elements,
     * ensuring neither element is null.
     *
     * @param <L>   the type of the left element
     * @param <R>   the type of the right element
     * @param left  the left element (must not be null)
     * @param right the right element (must not be null)
     * @return a new Pair instance containing the specified non-null elements
     * @throws NullPointerException if either left or right is null
     */
    public static <L, R> Pair<L, R> of(L left, R right) {
        Objects.requireNonNull(right, "Right value cannot be null");
        Objects.requireNonNull(left, "Left value cannot be null");
        return new Pair<>(left, right, false);
    }

    /**
     * Creates a new Pair with the elements swapped.
     *
     * @return a new Pair with the left element as the original right element
     * and the right element as the original left element
     */
    public Pair<R, L> swap() {
        return new Pair<>(right, left, false);
    }

    /**
     * Combines the left and right elements using the provided BiFunction.
     *
     * @param <T>    the type of the result
     * @param biFunc the function to apply to the left and right elements
     * @return the result of applying the BiFunction to the pair's elements
     */
    public <T extends java.io.Serializable> T combine(BiFunction<? super L, ? super R, ? extends T> biFunc) {
        return biFunc.apply(left, right);
    }

    /**
     * Creates a new Pair by applying the specified function to the left element,
     * keeping the right element unchanged.
     *
     * @param <T>  the type of the new left element
     * @param func the function to apply to the left element
     * @return a new Pair with the transformed left element and the original right element
     */
    public <T> Pair<T, R> mapLeft(Function<? super L, ? extends T> func) {
        return new Pair<>(func.apply(left), right, false);
    }

    /**
     * Creates a new Pair by applying the specified function to the right element,
     * keeping the left element unchanged.
     *
     * @param <T>  the type of the new right element
     * @param func the function to apply to the right element
     * @return a new Pair with the original left element and the transformed right element
     */
    public <T> Pair<L, T> mapRight(Function<? super R, ? extends T> func) {
        return new Pair<>(left, func.apply(right), false);
    }

    /**
     * Returns a string representation of the Pair.
     *
     * @return a string in the format "Pair{left=<left>, right=<right>}"
     */
    @Override
    public String toString() {
        return "Pair{left=" + left + ", right=" + right + '}';
    }

    /**
     * Compares this Pair to another object for equality.
     * Two Pairs are equal if their left and right elements are equal according to
     * {@link Objects#equals(Object, Object)}.
     *
     * @param obj the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof Pair<?, ?> o)) return false;
        return Objects.equals(left, o.left) && Objects.equals(right, o.right);
    }

    /**
     * Returns the hash code of this Pair, based on the hash codes of its elements.
     *
     * @return the hash code value for this Pair
     */
    @Override
    public int hashCode() {
        return Objects.hash(left, right);
    }

    public L getLeft() {
        return left;
    }

    public R getRight() {
        return right;
    }

    public boolean isLazy() {
        return lazy;
    }
}