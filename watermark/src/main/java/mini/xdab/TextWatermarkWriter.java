package mini.xdab;

import java.awt.*;
import java.awt.image.BufferedImage;


public class TextWatermarkWriter implements IWatermarkWriter {

    protected Font font;
    protected Color color;
    protected double alpha;
    protected int placement;


    @Override
    public void write(BufferedImage img, byte[] data) {
        write(img, TextUtils.bytesToString(data));
    }

    @Override
    public void write(BufferedImage img, String string) {
        //TODO: CREATE write(BufferedImage img, String string)
    }

}
