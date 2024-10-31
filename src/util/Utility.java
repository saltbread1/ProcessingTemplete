package util;

import processing.core.PGraphics;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGraphicsOpenGL;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static processing.core.PApplet.*;

public final class Utility
{
    private Utility()
    {
    }

    /**
     * get absolute pathname
     * @param relative relative pathname from project root
     * @return absolute pathname
     */
    public static String getAbsolutePathname(String relative)
    {
        return new File(relative).getAbsolutePath();
    }

    /**
     * get timestamp like 20380119031407_####
     * @return timestamp
     */
    public static String timestamp()
    {
        return year() + nf(month(), 2) + nf(day(), 2)
                + nf(hour(), 2) + nf(minute(), 2) + nf(second(), 2);
    }

    /**
     * read text file
     * @param path path to a file
     * @return contents of a file
     */
    public static String readTextFile(String path)
    {
        try
        {
            return Files.readString(Path.of(path), StandardCharsets.UTF_8);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }

    public static float sign(float x)
    {
        if (x >= 0)
        {
            return 1;
        }
        return -1;
    }

    public static float clamp(float x, float a, float b)
    {
        return min(max(x, a), b);
    }

    public static float step(float a, float x)
    {
        if (x < a)
        {
            return 0;
        }
        return 1;
    }

    public static float smoothstep(float a, float b, float x)
    {
        if (x <= a)
        {
            return 0.0f;
        }
        if (x >= b)
        {
            return 1.0f;
        }
        float t = (x - a) / (b - a);
        return t * t * (3.0f - 2.0f * t);
    }

    /**
     * production of each vector element
     *
     * @return production
     */
    public static PVector mult(PVector v0, PVector v1)
    {
        return new PVector(v0.x * v1.x, v0.y * v1.y, v0.z * v1.z);
    }

    /**
     * quotient of each vector element
     *
     * @return quotient
     */
    public static PVector div(PVector v0, PVector v1)
    {
        return mult(v0, new PVector(1.0f / v1.x, 1.0f / v1.y, 1.0f / v1.z));
    }

    public static PVector rotate3D(PVector target, PVector dir, float rad)
    {
        Quaternion q = new Quaternion(dir, rad);
        Quaternion qi = q.inverse(null);
        Quaternion qr = q.mult(target).multeq(qi);
        return qr.getVector();
    }

    public static PVector rotate3D(PVector target, PVector dir, float rad, PVector center)
    {
        return rotate3D(PVector.sub(target, center), dir, rad).add(center);
    }

    /**
     * apply 4x4 matrix to vector
     * @param v target vector
     * @return transformed vector
     */
    public static PVector applyMatrix(PVector v, PMatrix3D matrix)
    {
        float ax = matrix.m00 * v.x + matrix.m01 * v.y + matrix.m02 * v.z + matrix.m03;
        float ay = matrix.m10 * v.x + matrix.m11 * v.y + matrix.m12 * v.z + matrix.m13;
        float az = matrix.m20 * v.x + matrix.m21 * v.y + matrix.m22 * v.z + matrix.m23;
        float aw = matrix.m30 * v.x + matrix.m31 * v.y + matrix.m32 * v.z + matrix.m33;
        return new PVector(ax / aw, ay / aw, az / aw);
    }

    public static void begin2D(PGraphics target)
    {
        target.hint(PGraphics.DISABLE_DEPTH_TEST);
        target.pushMatrix();
        ((PGraphicsOpenGL) target).pushProjection();
        target.resetMatrix();
        target.translate(-target.width * 0.5f, -target.height * 0.5f);
        target.ortho();
    }

    public static void end2D(PGraphics target)
    {
        ((PGraphicsOpenGL) target).popProjection();
        target.popMatrix();
        target.hint(PGraphics.ENABLE_DEPTH_TEST);
    }

    public static PMatrix3D getTexMatrix()
    {
        return new PMatrix3D(
                0.5f, 0.0f, 0.0f, 0.5f,
                0.0f, 0.5f, 0.0f, 0.5f,
                0.0f, 0.0f, 1.0f, 0.0f,
                0.0f, 0.0f, 0.0f, 1.0f);
    }
}
