package util;

import processing.core.PApplet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Recorder
{
    private final PApplet pApplet;
    private final String outdirname;

    public Recorder(PApplet pApplet, String outdirname)
    {
        this.pApplet = pApplet;
        this.outdirname = outdirname;

        // delete cache directory
        ProcessBuilder builder = new ProcessBuilder("rm", "-rf", outdirname + "/cache");
        exec(builder);
    }

    public Recorder(PApplet pApplet)
    {
        this(pApplet, "images/" + pApplet.getClass().getSimpleName());
    }

    /**
     * save image file
     * @param extension extension of output file
     */
    public void saveImage(String extension)
    {
        String relative = outdirname + "/" + Utility.timestamp() + "_####." + extension;
        String filename = Utility.getAbsolutePathname(relative);
        pApplet.saveFrame(filename);
        System.out.println("Save image: " + filename);
    }

    /**
     * save png file
     */
    public void saveImage()
    {
        saveImage("png");
    }

    /**
     * save image as movie frames
     */
    public void saveFrame()
    {
        String relative = outdirname + "/cache/####.png";
        String filename = Utility.getAbsolutePathname(relative);
        pApplet.saveFrame(filename);
    }

    /**
     * make movie from frames using ffmpeg
     *
     * @param frameFPS fps of input frames
     * @param movieFPS fps of output movie
     */
    public void makeMovie(int frameFPS, int movieFPS)
    {
        String imgname = outdirname + "/cache/%04d.png";
        String outname = outdirname + "/" + Utility.timestamp() + ".mp4";
        ProcessBuilder builder = new ProcessBuilder("ffmpeg", "-y", "-loglevel", "16",
                "-r", "" + frameFPS, "-i", imgname, "-vcodec", "libx264", "-pix_fmt", "yuv420p",
                "-r", "" + movieFPS, outname);
        exec(builder);
        System.out.println("Create movie file: " + outname);
    }

    private void exec(ProcessBuilder builder)
    {
        try
        {
            Process process = builder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                System.out.println(line);
            }
            process.waitFor();
        }
        catch (IOException | InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }
}
