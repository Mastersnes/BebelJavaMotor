package com.bebel.api.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.bebel.api.elements.basique.DrawableElement;

/**
 * Classe de base pour un shader
 */
public abstract class AbstractShader {
    protected ShaderProgram shader;

    public AbstractShader(final String vert, final String frag) {
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(vert, frag);

        //ensure it compiled
        if (!shader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: " + shader.getLog());
        //print any warnings
        if (shader.getLog().length() != 0)
            System.out.println(shader.getLog());

        begin();
    }

    /**
     * Permet de commencer le shader en lui attribuant les variables de base
     */
    protected abstract void begin();

    /**
     * Appelé lorsque la fenetre est resize
     */
    public void resize(final int w, final int h) {
    }

    public ShaderProgram shader() {
        return shader;
    }

    /**
     * Bind les textures du calque
     * @param layer
     */
    public abstract void bind(final DrawableElement layer);
}
