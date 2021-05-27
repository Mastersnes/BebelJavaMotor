package com.bebel.api.elements.basique;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bebel.api.elements.basique.predicats.CollisionableElement;
import com.bebel.api.resources.assets.FontAsset;
import com.bebel.api.resources.assets.PhysicsAsset;
import org.apache.commons.lang3.StringUtils;

/**
 * Represente un element textuel
 */
public class TextualElement extends CollisionableElement {
    protected String texte;
    protected BitmapFont font;

    public TextualElement(final String name, final FontAsset font) {
        this(name, font, null);
    }
    public TextualElement(final String name, final FontAsset font, final PhysicsAsset physics) {
        super(name, null, physics);
        font(font);
    }

    protected void paintImpl(final SpriteBatch batch){
        draw(batch, font, texte);
        super.paintImpl(batch);
    }

    protected void draw(final SpriteBatch batch, final BitmapFont font, final String texte) {
        if (font != null && StringUtils.isNotEmpty(texte)) {
            font.draw(batch, texte, 0, 0, width, 0, true);
        }
    }

    /**
     * Font
     */
    public TextualElement font(final FontAsset asset) {
        this.font = asset.get();
        return this;
    }

    public TextualElement text(final String... textes) {
        if (textes != null) this.texte = String.join("\n", texte);
        return this;
    }

}
