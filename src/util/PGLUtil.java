package util;

import com.jogamp.opengl.GL4;
import processing.core.PImage;
import processing.opengl.PGL;

import java.nio.Buffer;
import java.nio.IntBuffer;
import java.util.Map;

public class PGLUtil
{
    private PGLUtil()
    {
    }

    /**
     * create a shader program object from shader sources
     * @param pgl rendering context
     * @param vsrc vertex shader source
     * @param fsrc fragment shader source
     * @param attribLoc a map corresponded attribute name to location
     * @param vIdentifier identifier of vertex shader
     * @param fIdentifier identifier of fragment shader
     * @param pIdentifier identifier of program
     * @return program object
     */
    public static int createShaderProgram(PGL pgl, String vsrc, String fsrc, Map<String, Integer> attribLoc,
                                          String vIdentifier, String fIdentifier, String pIdentifier)
    {
        int vs = pgl.createShader(PGL.VERTEX_SHADER);
        int fs = pgl.createShader(PGL.FRAGMENT_SHADER);
        pgl.shaderSource(vs, vsrc);
        pgl.compileShader(vs);
        checkShaderCompileStatus(pgl, vs, vIdentifier);
        pgl.shaderSource(fs, fsrc);
        pgl.compileShader(fs);
        checkShaderCompileStatus(pgl, fs, fIdentifier);

        int program = pgl.createProgram();
        pgl.attachShader(program, vs);
        pgl.attachShader(program, fs);
        pgl.deleteShader(vs);
        pgl.deleteShader(fs);
        for (Map.Entry<String, Integer> entry : attribLoc.entrySet())
        {
            pgl.bindAttribLocation(program, entry.getValue(), entry.getKey());
        }
        pgl.linkProgram(program);
        checkProgramLinkStatus(pgl, program, pIdentifier);

        return program;
    }

    private static void checkShaderCompileStatus(PGL pgl, int shader, String identifier)
    {
        IntBuffer status = IntBuffer.allocate(1);
        pgl.getShaderiv(shader, PGL.COMPILE_STATUS, status);
        if (status.get(0) == 0)
        {
            System.err.println("Shader compile error in " + identifier + ":");
        }

        IntBuffer bufSize = IntBuffer.allocate(1);
        pgl.getShaderiv(shader, PGL.INFO_LOG_LENGTH, bufSize);

        if (bufSize.get(0) > 1)
        {
            System.err.println(pgl.getShaderInfoLog(shader));
        }
    }

    private static void checkProgramLinkStatus(PGL pgl, int program, String pIdentifier)
    {
        IntBuffer status = IntBuffer.allocate(1);
        pgl.getProgramiv(program, PGL.LINK_STATUS, status);
        if (status.get(0) == 0)
        {
            System.err.println("Program link error in " + pIdentifier + ":");
        }

        IntBuffer bufSize = IntBuffer.allocate(1);
        pgl.getProgramiv(program, PGL.INFO_LOG_LENGTH, bufSize);

        if (bufSize.get(0) > 1)
        {
            System.err.println(pgl.getProgramInfoLog(program));
        }
    }

    /**
     * create texture
     * @param pgl rendering context
     * @param internalformat internalformat
     * @param width width of texture
     * @param height height of texture
     * @param format format
     * @param type type
     * @param buffer buffer
     * @return texture ID
     */
    public static int createTexture(PGL pgl, int internalformat, int width, int height, int format, int type, Buffer buffer)
    {
        IntBuffer texID = IntBuffer.allocate(1);
        pgl.genTextures(1, texID);
        if (buffer != null)
        {
            pgl.pixelStorei(PGL.UNPACK_ALIGNMENT, 1);
        }
        pgl.bindTexture(PGL.TEXTURE_2D, texID.get(0));
        pgl.texImage2D(PGL.TEXTURE_2D, 0, internalformat, width, height, 0, format, type, buffer);

        return texID.get(0);
    }

    /**
     * create texture from PImage
     * @param pgl rendering context
     * @param img target image
     * @return texture ID
     */
    public static int pImageToTexture(PGL pgl, PImage img)
    {
        img.loadPixels();

        return createTexture(pgl, PGL.RGBA8, img.width, img.height, GL4.GL_BGRA, PGL.UNSIGNED_BYTE, IntBuffer.wrap(img.pixels));
    }
}
