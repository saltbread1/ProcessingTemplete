package util;

import processing.core.PApplet;
import processing.core.PVector;

public class Quaternion
{
    private float x, y, z, w;

    public Quaternion()
    {
        w = 1;
    }

    public Quaternion(float x, float y, float z, float w)
    {
        set(x, y, z, w);
    }

    public Quaternion(PVector dir, float theta)
    {
        PVector normDir = dir.copy().normalize(null);
        x = PApplet.sin((theta * 0.5f)) * normDir.x;
        y = PApplet.sin((theta * 0.5f)) * normDir.y;
        z = PApplet.sin((theta * 0.5f)) * normDir.z;
        w = PApplet.cos((theta * 0.5f));
    }

    public void set(float x, float y, float z, float w)
    {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * calculate the product of "this x other"
     *
     * @param q multiplying quaternion from right
     * @return product
     */
    public Quaternion mult(Quaternion q)
    {
        float w1 = w;
        float w2 = q.w;
        PVector v1 = new PVector(x, y, z);
        PVector v2 = new PVector(q.x, q.y, q.z);
        float neww = w1 * w2 - PVector.dot(v1, v2);
        PVector newv = PVector.mult(v2, w1).add(PVector.mult(v1, w2)).add(v1.cross(v2));
        return new Quaternion(newv.x, newv.y, newv.z, neww);
    }

    public Quaternion mult(PVector v)
    {
        return mult(new Quaternion(v.x, v.y, v.z, 0));
    }

    public Quaternion multeq(Quaternion q)
    {
        float w1 = w;
        float w2 = q.w;
        PVector v1 = new PVector(x, y, z);
        PVector v2 = new PVector(q.x, q.y, q.z);
        float neww = w1 * w2 - PVector.dot(v1, v2);
        PVector newv = PVector.mult(v2, w1).add(PVector.mult(v1, w2)).add(v1.cross(v2));
        x = newv.x;
        y = newv.y;
        z = newv.z;
        w = neww;
        return this;
    }

    public Quaternion diveq(float n)
    {
        x /= n;
        y /= n;
        z /= n;
        w /= n;
        return this;
    }

    public Quaternion inverse(Quaternion target)
    {
        if (target == null)
        {
            target = new Quaternion();
        }
        target.set(x * (-1), y * (-1), z * (-1), w);
        float msq = x * x + y * y + z * z + w * w;
        if (msq > 0 && msq != 1)
        {
            target.diveq(msq);
        }
        return target;
    }

    public PVector getVector()
    {
        return new PVector(x, y, z);
    }
}
