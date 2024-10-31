package sample;

import util.Recorder;
import processing.core.*;

public class SampleSketch extends PApplet
{
    private final Recorder recorder = new Recorder(this);

    @Override
    public void settings()
    {
        size(512, 512, P3D);
    }

    @Override
    public void setup()
    {
        background(0);
        sphereDetail(32);
    }

    @Override
    public void draw()
    {
        background(0);
        ambientLight(32, 32, 32);
        lightSpecular(255, 255, 255);
        pointLight(16, 32, 230, mouseX, mouseY, 128);

        push();
        translate(width/2, height/2, 0);
        noStroke();
        fill(250, 8, 32);
        specular(250, 250, 250);
        shininess(5);
        sphere(64);
        pop();

        recorder.saveFrame();
    }

    @Override
    public void keyReleased()
    {
        if (key == 's')
        {
            recorder.saveImage();
        }
        if (key == 'd')
        {
            recorder.makeMovie(30, 30);
        }
    }

    public static void main(String[] args)
    {
        PApplet.main(SampleSketch.class);
    }
}
