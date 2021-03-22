package com.bebel.api.resources.elements;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;

/**
 * Represente une hitbox
 */
public class Hitbox extends ArrayList<Polygon> {
    public void setPosition(float x, float y) {
        //
    }

    public void translate(float x, float y) {
        for (final Polygon polygon : this) {
            polygon.translate(x, y);
        }
    }

    public void setRotation(float degrees) {
        for (final Polygon polygon : this) {
            polygon.setRotation(degrees);
        }
    }

    public void rotate(float degrees) {
        for (final Polygon polygon : this) {
            polygon.rotate(degrees);
        }
    }

    public void setScale(float scaleX, float scaleY) {
        for (final Polygon polygon : this) {
            polygon.setScale(scaleX, scaleY);
        }
    }

    public void scale(float amount) {
        for (final Polygon polygon : this) {
            polygon.scale(amount);
        }
    }

    public boolean contains(Vector2 point) {
        return this.contains(point.x, point.y);
    }
    public boolean contains(float x, float y) {
        for (final Polygon polygon : this) {
            if (polygon.contains(x, y)) return true;
        }
        return false;
    }

    public boolean overlaps(final Polygon otherPolygon) {
        for (final Polygon polygon : this) {
            if (Intersector.overlapConvexPolygons(polygon, otherPolygon)) return true;
        }
        return false;
    }
    public boolean overlaps(final Hitbox otherHitbox) {
        for (final Polygon polygon : this) {
            for (final Polygon otherPolygon : otherHitbox) {
                if (Intersector.overlapConvexPolygons(polygon, otherPolygon)) return true;
            }
        }
        return false;
    }


}
