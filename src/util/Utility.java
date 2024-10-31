package util;

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
}
