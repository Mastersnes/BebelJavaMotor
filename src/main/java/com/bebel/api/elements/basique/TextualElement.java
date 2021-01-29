package com.bebel.api.elements.basique;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.bebel.api.resources.assets.FontAsset;
import org.apache.commons.lang3.StringUtils;

/**
 * Represente un element textuel
 */
public class TextualElement extends DrawableElement {
    protected String texte;
    protected BitmapFont font;
    protected BitmapFont.TextBounds bounds;

    public TextualElement(final String name, final FontAsset font) {
        super(name, null, 0, 0);
        font(font);
    }

    protected void paintImpl(final SpriteBatch batch){
        draw(batch, font, texte);
        super.paintImpl(batch);
    }

    protected void draw(final SpriteBatch batch, final BitmapFont font, final String texte) {
        if (font != null && StringUtils.isNotEmpty(texte)) {
            font.drawWrapped(batch, texte, 0, 0, width);
        }
    }

    /**
     * Font
     */
    public TextualElement font(final FontAsset asset) {
        this.font = asset.get();
        return updateBounds();
    }

    public TextualElement text(final String... textes) {
        if (textes != null) this.texte = String.join("\n", texte);
        return updateBounds();
    }

    private TextualElement updateBounds() {
        bounds = font.getMultiLineBounds(texte);
        size(bounds.width, bounds.height);
        return this;
    }

}
