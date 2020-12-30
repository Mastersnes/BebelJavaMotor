package com.bebel.api.shaders;

import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.Vector3;
import com.bebel.api.elements.basique.DrawableElement;
import com.bebel.api.elements.basique.EventableElement;
import org.lwjgl.opengl.Display;

/**
 * Shader de test affichant une lumiere
 */
public class LightShader extends AbstractShader {
    public static final float DEFAULT_LIGHT_Z = 0.075f;
    public static final float AMBIENT_INTENSITY = 0.2f;
    public static final float LIGHT_INTENSITY = 1f;

    public static final Vector3 LIGHT_POS = new Vector3(0f, 0f, DEFAULT_LIGHT_Z);

    //Light RGB and intensity (alpha)
    public static final Vector3 LIGHT_COLOR = new Vector3(0.8f, 0.2f, 0.2f);

    //Ambient RGB and intensity (alpha)
    public static final Vector3 AMBIENT_COLOR = new Vector3(0.4f, 0.4f, 0.8f);

    //Attenuation coefficients for light falloff
    public static final Vector3 FALLOFF = new Vector3(.4f, 3f, 20f);


    private static final String VERT =
            "attribute vec4 " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "attribute vec4 " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
                    "attribute vec2 " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +

                    "uniform mat4 u_projTrans;\n" +
                    " \n" +
                    "varying vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +

                    "void main() {\n" +
                    "	vColor = " + ShaderProgram.COLOR_ATTRIBUTE + ";\n" +
                    "	vTexCoord = " + ShaderProgram.TEXCOORD_ATTRIBUTE + "0;\n" +
                    "	gl_Position =  u_projTrans * " + ShaderProgram.POSITION_ATTRIBUTE + ";\n" +
                    "}";

    //no changes except for LOWP for color values
    //we would store this in a file for increased readability
    private static final String FRAG =
            //GL ES specific stuff
            "#ifdef GL_ES\n" //
                    + "#define LOWP lowp\n" //
                    + "precision mediump float;\n" //
                    + "#else\n" //
                    + "#define LOWP \n" //
                    + "#endif\n" + //
                    "//attributes from vertex shader\n" +
                    "varying LOWP vec4 vColor;\n" +
                    "varying vec2 vTexCoord;\n" +
                    "\n" +
                    "//our texture samplers\n" +
                    "uniform sampler2D u_texture;   //diffuse map\n" +
                    "uniform sampler2D u_normals;   //normal map\n" +
                    "\n" +
                    "//values used for shading algorithm...\n" +
                    "uniform vec2 Resolution;         //resolution of screen\n" +
                    "uniform vec3 LightPos;           //light position, normalized\n" +
                    "uniform LOWP vec4 LightColor;    //light RGBA -- alpha is intensity\n" +
                    "uniform LOWP vec4 AmbientColor;  //ambient RGBA -- alpha is intensity \n" +
                    "uniform vec3 Falloff;            //attenuation coefficients\n" +
                    "\n" +
                    "void main() {\n" +
                    "	//RGBA of our diffuse color\n" +
                    "	vec4 DiffuseColor = texture2D(u_texture, vTexCoord);\n" +
                    "	\n" +
                    "	//RGB of our normal map\n" +
                    "	vec3 NormalMap = texture2D(u_normals, vTexCoord).rgb;\n" +
                    "	\n" +
                    "	//The delta position of light\n" +
                    "	vec3 LightDir = vec3(LightPos.xy - (gl_FragCoord.xy / Resolution.xy), LightPos.z);\n" +
                    "	\n" +
                    "	//Correct for aspect ratio\n" +
                    "	LightDir.x *= Resolution.x / Resolution.y;\n" +
                    "	\n" +
                    "	//Determine distance (used for attenuation) BEFORE we normalize our LightDir\n" +
                    "	float D = length(LightDir);\n" +
                    "	\n" +
                    "	//normalize our vectors\n" +
                    "	vec3 N = normalize(NormalMap * 2.0 - 1.0);\n" +
                    "	vec3 L = normalize(LightDir);\n" +
                    "	\n" +
                    "	//Pre-multiply light color with intensity\n" +
                    "	//Then perform \"N dot L\" to determine our diffuse term\n" +
                    "	vec3 Diffuse = (LightColor.rgb * LightColor.a) * max(dot(N, L), 0.0);\n" +
                    "\n" +
                    "	//pre-multiply ambient color with intensity\n" +
                    "	vec3 Ambient = AmbientColor.rgb * AmbientColor.a;\n" +
                    "	\n" +
                    "	//calculate attenuation\n" +
                    "	float Attenuation = 1.0 / ( Falloff.x + (Falloff.y*D) + (Falloff.z*D*D) );\n" +
                    "	\n" +
                    "	//the calculation which brings it all together\n" +
                    "	vec3 Intensity = Ambient + Diffuse * Attenuation;\n" +
                    "	vec3 FinalColor = DiffuseColor.rgb * Intensity;\n" +
                    "	gl_FragColor = vColor * vec4(FinalColor, DiffuseColor.a);\n" +
                    "}";

    public LightShader() {
        super(VERT, FRAG);
    }

    @Override
    protected void begin() {
        //our normal map
        shader.setUniformi("u_normals", 1); //GL_TEXTURE1

        //light/ambient colors
        shader.setUniformf("LightColor", LIGHT_COLOR.x, LIGHT_COLOR.y, LIGHT_COLOR.z, LIGHT_INTENSITY);
        shader.setUniformf("AmbientColor", AMBIENT_COLOR.x, AMBIENT_COLOR.y, AMBIENT_COLOR.z, AMBIENT_INTENSITY);
        shader.setUniformf("Falloff", FALLOFF);
    }

    @Override
    public void resize(int w, int h) {
        shader.begin();
        shader.setUniformf("Resolution", w, h);
        shader.end();
    }

    public float lightZ() {
        return LIGHT_POS.z;
    }
    public void lightZ(float z) {
        LIGHT_POS.z = Math.max(0f, z);
    }

    public void resetLightZ() {
        LIGHT_POS.z = DEFAULT_LIGHT_Z;
    }

    public void lightPos(float x, float y) {
        LIGHT_POS.x = x / Display.getWidth();
        LIGHT_POS.y = (Display.getHeight() - y) / Display.getHeight();
    }

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
