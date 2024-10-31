package sample;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.opengl.PGL;
import util.Utility;
import util.PGLUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

public class PGLSample extends PApplet
{
    private PGraphics pg;
    private int program;
    private int vbo;

    @Override
    public void settings()
    {
        size(512, 512, P3D);
    }

    @Override
    public void setup()
    {
        pg = createGraphics(width, height, P3D);

        pg.beginDraw();

        PGL pgl = pg.beginPGL();

        String shaderPath = Utility.getAbsolutePathname("resources/shaders/sample/PGLSample");
        String vsrc = Utility.readTextFile(shaderPath + "/sample.vert");
        String fsrc = Utility.readTextFile(shaderPath + "/sample.frag");
        Map<String, Integer> attribLoc = new HashMap<>();
        attribLoc.put("position", 0);
        attribLoc.put("color", 1);
        program = PGLUtil.createShaderProgram(pgl, vsrc, fsrc, attribLoc,
                "sample.vert", "sample.frag", "program0");

        float[] vertices = {
                -0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
                0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
                0.0f, 0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
        };

        // create vbo and send data
        IntBuffer vboBuf = IntBuffer.allocate(1);
        pgl.genBuffers(1, vboBuf);
        vbo = vboBuf.get(0);
        pgl.bindBuffer(PGL.ARRAY_BUFFER, vbo);
        FloatBuffer vertexBuffer = FloatBuffer.wrap(vertices);
        pgl.bufferData(PGL.ARRAY_BUFFER, vertexBuffer.capacity() * Float.BYTES, vertexBuffer, PGL.STATIC_DRAW);

        pg.endPGL();

        pg.endDraw();
    }

    @Override
    public void draw()
    {
        pg.beginDraw();

        PGL pgl = pg.beginPGL();

        pgl.clearColor(0.1f, 0.2f, 0.4f, 1.0f);
        pgl.disable(PGL.BLEND); // Note that blending is enabled by default.

        pgl.bindBuffer(PGL.ARRAY_BUFFER, vbo);
        // Note that attribute pointer must be assigned each frame.
        pgl.vertexAttribPointer(0, 3, PGL.FLOAT, false, 7 * Float.BYTES, 0);
        pgl.enableVertexAttribArray(0);
        pgl.vertexAttribPointer(1, 4, PGL.FLOAT, false, 7 * Float.BYTES, 3 * Float.BYTES);
        pgl.enableVertexAttribArray(1);

        // draw
        pgl.useProgram(program);
        pgl.clear(PGL.COLOR_BUFFER_BIT | PGL.DEPTH_BUFFER_BIT);
        pgl.drawArrays(PGL.TRIANGLES, 0, 3);

        pg.endPGL();

        pg.endDraw();

        image(pg, 0, 0);
    }

    @Override
    public void exit()
    {
        // delete sources
        /*
        PGL pgl = pg.beginPGL();

        pgl.deleteProgram(program);
        pgl.deleteBuffers(1, IntBuffer.wrap(new int[]{vbo}));

        pg.endPGL();
        */

        super.exit();
    }

    public static void main(String[] args)
    {
        PApplet.main(PGLSample.class);
    }
}
