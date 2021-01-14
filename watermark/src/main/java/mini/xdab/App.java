package mini.xdab;

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

        digiWm.write(img, msg);
        ImageUtils.saveToFile(img, "testOutput1.png");

        var img2 = ImageUtils.loadFromFile("testOutput1.png");
        String msg2 = digiWm.readString(img2);
    }
}
