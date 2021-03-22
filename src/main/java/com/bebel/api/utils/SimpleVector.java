package com.bebel.api.utils;

import com.badlogic.gdx.utils.Pool;

import java.util.Objects;

/**
 * Represente un simple vecteur dynamique
 */
public class SimpleVector<T> implements Pool.Poolable {
    public T x;
    public T y;

    public void x(final T x) {this.x = x;}
    public T x() {return x;}

    public void y(final T y) {this.y = y;}
    public T y() {return y;}

    public void set(final T x, final T y) {x(x); y(y);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleVector<T> that = (SimpleVector<T>) o;
        return Objects.equals(x, that.x) && Objects.equals(y, that.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public void reset() {
        this.x = null;
        this.y = null;
    }
}
