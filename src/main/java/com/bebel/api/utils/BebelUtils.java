package com.bebel.api.utils;

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
