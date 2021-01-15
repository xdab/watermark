package mini.xdab;

import mini.xdab.Experimental.LSBVisualizer;

import java.awt.image.BufferedImage;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        DigitalWatermark digiWm = new LSBWatermark();
        var img = ImageUtils.loadFromFile("testInput1.png");
        String msg = "Hello World!";

        var vis = LSBVisualizer.process(img);
        ImageUtils.saveToFile(vis, "lsbInput1.png");

        for (int i = 0; i < 1; ++i)
            digiWm.write(img, msg);

        ImageUtils.saveToFile(img, "testOutput1.png");

        var img2 = ImageUtils.loadFromFile("testOutput1.png");
        String msg2 = digiWm.readString(img2);
        System.out.println("Decoded message: '" + msg2 + "'");

        var vis2 = LSBVisualizer.process(img2);
        ImageUtils.saveToFile(vis2, "lsbOutput1.png");
    }
}
