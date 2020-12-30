package com.bebel.api.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.bebel.api.contrats.Updatable;
import com.bebel.api.elements.basique.DrawableElement;
import com.bebel.api.elements.basique.EventableElement;

/**
 * Classe de base pour un shader
 */
public abstract class AbstractShader implements Updatable {
    protected ShaderProgram shader;
    protected DrawableElement layer;
    protected float time;

    protected final String IMPORT_RGB = new StringBuilder()
            .append("vec3 red = vec3(1.0, 0.0, 0.0);").append("\n")
            .append("vec3 green = vec3(0.0, 1.0, 0.0);").append("\n")
            .append("vec3 blue = vec3(0.0, 0.0, 1.0);").append("\n")
            .append("\n").toString();

    protected final String IMPORT_HSB = new StringBuilder()
            .append("vec3 rgb2hsb(in vec3 c){").append("\n")
                .append("vec4 K = vec4(0.0, -1.0 / 3.0, 2.0 / 3.0, -1.0);").append("\n")
                .append("vec4 p = mix(vec4(c.bg, K.wz), vec4(c.gb, K.xy), step(c.b, c.g));").append("\n")
                .append("vec4 q = mix(vec4(p.xyw, c.r), vec4(c.r, p.yzx), step(p.x, c.r));").append("\n")
                .append("float d = q.x - min(q.w, q.y);").append("\n")
                .append("return vec3(abs(q.z + (q.w - q.y) / (6.0 * d + e)), d / (q.x + e), q.x);").append("\n")
            .append("}").append("\n")
            .append("\n")
            .append("vec3 hsb2rgb(in vec3 c){").append("\n")
                .append("vec3 rgb = clamp(abs(mod(c.x*6.0+vec3(0.0,4.0,2.0), 6.0)-3.0)-1.0, 0.0, 1.0 );").append("\n")
                .append("rgb = rgb*rgb*(3.0-2.0*rgb);").append("\n")
                .append("return c.z * mix(vec3(1.0), rgb, c.y);").append("\n")
            .append("}")
            .append("\n").toString();

    protected final String IMPORT_UTILS = new StringBuilder()
            .append("vec3 circle(in vec2 position, in float rayon){").append("\n")
                .append("vec2 p = (gl_FragCoord.xy / u_resolution.xy) - position;").append("\n")
                .append("float len = length(p);").append("\n")
                .append("return step(r, vec3(len));").append("\n")
            .append("}").append("\n")
            .append("\n")
            .append("vec3 tint(in vec3 c ){").append("\n")
                .append("return baseColor * mix(element, baseColor, tintColor);").append("\n")
            .append("}")
            .append("\n").toString();

    public AbstractShader(final String vert, final String frag) {
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(vert, frag);

        //ensure it compiled
        if (!shader.isCompiled())
            throw new GdxRuntimeException("Could not compile shader: " + shader.getLog());
        //print any warnings
        if (shader.getLog().length() != 0)
            System.out.println(shader.getLog());
    }

    /**
     * Permet de commencer le shader en lui attribuant les constantes et operations de base
     */
    public final void begin(final DrawableElement layer) {
        shader.begin();
        this.layer = layer;
        if (layer instanceof EventableElement) {
            ((EventableElement) layer).onMouseMove(m -> {
                shader.begin();
                shader.setUniformf("u_mouse", m.x, m.y);
                shader.end();
            });
        }

        begin();

        shader.end();
    }

    protected abstract void begin();

    /**
     * Appelé lorsque la fenetre est resize afin de mettre à jour la resolution
     */
    public void resize(final int w, final int h) {
        shader.begin();
        shader.setUniformf("u_resolution", w, h);
        shader.end();
    }

    @Override
    public boolean update(final float delta) {
        shader.begin();
        time += delta;
        shader.setUniformf("u_time", time);
        shader.end();
        return true;
    }

    public ShaderProgram shader() {
        return shader;
    }

    /**
     * appelé à chaque raffraichissement de l'image
     */
    public abstract void refresh();
}
