package com.bebel.api.utils;

/**
 * Classe utilitaire de l'API Bebel dedié au XML
 */
public class BebelXmlUtils {
    public static float[] parseFloatsCSV(String csv) {
        final String[] strings = csv.split("\\s*,\\s*");
        final int length = strings.length;
        final float[] floats = new float[length];

        for(int i = 0; i < length; ++i) {
            floats[i] = Float.parseFloat(strings[i].trim());
        }

        return floats;
    }
}
