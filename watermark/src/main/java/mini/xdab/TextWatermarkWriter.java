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
        StringBuilder sb = new StringBuilder();
        for (byte b : data) {
            if (font.canDisplay(b)) {
                sb.append((char)b);
            } else {
                sb.append("\\x");
                sb.append(Integer.toHexString(b));
            }
        }
        write(img, sb.toString());
        return;
    }

    @Override
    public void write(BufferedImage img, String string) {
        //TODO
    }

}
