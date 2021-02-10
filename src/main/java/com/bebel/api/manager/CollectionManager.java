package com.bebel.api.manager;

import com.badlogic.gdx.utils.Pool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Manager permettant de gerer une collection
 */
public class CollectionManager<T> implements Pool.Poolable {
    protected List<T> elements = new ArrayList<>();

    /**
     * Retourne vrai Si tous ces elements au moins sont presents
     * @param elements
     * @return
     */
    public boolean contains(final T... elements) {return containsAllOf(elements);}
    public boolean containsAllOf(final T... elements) {
        return this.elements.containsAll(Arrays.asList(elements));
    }

    /**
     * Retourne vrai Si un de ces elements au moins est presents
     * @param elements
     * @return
     */
    public boolean containsOneOf(final T... elements) {
        for (final T element : elements) {
            if (this.elements.contains(element)) return true;
        }
        return false;
    }

    /**
     * Retourne vrai Si tous ces elements UNIQUEMENT sont presents
     * @param elements
     * @return
     */
    public boolean containsOnlyAllOf(final T... elements) {
        if (elements.length != this.elements.size()) return false;
        return containsAllOf(elements);
    }

    /**
     * Retourne vrai Si au moins un de ces element UNIQUEMENT est present
     * @param elements
     * @return
     */
    public boolean containsOnlyOneOf(final T... elements) {
        final List<T> elementsList = Arrays.asList(elements);
        for (final T element : this.elements) {
            if (!elementsList.contains(element)) return false;
        }
        return containsOneOf(elements);
    }

    @Override
    public void reset() {elements.clear();}

    public CollectionManager<T> set(final T... newElements) {return set(Arrays.asList(newElements));}
    public CollectionManager<T> set(final List<T> newElements) {
        this.elements.clear();
        this.elements.addAll(newElements);
        return this;
    }
}
