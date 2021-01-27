package mini.xdab;

import mini.xdab.tools.LSBVisualizer;
import mini.xdab.analog.TextWatermarkWriter;
import mini.xdab.digital.DigitalWatermark;
import mini.xdab.digital.LSBWatermark;
import mini.xdab.utils.ImageUtils;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        IWatermarkWriter anlgWm = new TextWatermarkWriter();
        DigitalWatermark digiWm = new LSBWatermark();
        var img = ImageUtils.loadFromFile("testInput1.png");
        String msg = "Hello World!";

        var vis = LSBVisualizer.process(img);
        ImageUtils.saveToFile(vis, "lsbInput1.png");

        anlgWm.write(img, msg);
        digiWm.write(img, msg);

        ImageUtils.saveToFile(img, "testOutput1.png");

        var img2 = ImageUtils.loadFromFile("testOutput1.png");
        String msg2 = digiWm.readString(img2);
        System.out.println("Decoded message: '" + msg2 + "'");

        var vis2 = LSBVisualizer.process(img2);
        ImageUtils.saveToFile(vis2, "lsbOutput1.png");
    }
}
