package mini.xdab;

import java.awt.image.BufferedImage;


public interface IWatermarkReader {

    byte[] read(BufferedImage img);

    String readString(BufferedImage img);

}
