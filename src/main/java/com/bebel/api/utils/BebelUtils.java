package com.bebel.api.utils;

import com.bebel.api.elements.basique.predicats.AbstractElement;
import com.bebel.api.elements.basique.predicats.MovableElement;

import java.util.Comparator;

/**
 * Classe utilitaire de l'API Bebel
 */
public class BebelUtils {
    /**
     * Renvoi un jeton unique
     */
    public static String jeton() {
        return System.nanoTime() + "";
    }
}
