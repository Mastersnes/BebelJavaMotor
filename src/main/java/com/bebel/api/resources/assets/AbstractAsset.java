package com.bebel.api.resources.assets;

import com.badlogic.gdx.utils.Disposable;
import com.bebel.api.resources.ResourceManager;
import react.RList;

/**
 * Represente une ressource abstraite
 * Les ressources sont automatiquement chargée et mise en cache pour une utilisation ulterieure
 * @param <TYPE>
 */
public abstract class AbstractAsset<TYPE> {
    public final static RList<AbstractAsset> assets = RList.create();
    static {
        assets.connect(new RList.Listener<AbstractAsset>() {
            @Override
            public void onAdd(AbstractAsset elem) {
                elem.load();
            }

            @Override
            public void onRemove(AbstractAsset elem) {
                final Object res = elem.get();
                if (res instanceof Disposable) {
                    ((Disposable)res).dispose();
                }
            }
        });
    }


    protected final String path;
    protected final Class<TYPE> type;

    public AbstractAsset(final String path, final Class<TYPE> type) {
        this.path = path;
        this.type = type;
        if (path != null) assets.add(this);
    }

    public TYPE get() {
        return ResourceManager.getInstance().get(path);
    }

    public void load() {
        ResourceManager.getInstance().load(path, type);
    }
}
