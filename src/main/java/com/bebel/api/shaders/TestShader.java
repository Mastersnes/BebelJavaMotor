package com.bebel.api.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.bebel.api.elements.basique.DrawableElement;
import org.lwjgl.opengl.Display;

/**
 * Shader de test affichant une lumiere
 */
public class TestShader extends AbstractShader {
    private static final String VERT = new StringBuilder()
            .append("attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";").append("\n")
            .append("attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";").append("\n")
            .append("attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;").append("\n")

            .append("uniform mat4 u_projTrans;").append("\n")
            .append("varying vec4 v_color;").append("\n")
            .append("varying vec2 v_texCoords;").append("\n")

            .append("void main() {").append("\n")
                .append("v_color = " + ShaderProgram.COLOR_ATTRIBUTE + ";").append("\n")
                .append("v_texCoords = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;").append("\n")
                .append("gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";").append("\n")
            .append("}").toString();

    private static final String FRAG = new StringBuilder()
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

            .append("void main() {").append("\n")
                .append("vec2 st = gl_FragCoord.xy/u_resolution;").append("\n")
                .append("gl_FragColor = vec4(st.x,st.y,0.0,1.0);").append("\n")
            .append("}").toString();

    public TestShader() {
        super(VERT, FRAG);
    }

    @Override
    protected void begin() {
    }

    @Override
    public void resize(int w, int h) {}

    @Override
    public void refresh() {
//        final Texture image = layer.getImage();
//        final Texture normal = layer.getNormal();
//
//        shader.setUniformf("LightPos", LIGHT_POS);
//        if (normal != null && image != null) {
//            normal.bind(1);
//            image.bind(0);
//        }
    }
}
