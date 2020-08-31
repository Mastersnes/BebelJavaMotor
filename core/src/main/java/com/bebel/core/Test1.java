package com.bebel.core;

import com.bebel.api.BebelGame;
import com.bebel.api.pointAndClick.elements.Tableau;
import com.bebel.api.pointAndClick.scenes.PointAndClick;
import com.bebel.api.resources.ResourceManager;
import com.bebel.core.tableaux.Coffre;
import com.bebel.core.tableaux.Tableaux;

public class Test1 extends BebelGame {
    @Override
    public void create() {
        super.create();
        ResourceManager.getInstance().finishLoading();

        PointAndClick.getInstance().select(Tableaux.COFFRE);
    }
}
