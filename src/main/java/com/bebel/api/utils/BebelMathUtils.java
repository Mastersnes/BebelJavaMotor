package com.bebel.api.utils;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pools;
import pythagoras.i.Point;

/**
 * Classe utilitaire de l'API Bebel dedié aux math
 */
public class BebelMathUtils {
    /**
     * Renvoi l'inconnu de correspondance
     *
     * @param src
     * @param corresp
     * @param inconnuSrc
     * @return
     */
    public static float crossProduct(final float src, final float corresp, final float inconnuSrc) {
        return (inconnuSrc * corresp) / src;
    }

    public static Point texelToPixel(final Texture image,
                                     final float srcWidth, final float srcHeight,
                                     final float cibleWidth, final float cibleHeight) {
        return texelToPixel(image.getWidth(), image.getHeight(), srcWidth, srcHeight, cibleWidth, cibleHeight);
    }

    private static Point texelToPixel(final int imageWidth, final int imageHeight,
                                      float srcWidth, float srcHeight,
                                      float cibleWidth, float cibleHeight) {
        final Point texel = Pools.obtain(Point.class);
        texel.x = (int) crossProduct(srcWidth, imageWidth, cibleWidth);
        texel.y = (int) crossProduct(srcHeight, imageHeight, cibleHeight);
        return texel;
    }
}
