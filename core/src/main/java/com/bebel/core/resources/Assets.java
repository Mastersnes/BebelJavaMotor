package com.bebel.core.resources;

import com.bebel.api.resources.assets.TextureAsset;

/**
 *  Liste des assets du jeu
 */
public class Assets {
    static public class Coffre {
        public static TextureAsset BACKGROUND = new TextureAsset("images/game/coffre/back.jpg");
        public static TextureAsset FOREGROUND = new TextureAsset("images/game/coffre/front.png");

        public static TextureAsset SERRURE = new TextureAsset("images/game/coffre/items/serrure.png");
        public static TextureAsset TIGE = new TextureAsset("images/game/coffre/items/tige_normal.png");
        public static TextureAsset TIGE_ABIMEE = new TextureAsset("images/game/coffre/items/tige_abime.png");
    }
}
