package com.bebel.api.utils;

import com.badlogic.gdx.math.Vector2;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Represente un point de repere posé sur une map
 */
public class Jalon extends Vector2 {
    protected String name;
    protected Jalon up, down, left, right;

    public Jalon(final String name, final Vector2 position) {this(name, position.x, position.y);}
    public Jalon(final String name, final float x, final float y) {
        this.name = name;
        set(x, y);
    }

    public float x() {return x;}
    public float y() {return y;}

    public Jalon up() {return up;}
    public Jalon up(final String name, final float x, final float y) {
        this.up = new Jalon(name, x, y);
        return up.down = this;
    }

    public Jalon down() {return down;}
    public Jalon down(final String name, final float x, final float y) {
        this.down = new Jalon(name, x, y);
        return down.up = this;
    }

    public Jalon left() {return left;}
    public Jalon left(final String name, final float x, final float y) {
        this.left = new Jalon(name, x, y);
        return left.right = this;
    }

    public Jalon right() {return right;}
    public Jalon right(final String name, final float x, final float y) {
        this.right = new Jalon(name, x, y);
        return right.left = this;
    }

    /**
     * Permet de lister de facon recursive l'ensemble des jalons de la pile
     */
    public List<Jalon> listAll() {return listAllExcept(null);}
    protected List<Jalon> listAllExcept(final Jalon except) {
        final List<Jalon> jalons = new ArrayList<>();
        if (this != except) jalons.add(this);
        if (up != null && up != except) jalons.addAll(up.listAllExcept(this));
        if (down != null && down != except) jalons.addAll(down.listAllExcept(this));
        if (left != null && left != except) jalons.addAll(left.listAllExcept(this));
        if (right != null && right != except) jalons.addAll(right.listAllExcept(this));
        return jalons;
    }

    /**
     * Permet de trouver un jalon par son nom
     */
    public Jalon find(final String name) {
        for (final Jalon jalon : listAll()) {
            if (StringUtils.equals(jalon.name, name)) return jalon;
        }
        return null;
    }

    /**
     * Permet de trouver le meilleur chemin de ce repere à une destination donnée
     * La qualité du chemin est basé sur la distance totale de traversée
     */
    public Path findBestPath(final Jalon destination) {
        return findBestPath(destination, new ArrayList<>());
    }
    protected Path findBestPath(final Jalon destination, final List<Jalon> alreadyTested) {
        alreadyTested.add(this);
        final Path bestPath = new Path(this);
        if (this.equals(destination)) return bestPath;

        final List<Path> paths = new ArrayList<>();
        paths.add(findBestPath(up, destination, alreadyTested));
        paths.add(findBestPath(down, destination, alreadyTested));
        paths.add(findBestPath(left, destination, alreadyTested));
        paths.add(findBestPath(right, destination, alreadyTested));

        bestPath.addAll(
            paths.stream()
                .filter(Objects::nonNull)
                .min((path1, path2) -> (int) (path2.dst(this) - path1.dst(this)))
                .orElseThrow(NoSuchElementException::new)
        );
        return bestPath;
    }
    protected Path findBestPath(final Jalon origine, final Jalon destination, final List<Jalon> alreadyTested) {
        try {
            if (origine != null && !alreadyTested.contains(origine)) {
                return origine.findBestPath(destination, alreadyTested);
            }
        } catch (final NoSuchElementException e) {
            // Cas limite - Fin de la pile atteinte sans avoir trouvé la destination
        }
        return null;
    }

    @Override
    public String toString() {return name;}
}
