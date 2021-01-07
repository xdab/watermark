package mini.xdab;

import java.awt.image.BufferedImage;


public interface IWatermarkWriter {

    void write(BufferedImage img, byte[] data);

    void write(BufferedImage img, String string);

}
