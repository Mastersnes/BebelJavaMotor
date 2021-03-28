package com.bebel.api.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.bebel.api.contrats.Updatable;
import com.bebel.api.elements.basique.predicats.AbstractElement;
import com.bebel.api.elements.basique.predicats.EventableElement;

/**
 * Classe de base pour un shader
 */
public abstract class AbstractShader implements Updatable {
    protected ShaderProgram shader;
    protected AbstractElement layer;
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
                .append("float e = 1.0e-10;").append("\n")
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
            .append("float circle (in vec2 position, in float rayon) {").append("\n")
                .append("vec2 repere = gl_FragCoord.xy/u_resolution.xy;").append("\n")
                .append("vec2 pos = repere - position;").append("\n")
                .append("float result = step(rayon, length(pos));").append("\n")
                .append("return result;").append("\n")
            .append("}").append("\n")
            .append("vec3 circle (in vec2 position, in float rayon, vec3 color) {").append("\n")
                .append("return (1.0-circle(position, rayon))*color;").append("\n")
            .append("}").append("\n")
            .append("\n")
            .append("vec3 smoothCircle (in vec2 position, in float rayon, float flou) {").append("\n")
                .append("vec2 repere = gl_FragCoord.xy/u_resolution.xy;").append("\n")
                .append("vec2 pos = repere - position;").append("\n")
                .append("float result = smoothstep(rayon-flou, rayon+flou, length(pos));").append("\n")
                .append("return result;").append("\n")
            .append("}").append("\n")
            .append("vec3 smoothCircle (in vec2 position, in float rayon, float flou, vec3 color) {").append("\n")
                .append("return (1.0-smoothCircle(position, rayon))*color;").append("\n")
            .append("}").append("\n")
            .append("vec3 rect(in vec2 position, in vec2 size, in vec3 color) {").append("\n")
                .append("vec2 repere = gl_FragCoord.xy/u_resolution.xy;").append("\n")
                .append("vec2 bl = step(position, repere);").append("\n")
                .append("float result = bl.x * bl.y;").append("\n")
                .append("vec2 tr = step(1.0-size-position,1.0-repere);").append("\n")
                .append("result *= tr.x * tr.y;").append("\n")
                .append("return result*color;").append("\n")
            .append("}").append("\n")
            .append("vec3 smoothRect(in vec2 position, in vec2 size, in float flou, in vec3 color) {").append("\n")
                .append("vec2 repere = gl_FragCoord.xy/u_resolution.xy;").append("\n")
                .append("vec2 bl = smoothstep(position-flou, position+flou, repere);").append("\n")
                .append("float result = bl.x * bl.y;").append("\n")
                .append("vec2 tr = smoothstep(1.0-size-position-flou, 1.0-size-position+flou,1.0-repere);").append("\n")
                .append("result *= tr.x * tr.y;").append("\n")
                .append("return result*color;").append("\n")
            .append("}").append("\n")
            .append("vec3 replace(vec3 color1, vec3 color2, vec3 color) {").append("\n")
                .append("if (color == color1) return color2;").append("\n")
                .append("else return color;").append("\n")
            .append("}").append("\n")
            .append("\n").toString();

    private final String SUPER_VERT = new StringBuilder()
            .append("attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";").append("\n")
            .append("attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";").append("\n")
            .append("attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;").append("\n")

            .append("uniform mat4 u_projTrans;").append("\n")
            .append("varying vec4 v_color;").append("\n")
            .append("varying vec2 v_texCoords;").append("\n")
            .append(VERT())
            .append("\n").toString();

    private final String SUPER_FRAG = new StringBuilder()
            .append("#ifdef GL_ES").append("\n")
                .append("precision mediump float;").append("\n")
            .append("#endif").append("\n")

            .append("varying vec4 v_color;").append("\n")
            .append("varying vec2 v_texCoords;").append("\n")
            .append("uniform sampler2D u_texture;").append("\n")
            .append("uniform mat4 u_projTrans;").append("\n")

            .append("uniform vec2 u_resolution;").append("\n")
            .append("uniform vec2 u_mouse;").append("\n")
            .append("uniform float u_time;").append("\n")

            .append(IMPORT_UTILS).append("\n")
            .append(IMPORT_RGB).append("\n")
            .append(IMPORT_HSB).append("\n")

            .append(FRAG())
            .append("\n").toString();

    protected String VERT() {
        return new StringBuilder()
                .append("void main() {").append("\n")
                    .append("v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";").append("\n")
                    .append("v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;").append("\n")
                    .append("gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";").append("\n")
                .append("}").toString();
    }

    protected String FRAG() {
        return new StringBuilder()
                .append("void main() {").append("\n")
                    .append("vec4 texColor = texture2D(u_texture, v_texCoords);").append("\n")
                    .append("gl_FragColor = texColor * v_color;").append("\n")
                .append("}").toString();
    }

    public AbstractShader() {
        init(SUPER_VERT, SUPER_FRAG);
    }
    public AbstractShader(final String vert, final String frag) {
        init(vert, frag);
    }

    private void init(final String vert, final String frag) {
        ShaderProgram.pedantic = false;

        shader = new ShaderProgram(vert, frag);

        //ensure it compiled
        if (!shader.isCompiled()) {
            final StringBuilder err = new StringBuilder("ERROR, Could not compile shader")
                    .append("CAUSE : ").append(shader.getLog()).append("\n\n")
                    .append("VERT : ").append(SUPER_VERT).append("\n\n");

            throw new GdxRuntimeException(err.toString());
        }
        //print any warnings
        if (shader.getLog().length() != 0)
            System.out.println(shader.getLog());
    }

    /**
     * Permet de commencer le shader en lui attribuant les constantes et operations de base
     */
    public final void begin(final AbstractElement layer) {
        shader.bind();
        this.layer = layer;
        if (layer instanceof EventableElement) {
            ((EventableElement) layer).onMouseMove(m -> {
                shader.bind();
                shader.setUniformf("u_mouse", m.x, m.y);
            });
        }

        begin();
    }

    protected abstract void begin();

    /**
     * Appelé lorsque la fenetre est resize afin de mettre à jour la resolution
     */
    public void resize(final int w, final int h) {
        shader.bind();
        shader.setUniformf("u_resolution", w, h);
    }

    @Override
    public boolean update(final float delta) {
        shader.bind();
        time += delta;
        shader.setUniformf("u_time", time);
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
